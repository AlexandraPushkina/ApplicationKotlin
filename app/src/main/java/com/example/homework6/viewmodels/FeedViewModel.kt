package com.example.homework6.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.homework6.data.entities.PostEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.example.homework6.FeedRankingUseCase
import com.example.homework6.PostRepository
import com.example.homework6.data.UserDao
import com.example.homework6.data.entities.CommentEntity
import com.example.homework6.data.entities.TopicEntity
import com.example.homework6.data.entities.UserEntity

class FeedViewModel(
    private val userDao: UserDao,
    private val feedRankingUseCase: FeedRankingUseCase,
    private val repository: PostRepository
) : ViewModel() {

    private val _currentUser = MutableLiveData<UserEntity?>()
    val currentUser: LiveData<UserEntity?> = _currentUser

    private val _feedPosts = MutableLiveData<List<PostEntity>>()
    val feedPosts: LiveData<List<PostEntity>> = _feedPosts

    // Получение пользователя по ID
    fun getUser(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val user = userDao.getUserById(userId)
            _currentUser.postValue(user)
        }
    }

//    fun getUserByEmail(userEmail: String) {
//        viewModelScope.launch(Dispatchers.IO) {
//            val user = userDao.getUserByEmail(userEmail)
//            _currentUser.postValue(user)
//        }
//    }

    // Загрузка постов, учитывая веса (интересы) пользователя
    fun loadPosts(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val weights = repository.getUserWeights(userId)
            val result = feedRankingUseCase(weights)
            _feedPosts.postValue(result)
        }
    }

    fun getTopicsForPost(postId: Int): LiveData<List<TopicEntity>> {
        return repository.getTopicsForPost(postId)
    }

    fun incrementInterestWeights(userEmail: String, topics: List<TopicEntity>) {
        viewModelScope.launch {
            // Проходим по всем тегам поста и увеличиваем их вес в БД
            topics.forEach { topic ->
                val userId = repository.getUserByEmail(userEmail)
                if (userId != null)
                    repository.incrementInterestWeight(userId.id, topic.id)
            }
        }
    }
    fun isLiked(email: String, postId: Int?): LiveData<Boolean> {
        return postId?.let { repository.isLiked(email, it) }
            ?: MutableLiveData(false)
    }
    fun toggleLike(email: String, postId: Int?) {
        postId?.let { id ->
            viewModelScope.launch {
                repository.toggleLike(email, id)
            }
        }
    }

    fun hidePost(email: String, postId: Int?) {
        postId?.let { id ->
            viewModelScope.launch { repository.hidePost(email, id) }
        }
    }
    fun getComments(postId: Int?): LiveData<List<CommentEntity>> {
        return postId?.let { repository.getComments(it) } ?: MutableLiveData(emptyList())
    }

    fun sendComment(email: String, postId: Int?, content: String) {
        if (content.isBlank() || postId == null) return
        viewModelScope.launch {
            repository.addComment(email, postId, content)
        }
    }
}

class FeedViewModelFactory(
    private val userDao: UserDao,
    private val rankingUseCase: FeedRankingUseCase,
    private val repository: PostRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FeedViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FeedViewModel(userDao, rankingUseCase, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

