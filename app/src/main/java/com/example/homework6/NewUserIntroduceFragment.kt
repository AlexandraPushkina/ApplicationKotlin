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
        val selectedChipId = binding.chipGroupTopics.checkedChipId

        if (selectedChipId == View.NO_ID) {
            Toast.makeText(requireContext(), "Пожалуйста, выберите тему!", Toast.LENGTH_SHORT).show()
            return
        }

        // Сопоставляем ID чипа с ID в таблице topics
        val topicDatabaseId = when (selectedChipId) {
            R.id.chipNature -> 1
            R.id.chipArt -> 2
            R.id.chipCosmetics -> 3
            R.id.chipSport -> 4
            R.id.chipFood -> 5
            else -> 1
        }

        if (username != null && password != null) {

            // Запускаем корутину для работы с Room
            viewLifecycleOwner.lifecycleScope.launch {

                // Создаем объект. id = 0, чтобы Room сам сгенерировал новый
                val newUser = UserEntity(
                    id = 0,
                    username = username!!,
                    password = password!!,
                    bio = bioText,
                    topicId = topicDatabaseId
                )

                // Получаем БД и вставляем (без проверок, просто добавляем запись)
                val db = AppDatabase.getDatabase(requireContext())
                db.userDao().insertUser(newUser)

                // --- УСПЕШНО СОХРАНЕНО ---

                // Сохраняем имя текущего пользователя в настройки (для Профиля)
                val sharedPref = requireActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
                sharedPref.edit().putString("current_user_name", username).apply()

                Toast.makeText(requireContext(), "Регистрация завершена!", Toast.LENGTH_SHORT).show()

                // Переход на главный экран (MainActivity)
                val intent = Intent(requireContext(), MainActivity::class.java)
                // Очищаем стек (чтобы нельзя было вернуться назад на экран регистрации)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        } else {
            Toast.makeText(requireContext(), "Ошибка передачи данных", Toast.LENGTH_SHORT).show()
        }
    }
}