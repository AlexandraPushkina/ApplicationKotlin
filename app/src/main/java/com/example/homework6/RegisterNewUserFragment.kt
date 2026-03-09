package com.example.homework6

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.core.content.edit
import androidx.fragment.app.viewModels
import com.example.homework6.databinding.FragmentNewUserRegisterBinding
import com.example.homework6.viewmodels.AppViewModelFactory
import com.example.homework6.viewmodels.RegisterUserViewModel
import com.example.homework6.extensions.showErrorMessage
import com.example.homework6.extensions.showMessage

class RegisterNewUserFragment : Fragment(R.layout.fragment_new_user_register) {

    private var _binding: FragmentNewUserRegisterBinding? = null
    private val binding get() = _binding!!

    // Подключаем ViewModel
    private val viewModel: RegisterUserViewModel by viewModels {
        AppViewModelFactory(requireActivity().application)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Привязываем верстку
        _binding = FragmentNewUserRegisterBinding.bind(view)

        // 1. ПРЕДЗАПОЛНЕНИЕ ПОЛЕЙ (Если мы передали email и пароль из другого экрана)
        val passedEmail = arguments?.getString("user_email")
        val passedPassword = arguments?.getString("user_password")

        if (!passedEmail.isNullOrBlank()) {
            binding.tvUserEmail.setText(passedEmail)
        }
        if (!passedPassword.isNullOrBlank()) {
            binding.tvUserPassword.setText(passedPassword)
        }

        // 2. Ждем клика
        binding.btnRegister.setOnClickListener {
            // Вызываем функцию проверки и сохранения
            attemptRegistration()
        }
    }

    private fun attemptRegistration() {
        // 1. Считываем данные из полей ввода
        val userEmail = binding.tvUserEmail.text.toString().trim()
        val password = binding.tvUserPassword.text.toString().trim()
        val userName = binding.tvUserName.text.toString().trim()
        val userBio = binding.etBio.text.toString().trim()

        // 2. Валидация
        // Допустим, AuthValidator возвращает строку с ошибкой или null, если все отлично
        val emailError = AuthValidator.validateEmail(userEmail)
        val passwordError = AuthValidator.validatePassword(password)

        // Добавим простую проверку для имени (не должно быть пустым)
        val nameError = if (userName.isBlank()) "Введите имя" else null

        // 3. Отображаем ошибки (если они есть)
        binding.tvUserEmail.error = emailError
        binding.tvUserPassword.error = passwordError
        binding.tvUserName.error = nameError

        // 4. Если ошибок нет (все переменные == null), отправляем во ViewModel
        if (emailError == null && passwordError == null && nameError == null) {

            val selectedChipIds = binding.chipGroupTopics.checkedChipIds

            if (selectedChipIds.isEmpty()) {
                showErrorMessage("Пожалуйста, выберите хотя бы одну тему!")
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

            viewModel.registerUser(
                useremail = userEmail,
                password = password,
                username = userName,
                bio = userBio,
                topicIds = dbTopicIdsList
            )

            navigateToMainScreen()

        } else {
            showErrorMessage("Данные неккоректны")
        }
    }
    private fun navigateToMainScreen() {
        val intent = Intent(requireContext(), MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra(MainActivity.EXTRA_USER_ID, id_нового_пользователя) //Передаем id в MainActivity
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

