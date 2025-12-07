package com.example.homework6.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.homework6.data.entities.UserEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class UserViewModel(private val dao: UserDao) : ViewModel() {

    // Получаем поток данных из базы. Compose будет следить за ним.
    val allUsers: Flow<List<UserEntity>> = dao.getAllUsersFlow()

    // Функция добавления (запускаем в lifecycleScope)
    fun addUser(username: String, password: String, bio: String, topic: Int) {

        if (username.isNotBlank()) {
            viewModelScope.launch {
                dao.insertUser(
                    UserEntity(
                        username = username, password = password,
                        bio = bio, topicId = topic
                    )
                )
            }
        }
    }

    // Функция удаления
    fun deleteUser(user: UserEntity) {
        viewModelScope.launch {
            dao.deleteUser(user)
        }
    }
}

// Фабрика нужна, чтобы передать DAO в конструктор ViewModel
class UserViewModelFactory(private val dao: UserDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}