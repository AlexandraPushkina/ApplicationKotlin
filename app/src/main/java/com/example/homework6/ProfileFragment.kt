package com.example.homework6

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.homework6.R
import com.example.homework6.databinding.FragmentProfileBinding
import com.example.homework6.viewmodels.AppViewModelFactory
import com.example.homework6.viewmodels.ProfileViewModel

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    // Подключаем ViewModel
    private val viewModel: ProfileViewModel by viewModels {
        AppViewModelFactory(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. ПОДПИСЫВАЕМСЯ на данные пользователя (Имя, Био)
        viewModel.userProfile.observe(viewLifecycleOwner) { user ->
            binding.tvUsername.text = user.username
            binding.tvBio.text = user.bio.ifEmpty { "Биография не заполнена" }
        }

        // 2. ПОДПИСЫВАЕМСЯ на список тем (Чипы)
        viewModel.topicNames.observe(viewLifecycleOwner) { topicNames ->
            // Очищаем старые, чтобы не дублировались
            binding.chipGroupInterests.removeAllViews()

            // Создаем чипы для каждого названия
            for (name in topicNames) {
                addChipToGroup(name)
            }
        }

        // 3. ЗАПУСКАЕМ ЗАГРУЗКУ
        val sharedPref = requireActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val username = sharedPref.getString("current_user_name", null)

        if (username != null) {
            viewModel.loadProfile(username)
        } else {
            Toast.makeText(context, "Ошибка: пользователь не найден", Toast.LENGTH_SHORT).show()
        }
    }

    // Вспомогательная функция для красивого создания чипов (UI only)
    private fun addChipToGroup(topicName: String) {
        val chip = com.google.android.material.chip.Chip(requireContext())
        chip.text = topicName
        chip.isClickable = false
        chip.isCheckable = false
        // Визуальные настройки
        chip.setChipBackgroundColorResource(android.R.color.transparent)
        chip.setChipStrokeColorResource(android.R.color.holo_purple)
        chip.chipStrokeWidth = 3f // Размер чипа

        binding.chipGroupInterests.addView(chip)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}