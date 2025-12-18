package com.example.homework6.viewmodels

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homework6.data.AppDatabase
import com.example.homework6.data.entities.PostEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreatePostViewModel(private val db: AppDatabase) : ViewModel() {
    // 1. "Событие" успеха.
    // Используем Unit, потому что не важно значение, важен сам факт завершения.
    private val _postCreatedSuccess = MutableLiveData<Unit>()
    val postCreatedSuccess: LiveData<Unit> = _postCreatedSuccess

    // 2. Событие ошибки (если вдруг база упадет)
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun createPost(title: String,
                   content: String,
                   imageUrl: String?,
                   username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val user = db.userDao().getUser(username) ?: return@launch

                    val newPost = PostEntity(id = 0,
                        userId = user.id.toLong(),
                        authorName = username,
                        title = title,
                        content = content,
                        imageUrl = imageUrl,
                        topicIds = "5") // TODO: правильно указывать темы)

                db.postDao().insert(newPost)
                _postCreatedSuccess.postValue(Unit)

                }
            catch (e: Exception) {
                _error.postValue("Ошибка: ${e.message}")
            }
        }
    }
}