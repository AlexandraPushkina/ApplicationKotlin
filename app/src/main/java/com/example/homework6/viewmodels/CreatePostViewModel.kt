package com.example.homework6.viewmodels

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homework6.PostRepository
import com.example.homework6.data.AppDatabase
import com.example.homework6.data.entities.PostEntity
import com.example.homework6.data.entities.TopicEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreatePostViewModel(application: Application, private val repository: PostRepository) :
    AndroidViewModel(application) {
    // 1. "Событие" успеха.
    // Используем Unit, потому что не важно значение, важен сам факт завершения.
    private val _postCreatedSuccess = MutableLiveData<Unit>()
    val postCreatedSuccess: LiveData<Unit> = _postCreatedSuccess
    private val _allTopics = MutableLiveData<List<TopicEntity>>()
    val allTopics: LiveData<List<TopicEntity>> = _allTopics

    // 2. Событие ошибки (если вдруг база упадет)
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    // 2. Твой метод создания поста (рефакторинг)
    fun createPost(title: String,
                   content: String,
                   imageUrl: String?,
                   username: String,
                   topicIds: List<Long>) {
        if (topicIds.isEmpty()) {
            _error.postValue("Выберите хотя бы одну тему")
            return
        }
        if (topicIds.size > 10) {
            _error.postValue("Можно выбрать не более 10 тем")
            return
        }

        val email = getUserEmail()
        if (email == null) {
            _error.postValue("Ошибка: сессия пользователя не найдена")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val user = repository.getUserByEmail(email)
                if (user == null) {
                    _error.postValue("Пользователь с email $email не найден")
                    return@launch
                }
                Log.d("DEBUG_CREATE_POST","Пользователь $user - почта: $email")
                val newPost = PostEntity(
                    id = 0,
                    userId = user.id,
                    authorName = user.username,
                    title = title,
                    content = content,
                    imageUrl = imageUrl
                )
                repository.createPostWithTopics(newPost, topicIds)
                _postCreatedSuccess.postValue(Unit)
            } catch (e: Exception) {
                _error.postValue("Ошибка: ${e.message}")
            }
        }
    }
    fun getUsername(): String? {
        val sharedPref = getApplication<Application>().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        return sharedPref.getString("current_user_name", null)
    }

    fun getUserEmail(): String? {
        val sharedPref = getApplication<Application>().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        return sharedPref.getString("current_user_email", null)
    }


    fun loadTopics() {
        viewModelScope.launch(Dispatchers.IO) {
            val topics = repository.getAllTopics()
            _allTopics.postValue(topics)
        }
    }
}