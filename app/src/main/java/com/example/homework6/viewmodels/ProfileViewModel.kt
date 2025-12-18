package com.example.homework6.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
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
    // Необходимо для отправки пользователю в UI
    private val _topicNames = MutableLiveData<List<String>>()
    val topicNames: LiveData<List<String>> = _topicNames

    // Функция загрузки
    fun loadProfile(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            // А. Грузим пользователя
            val user = db.userDao().getUser(username)

            // Если пользователя нет — выходим
            if (user == null) return@launch

            // Отправляем пользователя в UI (для имени и био)
            _userProfile.postValue(user)

            // Б. Грузим темы
            val allTopics = db.topicDao().getAllTopics()

            // В. Поиск topicid, которые есть у пользователя
            try {
                // 1. Превращаем строку "1, 5" в список чисел [1, 5]
                val userTopicIds = user.topicId
                    .split(",")
                    .filter { it.isNotBlank() } // убираем пустые, если есть
                    .map { it.trim().toInt() }

                // 2. Находим темы, чьи ID есть в списке у пользователя, и берем их названия
                val finalTopicNames = allTopics
                    .filter { topic -> topic.id in userTopicIds } // Оставляем только нужные темы
                    .map { topic -> topic.name } // Превращаем объекты тем просто в названия

                // Г. Отправляем готовый список названий в UI
                _topicNames.postValue(finalTopicNames)

            } catch (e: Exception) {
                // Если вдруг строка с ID битая, можно отправить пустой список
                _topicNames.postValue(emptyList())
            }
        }
    }
}