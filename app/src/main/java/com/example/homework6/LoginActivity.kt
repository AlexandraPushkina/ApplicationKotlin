package com.example.homework6

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.homework6.data.userDao
import com.example.homework6.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen() // Экран заставки (если настроен)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Слушатель нажатия на кнопку "Далее" (или "Войти")
        binding.loginButton.setOnClickListener {
            handleNextStep()
        }

        binding.registerButton.setOnClickListener {
            openIntroduceFragment()
        }
    }

    private fun handleNextStep() {

        // 1. Получаем данные
        val userEmail = binding.userNameEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString()

        // 2. Валидация (проверка на пустоту, длину и т.д.)
        val emailError = AuthValidator.validateEmail(userEmail)
        val passwordError = AuthValidator.validatePassword(password)

        // Отображаем ошибки, если есть
        binding.userNameEditText.error = emailError
        binding.passwordEditText.error = passwordError

        // 3. Если ошибок нет — передаем данные дальше
        if (emailError == null && passwordError == null) {
            performLogin(userEmail, password)
        }
    }

    suspend fun performLogin(email: String, pass: String) {
        // Используем метод из вашего DAO
        val user = userDao.getUserByEmail(email)

        if (user == null) {
            // Почта не найдена
            showErrorMessage("Неверные данные для входа")
        } else if (user.password != pass) {
            // Пароль не совпал
            showErrorMessage("Неверные данные для входа")
        } else {
            // Успешный вход! Переходим на главную
            navigateToMain(user.id)
        }
    }

    private fun openIntroduceFragment() {
        val userEmail = binding.userNameEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString()

        val fragment = NewUserIntroduceFragment()

        // Упаковываем данные в структуру (Bundle), чтобы фрагмент их получил
        val bundle = Bundle()
        bundle.putString("user_name", userEmail)
        bundle.putString("user_password", password)
        fragment.arguments = bundle

        // Открываем фрагмент
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}
