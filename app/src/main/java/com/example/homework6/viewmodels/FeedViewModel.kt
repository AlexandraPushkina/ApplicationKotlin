package com.example.homework6.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homework6.data.AppDatabase
import com.example.homework6.data.entities.PostEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FeedViewModel(private val db: AppDatabase) : ViewModel() {

    // "Ящик", в который мы положим готовый список постов.
    // Фрагмент будет смотреть на этот ящик.
    private val _posts = MutableLiveData<List<PostEntity>>()
    val posts: LiveData<List<PostEntity>> = _posts

    // Функция загрузки данных
    fun loadPosts(username: String) {
        viewModelScope.launch(Dispatchers.IO) {
            // 1. Берем посты
            val allPosts = db.postDao().getAllPosts()

            // 2. Берем интересы пользователя
            val user = db.userDao().getUser(username) ?: return@launch

            val userInterests = user.topicId
                .split(",")
                .map { it.trim().toInt() }

            // 3. Фильтруем
            val filteredPosts = allPosts.filter { post ->
                val postTopicIds = post.topicIds
                    .split(",")
                    .map { it.trim().toInt() }
                postTopicIds.any { it in userInterests }
            }

            // 4. Кладем результат в LiveData (postValue безопасно вызывает обновление на UI потоке)
            _posts.postValue(filteredPosts)
        }
    }
}