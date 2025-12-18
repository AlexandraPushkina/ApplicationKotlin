package com.example.homework6

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.homework6.databinding.FragmentCreatePostBinding
import com.example.homework6.viewmodels.AppViewModelFactory
import com.example.homework6.viewmodels.CreatePostViewModel

class CreatePostFragment : Fragment() {

    private var _binding: FragmentCreatePostBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CreatePostViewModel by viewModels {
        AppViewModelFactory(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. ПОДПИСЫВАЕМСЯ НА УСПЕХ
        // Как только ViewModel скажет "Готово", покажем тост и закроем экран
        viewModel.postCreatedSuccess.observe(viewLifecycleOwner) {
            Toast.makeText(context, "Пост создан!", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }

        // 2. ПОДПИСЫВАЕМСЯ НА ОШИБКИ, на случай падение базы или других ошибок
        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
        }

        binding.btnCreatePost.setOnClickListener {
            validateAndSave()
        }
    }

    private fun validateAndSave() {
        val title = binding.etTitle.text.toString().trim()
        val content = binding.etContent.text.toString().trim()
        val imageUrl = binding.etImageUrl.text.toString().trim()

        // Валидация: Заголовок обязателен
        if (title.isBlank()) {
            binding.tilTitle.error = "Введите заголовок"
            return
        } else {
            binding.tilTitle.error = null // Убираем ошибку если все ок
        }

        // Подготовка данных
        // Если поле картинки пустое -> записываем null
        val finalImageUrl = imageUrl.ifBlank { null }

        val sharedPref = requireActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val username = sharedPref.getString("current_user_name", null)

        if (username != null) {
            // ОТПРАВЛЯЕМ ДАННЫЕ ВО VIEWMODEL
            viewModel.createPost(title, content, finalImageUrl, username)
        } else {
            Toast.makeText(context, "Ошибка: вы не авторизованы", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}