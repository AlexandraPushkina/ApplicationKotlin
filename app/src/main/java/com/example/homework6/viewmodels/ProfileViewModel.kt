package com.example.homework6.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.homework6.PostRepository
import com.example.homework6.data.AppDatabase
import com.example.homework6.data.entities.PostEntity
import com.example.homework6.data.entities.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch

class ProfileViewModel(private val db: AppDatabase,
                       repository: PostRepository) : ViewModel() {

    // Данные пользователя (Имя, Био)
    private val _userProfile = MutableLiveData<UserEntity>()
    val userProfile: LiveData<UserEntity> = _userProfile

    // Список интересов
    private val _topicNames = MutableLiveData<List<String>>()
    val topicNames: LiveData<List<String>> = _topicNames

    //Текущий пользователь (используется только при переходе)
    private val _currentUserId = MutableStateFlow<Int?>(null)
    @OptIn(ExperimentalCoroutinesApi::class)
    val likedPosts: LiveData<List<PostEntity>> = _currentUserId
        .filterNotNull() // Ждем, пока ID не станет известен (не null)
        .flatMapLatest { userId ->
            repository.getLikedPosts(userId)
        }
        .asLiveData()


    // Функция загрузки
    fun loadProfile(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _currentUserId.value = userId
                val user = fetchUser(userId)
                if (user == null)
                    return@launch

                // Отправление пользователя в UI
                _userProfile.postValue(user)

                // Загрузка его интересов
                fetchAndProcessInterests(userId)
            } catch (e: Exception) {
                _topicNames.postValue(emptyList())
            }
        }
    }

        private suspend fun fetchUser(userId: Int): UserEntity? {
            return db.userDao().getUserById(userId)
        }

    private suspend fun fetchAndProcessInterests(userId: Int) {
        val allUserInterests = db.userInterestsDao().getUserInterests(userId)
        val positiveTopicIds = allUserInterests
            .filter { it.weight > 0 }
            .map { it.topicId }

       if (positiveTopicIds.isEmpty()) {
            _topicNames.postValue(emptyList())
            return
        }

        val allTopics = db.topicDao().getAllTopicsList()
        val finalTopicNames = allTopics
            .filter { positiveTopicIds.contains(it.id) }
            .map { it.name }

        _topicNames.postValue(finalTopicNames)
    }
}