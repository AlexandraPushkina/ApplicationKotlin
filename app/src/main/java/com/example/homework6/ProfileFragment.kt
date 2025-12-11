package com.example.homework6

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.homework6.data.AppDatabase
import com.example.homework6.databinding.FragmentProfileBinding
import kotlinx.coroutines.launch

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var binding: FragmentProfileBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentProfileBinding.bind(view)

        loadUserProfile()

        // TODO: Кнопка выхода (опционально, чтобы проверить логику)
    //        binding.btnLogout?.setOnClickListener {
    //            logout()
    //        }
    }

    private fun loadUserProfile() {
        val sharedPref = requireActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val username = sharedPref.getString("current_user_name", null)

        if (username == null) return

        viewLifecycleOwner.lifecycleScope.launch {
            val db = AppDatabase.getDatabase(requireContext())

            // 1. Получаем пользователя
            val user = db.userDao().getUser(username)

            // 2. Получаем список всех возможных тем
            val allTopics = db.topicDao().getAllTopics()

            if (user != null) {
                // Заполняем текстовые поля
                binding.tvUsername.text = user.username
                binding.tvBio.text = user.bio.ifEmpty { "Биография не заполнена" }

                // --- ЛОГИКА ЧИПОВ ---

                // 1. Очищаем группу перед добавлением
                binding.chipGroupInterests.removeAllViews()

                // 2. Парсим строку в список чисел []
                val userTopicIds = user.topicId.split(",")
                    .filter { it.isNotEmpty() }
                    .map { it.toInt() }

                // 3. Бежим по всем существующим темам
                for (topic in allTopics) {
                    // Если ID этой темы есть в списке у пользователя
                    if (userTopicIds.contains(topic.id)) {
                        // Создаем чип и добавляем на экран
                        addChipToGroup(topic.name)
                    }
                }

            } else {
                Toast.makeText(requireContext(), "Пользователь не найден", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Вспомогательная функция для создания Чипа
    private fun addChipToGroup(topicName: String) {
        val chip = com.google.android.material.chip.Chip(requireContext())
        chip.text = topicName
        chip.isClickable = false
        chip.isCheckable = false
        chip.setChipBackgroundColorResource(android.R.color.transparent)
        chip.setChipStrokeColorResource(com.google.android.material.R.color.material_on_surface_stroke)
        chip.chipStrokeWidth = 1f // толщина рамки (нужно конвертировать в пиксели, но для теста сойдет)

        // Добавляем в ChipGroup
        binding.chipGroupInterests.addView(chip)
    }

}