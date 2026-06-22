package com.example.homework6

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.homework6.data.entities.TopicEntity
import com.example.homework6.databinding.FragmentNewUserRegisterBinding
import com.example.homework6.extensions.EXTRA_USER_ID
import com.example.homework6.extensions.getNameWithEmoji
import com.example.homework6.viewmodels.AppViewModelFactory
import com.example.homework6.viewmodels.RegisterUserViewModel
import com.example.homework6.extensions.showErrorMessage
import com.google.android.material.chip.Chip

class RegisterNewUserFragment : Fragment(R.layout.fragment_new_user_register) {

    private var _binding: FragmentNewUserRegisterBinding? = null
    private val binding get() = _binding!!

    // Подключение ViewModel
    private val viewModel: RegisterUserViewModel by viewModels {
        AppViewModelFactory(requireActivity().application)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentNewUserRegisterBinding.bind(view)

        setupInitialData()
        setupObservers()
        setupListeners()
    }

    private fun setupInitialData() {
        val passedEmail = arguments?.getString("user_email")
        val passedPassword = arguments?.getString("user_password")

        if (!passedEmail.isNullOrBlank()) {
            binding.tvUserEmail.setText(passedEmail)
        }
        if (!passedPassword.isNullOrBlank()) {
            binding.tvUserPassword.setText(passedPassword)
        }
    }

    private fun setupListeners() {
        binding.btnRegister.setOnClickListener {
            attemptRegistration()
        }
    }

    private fun setupObservers() {
        // 1. Реактивный список топиков
        viewModel.allTopics.observe(viewLifecycleOwner) { topics ->
            if (topics.isNotEmpty()) {
                Log.d("DEBUG_UI", "Получено топиков для чипов: ${topics.size}")
                createChips(topics)
            }
        }

        // 2. Наблюдаем за успешной регистрацией
        viewModel.registrationSuccess.observe(viewLifecycleOwner) { newUserId ->
            navigateToMainScreen(newUserId)
        }

        // 3. Наблюдаем за ошибками из ViewModel
        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            showErrorMessage(errorMessage)
        }
    }

    private fun createChips(topics: List<TopicEntity>) {
        binding.chipGroupTopics.removeAllViews()
        val inflater = LayoutInflater.from(requireContext())

        for (topic in topics) {
            val chip = inflater.inflate(R.layout.item_topic_chip, binding.chipGroupTopics, false) as Chip
            chip.text = topic.getNameWithEmoji()
            chip.id = View.generateViewId()
            chip.tag = topic.id
            binding.chipGroupTopics.addView(chip)
        }
    }

    private fun attemptRegistration() {
        val userEmail = binding.tvUserEmail.text.toString().trim()
        val password = binding.tvUserPassword.text.toString().trim()
        val userName = binding.tvUserName.text.toString().trim()
        val userBio = binding.etBio.text.toString().trim()

        val emailError = AuthValidator.validateEmail(userEmail)
        val passwordError = AuthValidator.validatePassword(password)
        val nameError = if (userName.isBlank()) "Введите имя" else null

        binding.tvUserEmail.error = emailError
        binding.tvUserPassword.error = passwordError
        binding.tvUserName.error = nameError

        if (emailError == null && passwordError == null && nameError == null) {

            val selectedChipIds = binding.chipGroupTopics.checkedChipIds

            if (selectedChipIds.isEmpty()) {
                showErrorMessage("Пожалуйста, выберите хотя бы одну тему!")
                return
            }

            // Магия сбора данных с программных чипов
            val dbTopicIdsList: List<Int> = selectedChipIds.mapNotNull { chipId ->
                val chip = binding.chipGroupTopics.findViewById<Chip>(chipId)
                chip?.tag as? Int
            }

            Log.d("DEBUG_DB", "Преобразованные в DB IDs: $dbTopicIdsList")

            viewModel.registerUser(
                useremail = userEmail,
                password = password,
                username = userName,
                bio = userBio,
                topicIds = dbTopicIdsList
            )
        } else {
            showErrorMessage("Данные некорректны")
        }
    }

    private fun navigateToMainScreen(userId: Int) {
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(EXTRA_USER_ID, userId)
        startActivity(intent)
    }

    private fun showErrorMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

