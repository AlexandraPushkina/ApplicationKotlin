package com.example.homework6

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.homework6.data.AppDatabase
import com.example.homework6.data.test_data.generateTestPosts
import com.example.homework6.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Находим NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Связываем NavController с нашей BottomNavigationView
        // Эта одна строка автоматически обрабатывает все нажатия!
        binding.bottomNavView.setupWithNavController(navController)

        val db = AppDatabase.getDatabase(this)

        // Запускаем корутину (в фоновом потоке IO)
        lifecycleScope.launch(Dispatchers.IO) {
            val count = db.postDao().getCount()

            // Если постов нет (равно 0), то загружаем тестовые посты
            if (count == 0) {
                db.postDao().deleteAll()
                val testPosts = generateTestPosts()
                db.postDao().insertAll(testPosts)
            }
        }
    }
}