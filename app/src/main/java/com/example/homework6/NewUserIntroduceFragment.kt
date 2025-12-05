package com.example.homework6

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.homework6.databinding.FragmentNewUserIntroduceBinding
import androidx.core.content.edit

class NewUserIntroduceFragment : Fragment(R.layout.fragment_new_user_introduce) {

    private lateinit var binding: FragmentNewUserIntroduceBinding // Используем ViewBinding
    private lateinit var dbHelper: DatabaseHelper

    // Переменные для данных, полученных из Activity
    private var email: String? = null
    private var password: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNewUserIntroduceBinding.bind(view)
        dbHelper = DatabaseHelper(requireContext())

        // 1. Достаем данные (email и пароль), которые передали из LoginActivity
        email = arguments?.getString("user_email")
        password = arguments?.getString("user_password")

        // 2. Отображаем имя пользователя (часть до @)
        if (email != null) {
            val username = email!!.substringBefore("@")
            binding.tvUsername.text = username
        }

        // 3. Обработка нажатия кнопки "Продолжить"
        binding.btnSaveBio.setOnClickListener {
            saveUserAndContinue()
        }
    }

    private fun saveUserAndContinue() {
        // Получаем введенное BIO
        val bioText = binding.etBio.text.toString()

        // Получаем ID выбранного чипа
        val selectedChipId = binding.chipGroupTopics.checkedChipId

        if (selectedChipId == View.NO_ID) {
            Toast.makeText(context, "Пожалуйста, выберите тему!", Toast.LENGTH_SHORT).show()
            return
        }

        // Сопоставляем ID чипа
        val topicDatabaseId = when (selectedChipId) {
            R.id.chipNature -> 1      // Если выбран чип с id chipNature -> пишем в базу 1
            R.id.chipArt -> 2         // Искусство
            R.id.chipCosmetics -> 3   // Косметика
            R.id.chipSport -> 4       // Спорт
            R.id.chipFood -> 5        // Еда
            else -> 1 // На всякий случай
        }

        // Сохраняем в Базу Данных
        if (email != null && password != null) {
            dbHelper.addUser(email!!, password!!,bioText, topicDatabaseId)

            Toast.makeText(context, "Регистрация завершена!", Toast.LENGTH_SHORT).show()

            // Переход на MainActivity
            val intent = Intent(requireContext(), MainActivity::class.java)

            // Очищаем стек, чтобы нажав "Назад" в приложении нельзя было вернуться на экран регистрации
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            // Сохраняем email текущего пользователя
            val sharedPref = requireActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
            sharedPref.edit { putString("current_user_email", email) }

            startActivity(intent)
        }
    }
}