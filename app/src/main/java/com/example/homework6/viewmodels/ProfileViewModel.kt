package com.example.homework6.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.homework6.data.AppDatabase
import com.example.homework6.data.entities.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileViewModel(private val db: AppDatabase) : ViewModel() {

    // 1. Данные пользователя (Имя, Био)
    private val _userProfile = MutableLiveData<UserEntity>()
    val userProfile: LiveData<UserEntity> = _userProfile

    // 2. Список НАЗВАНИЙ интересов (например: ["Спорт", "Музыка"])
    private val _topicNames = MutableLiveData<List<String>>()
    val topicNames: LiveData<List<String>> = _topicNames

    // Функция загрузки
    fun loadProfile(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // 1. Грузим пользователя из базы
                val user = fetchUser(userId)

                // Если пользователя нет — прерываем работу корутины
                if (user == null)
                    return@launch

                // Отправляем пользователя в UI (Фрагмент получит имя и био)
                _userProfile.postValue(user)

                // Загружаем его интересы
                fetchAndProcessInterests(userId)
            } catch (e: Exception) {
                // Защита от краша при сбоях БД
                _topicNames.postValue(emptyList())
            }
        }
    }

        private suspend fun fetchUser(userId: Int): UserEntity? {
            return db.userDao().getUserById(userId)
        }

    private suspend fun fetchAndProcessInterests(userId: Int) {
        // Достаем все интересы юзера
        // (Обратите внимание: userInterestsDao обычно пишется с маленькой буквы)
        val allUserInterests = db.UserInterestsDao().getUserInterests(userId)

        // Оставляем только те, где вес > 0, и берем только их ID
        // (Функции filter и map делают код короче и избавляют от циклов for)
        val positiveTopicIds = allUserInterests
            .filter { it.weight > 0 }
            .map { it.topicId }

        // Если положительных интересов нет, сразу отправляем пустой список и выходим
        if (positiveTopicIds.isEmpty()) {
            _topicNames.postValue(emptyList())
            return
        }

        // Грузим все темы
        val allTopics = db.topicDao().getAllTopicsList()

        // Находим названия нужных тем
        val finalTopicNames = allTopics
            .filter { positiveTopicIds.contains(it.id) }
            .map { it.name }

        // Отправляем готовый список названий во Фрагмент
        _topicNames.postValue(finalTopicNames)
    }
}

// Фабрика для ProfileViewModel (Обязательна, так как передаем AppDatabase)
class ProfileViewModelFactory(private val db: AppDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProfileViewModel(db) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}