package com.example.homework6.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homework6.data.AppDatabase
import com.example.homework6.data.entities.PostEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.homework6.FeedRankingUseCase

class FeedViewModel(private val db: AppDatabase) : ViewModel() {

    // "Ящик", в который мы положим готовый список постов.
    // Фрагмент будет смотреть на этот ящик.
    private val _posts = MutableLiveData<List<PostEntity>>()
    val posts: LiveData<List<PostEntity>> = _posts

    // Функция загрузки данных
    fun loadPosts(userEmail: String) {
        viewModelScope.launch(Dispatchers.IO) {

            // 1. Находим пользователя
            val user = db.userDao().getUserByEmail(userEmail) ?: return@launch

            // 2. Получаем сырые данные из базы
            val allPosts = db.postDao().getAllPosts()
            val userInterestsList = db.UserInterestsDao().getUserInterests(user.id)

            // 3. Подготавливаем словарь весов { TopicId = Weight }
            val interestWeightsMap = userInterestsList.associate { it.topicId to it.weight }

            // 4. МАГИЯ ЗДЕСЬ: Отдаем сырые данные нашему UseCase и получаем умную ленту
            val smartFeed = FeedRankingUseCase()
            val feedCurrentUser = smartFeed.invoke(allPosts, interestWeightsMap)

            // 5. Отправляем во Фрагмент
            _posts.postValue(feedCurrentUser)
        }
    }
}