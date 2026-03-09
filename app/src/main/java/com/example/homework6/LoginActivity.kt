package com.example.homework6

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.homework6.data.AppDatabase
import com.example.homework6.databinding.ActivityLoginBinding
import com.example.homework6.extensions.navigateToMain
import com.example.homework6.extensions.showErrorMessage
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen() // Экран заставки (если настроен)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Слушатель нажатия на кнопку "Далее" (или "Войти")
        binding.loginButton.setOnClickListener {
            lifecycleScope.launch {
                handleNextStep()
            }
        }

        binding.registerButton.setOnClickListener {
            openIntroduceFragment(binding.userEmailEditText.text.toString().trim(),
                                binding.passwordEditText.text.toString())
        }
    }

    private suspend fun handleNextStep() {

        // 1. Получаем данные
        val userEmail = binding.userEmailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString()

        // 2. Валидация (проверка на пустоту, длину и т.д.)
        val emailError = AuthValidator.validateEmail(userEmail)
        val passwordError = AuthValidator.validatePassword(password)

        // Отображаем ошибки, если есть
        binding.userEmailEditText.error = emailError
        binding.passwordEditText.error = passwordError

        // 3. Если ошибок нет — передаем данные дальше
        if (emailError == null && passwordError == null) {
            performLogin(userEmail, password)
        }
    }

    suspend fun performLogin(email: String, pass: String) {
        val db = AppDatabase.getDatabase(this)
        val user = db.userDao().getUserByEmail(email)

        if (user == null) {
            // Почта не найдена
            showErrorMessage("Такой почты не существует!")
        }
        else if (db.userDao().checkUserByEmailAndPassword(email, pass) == null) {
            // Пароль не совпал
            showErrorMessage("Неверный пароль для входа")
        } else {
            // Успешный вход
            navigateToMain(user.id)
        }
    }

    private fun openIntroduceFragment(userEmail: String, password: String) {
        val fragment = RegisterNewUserFragment()

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