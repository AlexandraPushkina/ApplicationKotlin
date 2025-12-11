package com.example.homework6

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.homework6.data.AppDatabase
import com.example.homework6.data.entities.UserEntity
import com.example.homework6.databinding.FragmentNewUserIntroduceBinding
import kotlinx.coroutines.launch
import androidx.core.content.edit

class NewUserIntroduceFragment : Fragment(R.layout.fragment_new_user_introduce) {

    private lateinit var binding: FragmentNewUserIntroduceBinding

    // Переменные для данных
    private var username: String? = null
    private var password: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNewUserIntroduceBinding.bind(view)

        // 1. Достаем данные.
        username = arguments?.getString("user_name")
        password = arguments?.getString("user_password")

        // 2. Отображаем имя
        binding.tvUsername.text = username ?: "Пользователь"

        // 3. Кнопка сохранения
        binding.btnSaveBio.setOnClickListener {
            saveUserAndContinue()
        }
    }

    private fun saveUserAndContinue() {
        val bioText = binding.etBio.text.toString().trim()

        // Получаем список ID всех нажатых чипов
        val selectedChipIds = binding.chipGroupTopics.checkedChipIds

        if (selectedChipIds.isEmpty()) {
            Toast.makeText(requireContext(), "Пожалуйста, выберите хотя бы одну тему!", Toast.LENGTH_SHORT).show()
            return
        }

        // Создаем список для хранения ID тем (тех, которые для БД: 1, 2, 3...)
        val dbTopicIdsList = mutableListOf<Int>()

        // Проходимся по каждому выбранному чипу и конвертируем его ID в ID темы
        for (chipId in selectedChipIds) {
            val topicId = when (chipId) {
                R.id.chipNature -> 1
                R.id.chipArt -> 2
                R.id.chipCosmetics -> 3
                R.id.chipSport -> 4
                R.id.chipFood -> 5
                else -> null // Игнорируем неизвестные (на всякий случай)
            }
            if (topicId != null) {
                dbTopicIdsList.add(topicId)
            }
        }

        // Превращаем список чисел [1, 4, 5] в строку "1,4,5"
        val topicIdsString = dbTopicIdsList.joinToString(separator = ",")

        if (username != null && password != null) {

            viewLifecycleOwner.lifecycleScope.launch {

                // Создаем юзера с новой строкой тем
                val newUser = UserEntity(
                    id = 0,
                    username = username!!,
                    password = password!!,
                    bio = bioText,
                    topicId = topicIdsString // Передаем строку
                )

                val db = AppDatabase.getDatabase(requireContext())
                db.userDao().insertUser(newUser)

                // --- УСПЕШНО ---
                val sharedPref = requireActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
                sharedPref.edit { putString("current_user_name", username) }

                Toast.makeText(requireContext(), "Регистрация завершена!", Toast.LENGTH_SHORT).show()

                val intent = Intent(requireContext(), MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        } else {
            Toast.makeText(requireContext(), "Ошибка передачи данных", Toast.LENGTH_SHORT).show()
        }
    }
}