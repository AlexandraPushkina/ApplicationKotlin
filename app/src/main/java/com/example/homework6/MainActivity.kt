package com.example.homework6

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.homework6.databinding.ActivityMainBinding
import com.example.homework6.viewmodels.AppViewModelFactory
import com.example.homework6.viewmodels.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // Подключаем ViewModel
    private val viewModel: MainViewModel by viewModels {
        AppViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 1. Настройка навигации
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavView.setupWithNavController(navController)

        // 2. Инициализация базы данных (проверка и заливка тестовых данных)
        viewModel.initDatabase()
    }
}
