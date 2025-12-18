package com.example.homework6.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homework6.data.AppDatabase
import com.example.homework6.data.entities.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NewUserIntroduceViewModel(private val db: AppDatabase) : ViewModel() {

    // Сигнал об успешной регистрации
    private val _registrationSuccess = MutableLiveData<Unit>()
    val registrationSuccess: LiveData<Unit> = _registrationSuccess

    // Сигнал об ошибке
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun registerUser(username: String, password: String, bio: String, topicIds: List<Int>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // 1. Превращаем список чисел [1, 4, 5] в строку "1,4,5"
                // Если список пустой — строка будет пустой, но мы проверим это во фрагменте
                val topicIdsString = topicIds.joinToString(separator = ",")

                // 2. Создаем юзера
                val newUser = UserEntity(
                    id = 0,
                    username = username,
                    password = password,
                    bio = bio,
                    topicId = topicIdsString
                )

                // 3. Сохраняем в базу
                db.userDao().insertUser(newUser)

                // 4. Сообщаем об успехе
                _registrationSuccess.postValue(Unit)

            } catch (e: Exception) {
                _error.postValue("Ошибка регистрации: ${e.message}")
            }
        }
    }
}