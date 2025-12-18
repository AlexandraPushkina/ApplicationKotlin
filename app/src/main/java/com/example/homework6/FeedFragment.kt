package com.example.homework6

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.homework6.data.AppDatabase
import com.example.homework6.databinding.FragmentFeedBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.navigation.fragment.findNavController
import com.example.homework6.viewmodels.AppViewModelFactory
import com.example.homework6.viewmodels.FeedViewModel

class FeedFragment : Fragment() {

    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!
    // viewModel - связывает этот фрагмент и бд
    private val viewModel: FeedViewModel by viewModels {
        AppViewModelFactory(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFeedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView() // Настройка LayoutManager

        // Получаем имя пользователя
        val sharedPref = requireActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val username = sharedPref.getString("current_user_name", null)!!

        // ПОДПИСЫВАЕМСЯ на обновления (Observer)
        viewModel.posts.observe(viewLifecycleOwner) { loadedPosts ->
            // Создаем адаптер с уже готовыми данными, которые прислала ViewModel
            val adapter = PostsAdapter(loadedPosts.toMutableList()) { selectedPost ->
                val dialog = DialogPostDetailFragment.newInstance(selectedPost)
                dialog.show(parentFragmentManager, "PostDetail")
            }
            binding.recyclerViewFeed.adapter = adapter
        }

        // ДАЕМ КОМАНДУ загрузить данные
        viewModel.loadPosts(username)

        binding.fabProfile.setOnClickListener {
            // переносит через navigate на Создание поста
            findNavController().navigate(R.id.createPostFragment)
        }
    }

    private fun setupRecyclerView() {
        // настройка сетки (2 колонки)
        binding.recyclerViewFeed.layoutManager = GridLayoutManager(context, 2)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Очищаем ссылку во избежание утечек памяти
    }
}