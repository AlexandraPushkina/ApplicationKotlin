package com.example.homework6

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.homework6.databinding.FragmentNewUserIntroduceBinding
import androidx.core.content.edit
import androidx.fragment.app.viewModels
import com.example.homework6.viewmodels.AppViewModelFactory
import com.example.homework6.viewmodels.NewUserIntroduceViewModel

class NewUserIntroduceFragment : Fragment(R.layout.fragment_new_user_introduce) {

    private lateinit var binding: FragmentNewUserIntroduceBinding

    // Подключаем ViewModel
    private val viewModel: NewUserIntroduceViewModel by viewModels {
        AppViewModelFactory(requireActivity().application)
    }

    private var username: String? = null
    private var password: String? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNewUserIntroduceBinding.bind(view)

        // 1. Достаем аргументы
        username = arguments?.getString("user_name")
        password = arguments?.getString("user_password")

        binding.tvUsername.text = username ?: "Пользователь"

        // 2. ПОДПИСЫВАЕМСЯ на успех
        viewModel.registrationSuccess.observe(viewLifecycleOwner) {
            // Сохраняем сессию в SharedPreferences
            val sharedPref = requireActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
            sharedPref.edit { putString("current_user_name", username) }

            Toast.makeText(requireContext(), "Регистрация завершена!", Toast.LENGTH_SHORT).show()

            // Переход на главный экран
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        // 3. ПОДПИСЫВАЕМСЯ на ошибки
        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }

        // 4. Обработка нажатия
        binding.btnSaveBio.setOnClickListener {
            prepareAndSave()
        }
    }

    private fun prepareAndSave() {
        // Проверки на null
        if (username == null || password == null) {
            Toast.makeText(requireContext(), "Ошибка: нет данных пользователя", Toast.LENGTH_SHORT).show()
            return
        }

        val bioText = binding.etBio.text.toString().trim()
        val selectedChipIds = binding.chipGroupTopics.checkedChipIds

        if (selectedChipIds.isEmpty()) {
            Toast.makeText(requireContext(), "Пожалуйста, выберите хотя бы одну тему!", Toast.LENGTH_SHORT).show()
            return
        }

        // Собираем список ID тем (UI -> Data)
        val dbTopicIdsList = mutableListOf<Int>()

        for (chipId in selectedChipIds) {
            val topicId = when (chipId) {
                R.id.chipNature -> 1
                R.id.chipArt -> 2
                R.id.chipCosmetics -> 3
                R.id.chipSport -> 4
                R.id.chipFood -> 5
                else -> null
            }
            if (topicId != null) {
                dbTopicIdsList.add(topicId)
            }
        }

        // ОТПРАВЛЯЕМ ВО VIEWMODEL
        viewModel.registerUser(
            username = username!!,
            password = password!!,
            bio = bioText,
            topicIds = dbTopicIdsList
        )
    }
}