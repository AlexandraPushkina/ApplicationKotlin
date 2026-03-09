package com.example.homework6.extensions

import android.content.Context
import android.content.Intent
import com.example.homework6.MainActivity

// Расширения для глобального доступа из каждого файла

// Ключ для передачи ID
const val EXTRA_USER_ID = "extra_user_id"

fun Context.navigateToMain(userId: Int) {
    val intent = Intent(this, MainActivity::class.java).apply {
        // Очищаем стек, чтобы нельзя было вернуться на экран логина по кнопке "Назад"
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        putExtra(EXTRA_USER_ID, userId)
    }
    startActivity(intent)
}