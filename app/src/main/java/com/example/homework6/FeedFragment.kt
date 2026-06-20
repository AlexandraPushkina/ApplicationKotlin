package com.example.homework6

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.homework6.databinding.FragmentFeedBinding
import com.example.homework6.extensions.EXTRA_USER_ID
import com.example.homework6.viewmodels.AppViewModelFactory
import com.example.homework6.viewmodels.FeedViewModel

class FeedFragment : Fragment() {

    private var _binding: FragmentFeedBinding? = null
    private val binding get() = _binding!!

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
        setupRecyclerView()

        val postsAdapter = PostAdapter(mutableListOf()) { selectedPost ->
            val dialog = DialogPostDetailFragment.newInstance(selectedPost)
            dialog.show(parentFragmentManager, "PostDetail")
        }
        binding.recyclerViewFeed.adapter = postsAdapter

        // 1. Настраиваем SearchView
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = true
            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrBlank()) {
                    // Если стерли текст, возвращаем на экран нашу отранжированную ленту
                    viewModel.feedPosts.value?.let { postsAdapter.updatePosts(it) }
                }
                viewModel.setSearchQuery(newText ?: "")
                return true
            }
        })

        // 2. Свайп для обновления
        binding.swipeRefreshLayout.setOnRefreshListener {
            val sharedPref = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
            val currentEmail = sharedPref.getString("current_user_email", null)

            if (currentEmail != null) {
                viewModel.getUser(email = currentEmail)
            } else {
                binding.swipeRefreshLayout.isRefreshing = false
                Toast.makeText(context, "Вы не авторизованы", Toast.LENGTH_SHORT).show()
            }
        }

        // 3. Наблюдаем за пользователем
        viewModel.currentUser.observe(viewLifecycleOwner) { user ->
            if (user != null) {
                Log.d("MyFeedDebug", "Пользователь ${user.id} авторизован.")
                viewModel.loadPosts(user.id) // Запускаем ранжирование
            } else {
                binding.swipeRefreshLayout.isRefreshing = false
                kickUserOut("Пользователь не найден.")
            }
        }

        // 4. Наблюдаем за feedPosts
        viewModel.feedPosts.observe(viewLifecycleOwner) { loadedPosts ->
            Log.d("DEBUG_FEED", "Отранжированные посты: ${loadedPosts.size}")
            // Обновляем список, если сейчас мы не ищем текст
            if (binding.searchView.query.isNullOrBlank()) {
                postsAdapter.updatePosts(loadedPosts)
            }
            binding.swipeRefreshLayout.isRefreshing = false
        }

        // 5. Наблюдаем за searchResults
        viewModel.searchResults.observe(viewLifecycleOwner) { searchedPosts ->
            postsAdapter.updatePosts(searchedPosts)
        }

        // 6. Первоначальный запуск
        val userId = requireActivity().intent.getIntExtra(EXTRA_USER_ID, -1)
        if (userId != -1) {
            viewModel.getUser(userId)
        } else {
            kickUserOut("Ошибка сессии. Пользователь не найден")
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerViewFeed.layoutManager = GridLayoutManager(context, 2)
    }

    private fun kickUserOut(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
        val intent = Intent(requireContext(), AuthValidator::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}