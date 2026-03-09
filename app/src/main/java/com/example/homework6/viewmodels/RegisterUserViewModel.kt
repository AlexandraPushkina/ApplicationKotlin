package com.example.homework6.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homework6.data.AppDatabase
import com.example.homework6.data.entities.UserEntity
import com.example.homework6.data.entities.UserInterestsEntity
import com.example.homework6.utils.InteractionWeights
import com.example.homework6.utils.InteractionWeights.REGISTER_CHOICE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterUserViewModel(private val db: AppDatabase) : ViewModel() {

    // Сигнал об успешной регистрации
    private val _registrationSuccess = MutableLiveData<Unit>()
    val registrationSuccess: LiveData<Unit> = _registrationSuccess

    // Сигнал об ошибке
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun registerUser(useremail: String,
                     password: String,
                     username: String,
                     bio: String,
                     topicIds: List<Int>,
                     onSuccess: (Int) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // 1. Создаем юзера
                val newUser = UserEntity(
                    id = 0,
                    useremail = useremail,
                    password = password,
                    username = username,
                    bio = bio
                )

                // 2. Сохраняем в таблицу users
                val insertedUserId = db.userDao().insertUser(newUser).toInt()

                // 3. Берем всевозможные темы
                val allTopicIdsInDb = db.topicDao().getAllTopicsIds()

                // 4. Формируем полный список интересов.
                val initialInterests = allTopicIdsInDb.map { currentTopicId ->

                    // Проверяем: есть ли текущая тема в списке тех, что выбрал юзер?
                    val initialWeight = if (topicIds.contains(currentTopicId)) {
                        InteractionWeights.REGISTER_CHOICE
                    } else {
                        InteractionWeights.NEUTRAL
                    }

                    UserInterestsEntity(
                        userId = insertedUserId,
                        topicId = currentTopicId,
                        weight = initialWeight
                    )
                }

                if (initialInterests.isNotEmpty()) {
                    db.UserInterestsDao().insertInterests(initialInterests)
                }

                _registrationSuccess.postValue(Unit)

            } catch (e: Exception) {
                _error.postValue("Ошибка регистрации: ${e.message}")
            }
        }
    }
}