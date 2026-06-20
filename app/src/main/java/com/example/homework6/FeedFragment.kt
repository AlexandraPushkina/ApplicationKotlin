package com.example.homework6

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

        // 1. Создаем адаптер
        val postsAdapter = PostsAdapter(mutableListOf()) { selectedPost ->
            val dialog = DialogPostDetailFragment.newInstance(selectedPost)
            dialog.show(parentFragmentManager, "PostDetail")
        }
        binding.recyclerViewFeed.adapter = postsAdapter

        // 2. Настраиваем SearchView
        binding.searchView.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = true
            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.setSearchQuery(newText ?: "")
                return true
            }
        })

        // 3. ЕДИНСТВЕННЫЙ наблюдатель для списка постов
        // Сюда будут приходить данные и от поиска, и при обычном старте
        viewModel.posts.observe(viewLifecycleOwner) { loadedPosts ->
            Log.d("MyFeedDebug", "Посты пришли в фрагмент: ${loadedPosts.size}")
            postsAdapter.updatePosts(loadedPosts)
        }

        // 4. Проверка пользователя
        val userId = requireActivity().intent.getIntExtra(EXTRA_USER_ID, -1)
        if (userId == -1) {
            kickUserOut("Ошибка сессии.")
            return
        }

        viewModel.currentUser.observe(viewLifecycleOwner) { user ->
            if (user == null) {
                kickUserOut("Пользователь не найден.")
            } else {
                Log.d("MyFeedDebug", "Пользователь $userId авторизован.")
            }
        }
        viewModel.getUser(userId)
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