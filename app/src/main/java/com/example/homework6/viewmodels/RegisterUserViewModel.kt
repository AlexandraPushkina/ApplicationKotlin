package com.example.homework6.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homework6.PostRepository
import com.example.homework6.data.AppDatabase
import com.example.homework6.data.entities.TopicEntity
import com.example.homework6.data.entities.UserEntity
import com.example.homework6.data.entities.UserInterestsEntity
import com.example.homework6.utils.InteractionWeights.REGISTER_CHOICE
import com.example.homework6.utils.InteractionWeights.NEUTRAL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterUserViewModel(private val db: AppDatabase,
                            private val repository: PostRepository) : ViewModel() {

    // Сигнал об успешной регистрации
    private val _registrationSuccess = MutableLiveData<Int>()
    val registrationSuccess: LiveData<Int> = _registrationSuccess

    // Сигнал об ошибке
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    // Автоматически обновляемый список топиков
    private val _allTopics = MutableLiveData<List<TopicEntity>>()
    val allTopics: LiveData<List<TopicEntity>> = _allTopics
    init {
        // Если база пустая - придет пустой список. Как только она заполнится в фоне -
        // автоматически придет полный список топиков
        viewModelScope.launch(Dispatchers.IO) {
            db.topicDao().getAllTopicsFlow().collect { topics ->
                _allTopics.postValue(topics)
            }
        }
    }

    fun registerUser(useremail: String,
                     password: String,
                     username: String,
                     bio: String,
                     topicIds: List<Int>) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val newUser = UserEntity(
                    id = 0,
                    useremail = useremail,
                    password = password,
                    username = username,
                    bio = bio
                )

                val insertedUserId = db.userDao().insertUser(newUser).toInt()
                Log.d("DEBUG_DB", "Пользователь создан с ID: $insertedUserId")

                val allTopicIdsInDb = db.topicDao().getAllTopicsIds()

                val initialInterests = allTopicIdsInDb.map { currentTopicId ->

                    val initialWeight = if (topicIds.contains(currentTopicId)) {
                        REGISTER_CHOICE
                    } else {
                        NEUTRAL
                    }

                    UserInterestsEntity(
                        userId = insertedUserId,
                        topicId = currentTopicId,
                        weight = initialWeight
                    )
                }

                if (initialInterests.isNotEmpty()) {
                    db.userInterestsDao().insertInterests(initialInterests)
                    Log.d("DEBUG_DB", "Интересы для пользователя $insertedUserId успешно инициализированы. Кол-во: ${initialInterests.size}")
                } else {
                    Log.e("DEBUG_DB", "Список всех тем пуст! Проверь, заполнена ли таблица topics.")
                }

                _registrationSuccess.postValue(insertedUserId) // ожидаем id пользователя

            } catch (e: Exception) {
                Log.e("DEBUG_DB", "Ошибка при регистрации: ${e.message}")
                _error.postValue("Ошибка регистрации: ${e.message}")
            }
        }
    }
    fun getAllTopics() {
        viewModelScope.launch(Dispatchers.IO) {
            val topics = repository.getAllTopics()
            _allTopics.postValue(topics)
        }
    }
}