package com.example.homework6.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homework6.data.AppDatabase
import com.example.homework6.data.entities.PostEntity
import com.example.homework6.data.test_data.generateTestPosts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val db: AppDatabase) : ViewModel() {

    // Функция инициализации данных
    fun initDatabase() {
        viewModelScope.launch(Dispatchers.IO) {
            val count = db.postDao().getCount()

            // Если постов нет (равно 0), то загружаем тестовые посты
            if (count == 0) {
                // db.postDao().deleteAll() // Снять комментарий при необходимости зачистки постов
                val testPosts = generateTestPosts()
                db.postDao().insertAll(testPosts)
            }
        }
    }
}
