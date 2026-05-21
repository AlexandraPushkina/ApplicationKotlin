package com.example.homework6.viewmodels

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

class CreatePostViewModel(private val repository: PostRepository) : ViewModel() {
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
                   topicIds: List<Int>) {
        if (topicIds.isEmpty()) {
            _error.postValue("Выберите хотя бы одну тему")
            return
        }
        if (topicIds.size > 10) {
            _error.postValue("Можно выбрать не более 10 тем")
            return
        }

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val user = repository.getUserByEmail(username)?: return@launch
                val newPost = PostEntity(
                    id = 0,
                    userId = user.id,
                    authorName = username,
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

    fun loadTopics() {
        viewModelScope.launch(Dispatchers.IO) {
            val topics = repository.getAllTopics()
            _allTopics.postValue(topics)
        }
    }
}