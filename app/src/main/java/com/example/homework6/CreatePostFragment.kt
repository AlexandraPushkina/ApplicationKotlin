package com.example.homework6

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.view.ContextThemeWrapper
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

        // Загружаем существующие темы постов
        viewModel.loadTopics()

        // 2. Наблюдаем за списком тем и создаем чипы
        viewModel.allTopics.observe(viewLifecycleOwner) { topics ->
            setupTopicChips(topics)
        }

        // 3. Обработка успеха
        // Как только ViewModel скажет "Готово", покажем тост и закроем экран
        viewModel.postCreatedSuccess.observe(viewLifecycleOwner) {
            Toast.makeText(context, "Пост создан!", Toast.LENGTH_SHORT).show()
            findNavController().popBackStack()
        }

        // 4. Обработка ошибок
        viewModel.error.observe(viewLifecycleOwner) { errorMessage ->
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
        }
        // Кнопка создания
        binding.btnCreatePost.setOnClickListener {
            validateAndSave()
        }
    }

    private fun setupTopicChips(topics: List<TopicEntity>) {
        binding.chipGroupTopics.removeAllViews()

        val inflater = LayoutInflater.from(requireContext())

        topics.forEach { topic ->
            // Используем стандартный стиль фильтра (с галочкой при выборе)
            val chip = inflater.inflate(R.layout.item_topic_chip,
                                        binding.chipGroupTopics,
                                        false) as Chip

            chip.apply {
                text = topic.name
                id = topic.id // ID из базы данных для checkedChipIds

                // Слушатель для ограничения в 10 тем
                setOnCheckedChangeListener { _, isChecked ->
                    val selectedCount = binding.chipGroupTopics.checkedChipIds.size
                    if (isChecked && selectedCount > 10) {
                        this.isChecked = false // Отменяем выбор
                        Toast.makeText(context, "Максимум 10 тем", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            binding.chipGroupTopics.addView(chip)
        }
    }

    private fun validateAndSave() {
        val title = binding.etTitle.text.toString().trim()
        val content = binding.etContent.text.toString().trim()
        val imageUrl = binding.etImageUrl.text.toString().trim()
        val selectedTopicIds: List<Long> = binding.chipGroupTopics.checkedChipIds.map { it.toLong() }

        // Валидация: Заголовок обязателен
        if (title.isBlank() || content.isEmpty()) {
            Toast.makeText(context, "Заполните все поля", Toast.LENGTH_SHORT).show()
            return
        } else {
            binding.tilTitle.error = null // Убираем ошибку если все ок
        }
        if (selectedTopicIds.isEmpty()) {
            Toast.makeText(context, "Выберите хотя бы одну тему", Toast.LENGTH_SHORT).show()
            return
        }
        val sharedPref = requireActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val username = sharedPref.getString("current_user_name", null)

        if (username != null) {

            // Подготовка данных
            // Если поле картинки пустое -> записываем null
            val finalImageUrl = imageUrl.ifBlank { null }
            val selectedTopicIds: List<Long> = binding.chipGroupTopics.checkedChipIds.map { it.toLong() } // Получаем список ID
            // ОТПРАВЛЯЕМ ДАННЫЕ ВО VIEWMODEL
            viewModel.createPost(title, content, finalImageUrl, username, selectedTopicIds)
        } else {
            Toast.makeText(context, "Ошибка: вы не авторизованы", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}