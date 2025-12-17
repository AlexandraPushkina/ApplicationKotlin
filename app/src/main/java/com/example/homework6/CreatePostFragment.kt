package com.example.homework6

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.homework6.data.AppDatabase
import com.example.homework6.data.entities.PostEntity
import com.example.homework6.databinding.FragmentCreatePostBinding
import kotlinx.coroutines.launch

class CreatePostFragment : Fragment() {

    private var _binding: FragmentCreatePostBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCreatePost.setOnClickListener {
            savePost()
        }
    }

    private fun savePost() {
        val title = binding.etTitle.text.toString().trim()
        val content = binding.etContent.text.toString().trim()
        val imageUrl = binding.etImageUrl.text.toString().trim()
        val db = AppDatabase.getDatabase(requireContext())

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

        val newPost = PostEntity(
            id = 0,
            userId = 1,
            authorName = "Я (User)", // Автоматическое имя
            title = title,
            content = content,
            imageUrl = finalImageUrl,
            topicIds = "5" // TODO: правильно указывать темы
        )

        lifecycleScope.launch {
            try {
                db.postDao().insert(newPost)
                Toast.makeText(context, "Пост создан!", Toast.LENGTH_SHORT).show()

                // Возвращаемся назад к списку постов
                findNavController().popBackStack()

            } catch (e: Exception) {
                Toast.makeText(context, "Ошибка сохранения: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}