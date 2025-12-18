package com.example.homework6.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.homework6.data.AppDatabase

// Factory (Фабрика) - используется для передачи другим ViewModel Context для обращения к бд
class AppViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = AppDatabase.getDatabase(application)

        // Перечисляем все ViewModel
        if (modelClass.isAssignableFrom(FeedViewModel::class.java)) {
            return FeedViewModel(db) as T
        }
        if (modelClass.isAssignableFrom(CreatePostViewModel::class.java)) {
            return CreatePostViewModel(db) as T
        }
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(db) as T
        }
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(db) as T
        }
        if (modelClass.isAssignableFrom(NewUserIntroduceViewModel::class.java)) {
            return NewUserIntroduceViewModel(db) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}