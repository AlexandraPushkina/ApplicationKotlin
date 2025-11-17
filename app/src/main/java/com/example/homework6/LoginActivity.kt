package com.example.homework6

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.homework6.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    // Объявляем переменную для View Binding
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)  //activity_login

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
            Toast.makeText(this, "Вход выполнен успешно!", Toast.LENGTH_SHORT).show()

        }
    }
}