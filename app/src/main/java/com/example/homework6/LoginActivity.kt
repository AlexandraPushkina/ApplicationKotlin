package com.example.homework6

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.homework6.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    // Объявляем переменную для View Binding
    private lateinit var binding: ActivityLoginBinding
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)  //activity_login

        dbHelper = DatabaseHelper(this)

        // Устанавливаем слушатель нажатий на кнопку
        binding.loginButton.setOnClickListener {
            loginUser()
        }
    }

    private fun loginUser() {
        // Получаем текст из полей ввода
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()

        // Проверяем email и пароль с помощью нашего валидатора
        val emailError = AuthValidator.validateEmail(email)
        val passwordError = AuthValidator.validatePassword(password)

        // Показываем или убираем ошибки
        binding.emailEditText.error = emailError
        binding.passwordEditText.error = passwordError

        if (emailError == null && passwordError == null) {
        // 1. Создаем экземпляр фрагмента
            val fragment = NewUserIntroduceFragment()

        // 2. Упаковываем данные (email и пароль), чтобы передать их во фрагмент
            val bundle = Bundle()  // упаковка (похожа на Структуру)
            bundle.putString("user_email", email)
            bundle.putString("user_password", password)
            fragment.arguments = bundle

         // 3. Открываем фрагмент
            // R.id.fragment_container — это ID контейнера в activity_login.xml
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null) // Чтобы можно было вернуться назад кнопкой "Назад"
                .commit()
        }
    }
}