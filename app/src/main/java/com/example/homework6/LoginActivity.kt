package com.example.homework6

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
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
    }

    private fun handleNextStep() {

        // 1. Получаем данные
        val username = binding.userNameEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()

        // 2. Валидация (проверка на пустоту, длину и т.д.)
        val nameError = AuthValidator.validateUserName(username)
        val passwordError = AuthValidator.validatePassword(password)

        // Отображаем ошибки, если есть
        binding.userNameEditText.error = nameError
        binding.passwordEditText.error = passwordError

        // 3. Если ошибок нет — передаем данные дальше
        if (nameError == null && passwordError == null) {
            openIntroduceFragment(username, password)
        }
    }

    private fun openIntroduceFragment(username: String, pass: String) {
        val fragment = NewUserIntroduceFragment()

        // Упаковываем данные в структуру (Bundle), чтобы фрагмент их получил
        val bundle = Bundle()
        bundle.putString("user_name", username)
        bundle.putString("user_password", pass)
        fragment.arguments = bundle

        // Открываем фрагмент
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}
