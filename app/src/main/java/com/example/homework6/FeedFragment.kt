package com.example.homework6

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.homework6.data.AppDatabase
import com.example.homework6.databinding.FragmentFeedBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.navigation.fragment.findNavController

class FeedFragment : Fragment() {

    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        binding.fabProfile.setOnClickListener {
            findNavController().navigate(R.id.profileFragment)
        }
    }

    private fun setupRecyclerView() {
        // настройка сетки (2 колонки)
        binding.recyclerViewFeed.layoutManager = GridLayoutManager(context, 2)

        val db = AppDatabase.getDatabase(requireContext())
        val sharedPref = requireActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val username = sharedPref.getString("current_user_name", null)!!

        // Загружаем данные в корутине
        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            // Берем ВСЕ посты из базы
            val allPosts = db.postDao().getAllPosts()
            val userInterests = db.userDao().getUser(username)!!.topicId
                .split(",")
                .map { it.trim().toInt() }

            // ФИЛЬТРАЦИЯ
            // Оставляем только те посты, где хотя бы одна тема совпадает с интересами

            val filteredPosts = allPosts.filter { post ->
                val postTopicIds = post.topicIds
                    .split(",")       // Разбиваем по запятой
                    .map { it.trim().toInt() } // Убираем пробелы и превращаем в Int

                // Проверяем: есть ли пересечение между темами поста и интересами юзера
                // any возвращает true, если хотя бы один элемент совпадает
                postTopicIds.any { it in userInterests }
            }

            // 4. Обновляем UI
            withContext(Dispatchers.Main) {
                // Создаем адаптер уже с отфильтрованным списком
                val adapter = PostsAdapter(filteredPosts)
                binding.recyclerViewFeed.adapter = adapter
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Очищаем ссылку во избежание утечек памяти
    }
}