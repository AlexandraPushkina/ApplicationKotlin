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
    fun loadProfile(userEmail: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // 1. Грузим пользователя из базы
                val user = db.userDao().getUserByEmail(userEmail)

                // Если пользователя нет — прерываем работу корутины
                if (user == null) {
                    return@launch
                }

                // Отправляем пользователя в UI (Фрагмент получит имя и био)
                _userProfile.postValue(user)

                // 2. Достаем ВСЕ интересы этого пользователя
                val allUserInterests = db.UserInterestsDao().getUserInterests(user.id)

                // 3. Отбираем только те ID тем, которые вес > 0
                val positiveTopicIds = mutableListOf<Int>()

                for (interest in allUserInterests) {
                    if (interest.weight > 0) {
                        positiveTopicIds.add(interest.topicId)
                    }
                }

                // Г. Грузим ВСЕ темы из базы данных (чтобы узнать их текстовые названия)
                // Примечание: Убедитесь, что у вас в TopicDao метод называется именно так и
                // возвращает List<TopicEntity> (то есть объекты с id и name)
                val allTopics = db.topicDao().getAllTopicsList()

                // Д. Формируем финальный список названий
                val finalTopicNames = mutableListOf<String>()

                // Перебираем все темы из базы.
                // Если ID темы есть в нашем списке "положительных" интересов — берем её название.
                for (topic in allTopics) {
                    if (positiveTopicIds.contains(topic.id)) {
                        finalTopicNames.add(topic.name)
                    }
                }

                // Е. Отправляем готовый список названий в UI
                _topicNames.postValue(finalTopicNames)

            } catch (e: Exception) {
                // В случае любой ошибки при работе с БД, отправляем пустой список,
                // чтобы приложение не упало
                _topicNames.postValue(emptyList())
            }
        }
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