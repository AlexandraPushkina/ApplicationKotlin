package com.example.homework6

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen() // Запустить начальный экран...
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_first) // setContentView - показать наш xml
    }
}
