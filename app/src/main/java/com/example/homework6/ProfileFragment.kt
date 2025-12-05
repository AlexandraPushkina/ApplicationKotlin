package com.example.homework6

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.example.homework6.databinding.FragmentProfileBinding

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var dbHelper: DatabaseHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)
        dbHelper = DatabaseHelper(requireContext())

        loadUserProfile()
    }

    private fun loadUserProfile() {
        // 1. Получаем сохраненный email пользователя из настроек
        // (Мы предполагаем, что вы сохранили его при логине. Если нет - шаг 4)
        val sharedPref = requireActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val email = sharedPref.getString("current_user_email", null)

        if (email != null) {
            // 2. Делаем запрос в базу
            val cursor = dbHelper.getUserProfile(email)

            if (cursor != null && cursor.moveToFirst()) {
                // 3. Достаем данные из курсора
                val username = cursor.getString(0)
                val bio = cursor.getString(1)
                val topicName = cursor.getString(2)

                // 4. Заполняем UI
                binding.tvUsername.text = username ?: "Неизвестный"
                binding.tvBio.text = bio ?: "Биография не заполнена"

                // Если тема нашлась, ставим её, если нет - пишем "Без темы"
                binding.chipTopic.text = topicName ?: "Без темы"

                cursor.close()
            }
        } else {
            binding.tvUsername.text = "Ошибка входа"
            binding.tvBio.text = "Пожалуйста, перезайдите в приложение"
        }
    }
}