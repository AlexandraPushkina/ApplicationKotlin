package com.example.homework6

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.homework6.data.entities.TopicEntity
import com.example.homework6.databinding.FragmentCreatePostBinding
import com.example.homework6.viewmodels.AppViewModelFactory
import com.example.homework6.viewmodels.CreatePostViewModel
import com.google.android.material.chip.Chip

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

        // Загрузка существующих тем постов
        viewModel.loadTopics()

        // Наблюдение за списком тем и создание чипов
        viewModel.allTopics.observe(viewLifecycleOwner) { topics ->
            Log.d("DEBUG_UI", "Загружено тем: ${topics.size}")
            setupTopicChips(topics)
        }

        viewModel.postCreatedSuccess.observe(viewLifecycleOwner) {
            Toast.makeText(context, "Пост создан!", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }

        // Обработка ошибок
        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
        }
        // Кнопка создания
        binding.btnCreatePost.setOnClickListener {
            validateAndSave()
        }
    }

    private fun setupTopicChips(topics: List<TopicEntity>) {
        val chipGroup = binding.chipGroupTopics
        chipGroup.removeAllViews()

        Log.d("DEBUG_UI", "setupTopicChips: topics.size=${topics.size}")

        if (topics.isEmpty()) {
            Log.d("DEBUG_UI", "Темы не найдены для отображения")
            return
        }

        topics.forEach { topic ->
            val chip = Chip(requireContext()).apply {
                text = topic.name
                isCheckable = true
                id = View.generateViewId() // уникальный ID чипа
                tag = topic.id             // сохранение id темы для последующего получения
            }
            chipGroup.addView(chip)
        }
        Log.d("DEBUG_UI", "setupTopicChips: чипы добавлены: ${chipGroup.childCount}")
    }

    private fun getSelectedTopicIds(): List<Int> {
        return binding.chipGroupTopics.children
            .filterIsInstance<Chip>()
            .filter { it.isChecked }
            .mapNotNull { it.tag as? Int }
            .toList()
    }

    private fun validateAndSave() {
        val title = binding.etTitle.text.toString().trim()
        val content = binding.etContent.text.toString().trim()
        val imageUrl = binding.etImageUrl.text.toString().trim()
        val username = viewModel.getUsername()

        val validTopicIds = getSelectedTopicIds()
        val checkedChipsCount = binding.chipGroupTopics.checkedChipIds.size

        if (title.isBlank() || content.isEmpty()) {
            Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
            return
        } else {
            binding.tilTitle.error = null
        }

        if (checkedChipsCount == 0) {
            Toast.makeText(context, "Выберите хотя бы одну тему", Toast.LENGTH_SHORT).show()
            return
        }

        if (checkedChipsCount != validTopicIds.size) {
            Toast.makeText(
                requireContext(),
                "Системная ошибка: выбраны несуществующие теги. Пожалуйста, обратитесь к разработчикам.",
                Toast.LENGTH_LONG
            ).show()
            return
        }

        if (username != null) {
            val finalImageUrl = imageUrl.ifBlank { null }

            val finalTopicIdsLong = validTopicIds.map { it.toLong() }
            viewModel.createPost(title, content, finalImageUrl, username, finalTopicIdsLong)
        } else {
            Toast.makeText(context, "Ошибка: вы не авторизованы", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}