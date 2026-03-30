package com.example.homework6.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.homework6.data.AppDatabase
import com.example.homework6.data.entities.UserInterestsEntity
import com.example.homework6.utils.InteractionWeights
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ContentInteractionsViewModel(private val db: AppDatabase) : ViewModel() {

    fun handleInteraction(userId: Int, topicId: Int, weightDelta: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // 1. ПРЕДОХРАНИТЕЛЬ:
                // Если мы добавили новую тему в приложение ПОСЛЕ регистрации этого юзера,
                // у него не будет связи с этой темой в базе.
                // Эта строчка создаст связь с весом 0. Если связь уже есть, она ничего не сделает (IGNORE).
                val newInterest = UserInterestsEntity(
                    userId = userId,
                    topicId = topicId,
                    weight = 0
                )
                db.UserInterestsDao().insertInterestIfNotExists(newInterest)

                // 2. ОБНОВЛЕНИЕ ВЕСА:
                // Прибавляем или отнимаем переданное значение (weightDelta)
                db.UserInterestsDao().updateInterestWeight(userId, topicId, weightDelta)

                // Для отладки (потом можно удалить):
                Log.d("ContentInteractions", "User $userId interacted with Topic $topicId. Weight changed by: $weightDelta")

            } catch (e: Exception) {
                Log.e("ContentInteractions", "Error updating weight: ${e.message}")
            }
        }
    }

    fun onPostViewed(userId: Int, topicId: Int) {
        handleInteraction(userId, topicId, InteractionWeights.VIEW_POST)
    }

    fun onPostLiked(userId: Int, topicId: Int) {
        handleInteraction(userId, topicId, InteractionWeights.LIKE_POST)
    }

    fun onPostCommented(userId: Int, topicId: Int) {
        handleInteraction(userId, topicId, InteractionWeights.COMMENT_POST)
    }

    fun onPostHidden(userId: Int, topicId: Int) {
        handleInteraction(userId, topicId, InteractionWeights.DONT_SHOW_AGAIN)
    }
}

class ContentInteractionsViewModelFactory(private val db: AppDatabase) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContentInteractionsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ContentInteractionsViewModel(db) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
