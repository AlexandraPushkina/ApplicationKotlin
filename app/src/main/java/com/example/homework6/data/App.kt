package com.example.homework6.data

// База данных инициализируется один раз на всё время жизни приложения и доступна во всех фрагментах.

import android.app.Application

class App : Application() {
    // Создаем базу данных лениво (только когда она понадобится)
    val database by lazy { AppDatabase.getDatabase(this) }
}