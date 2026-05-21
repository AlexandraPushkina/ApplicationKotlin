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
import com.example.homework6.PostRepository
import com.example.homework6.data.UserDao
import com.example.homework6.data.entities.UserEntity

class FeedViewModel(
    private val userDao: UserDao, // Добавляем DAO для работы с юзером
    private val feedRankingUseCase: FeedRankingUseCase
) : ViewModel() {

    // Стримы данных для фрагмента
    private val _currentUser = MutableLiveData<UserEntity?>()
    val currentUser: LiveData<UserEntity?> = _currentUser

    private val _feedPosts = MutableLiveData<List<PostEntity>>()
    val feedPosts: LiveData<List<PostEntity>> = _feedPosts

    // 1. Получаем пользователя по ID
    fun getUser(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = userDao.getUserById(userId)
            _currentUser.postValue(user)
        }
    }

    // 2. Загружаем посты, учитывая веса (интересы) пользователя
    fun loadPosts(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            // Сначала получаем пользователя, чтобы достать его веса
            val user = userDao.getUserById(userId)
            val weights = user?.interests ?: emptyMap() // Предполагаем, что веса хранятся в поле interests

            // Вызываем UseCase для ранжирования ленты
            val result = feedRankingUseCase(weights)
            _feedPosts.postValue(result)
        }
    }
}