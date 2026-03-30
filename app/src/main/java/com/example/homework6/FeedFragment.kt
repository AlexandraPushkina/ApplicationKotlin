package com.example.homework6

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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
import com.example.homework6.extensions.EXTRA_USER_ID
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

        // Пытаемся достать userId, который передавали в MainActivity
        val userId = requireActivity().intent.getIntExtra(EXTRA_USER_ID, -1)

        // Если юзера нет, то выходим на экрна регистрации
        if (userId == -1) {
            kickUserOut("Ошибка сессии. Пожалуйста, войдите снова.")
            return
        }

        // 3. Подписываемся на список постов
        viewModel.posts.observe(viewLifecycleOwner) { loadedPosts ->
            // Когда посты загрузятся, создаем адаптер и показываем их
            val adapter = PostsAdapter(loadedPosts.toMutableList()) { selectedPost ->
                val dialog = DialogPostDetailFragment.newInstance(selectedPost)
                dialog.show(parentFragmentManager, "PostDetail")
            }
            binding.recyclerViewFeed.adapter = adapter
        }

        // 4. Подписываемся на данные пользователя из БД
        viewModel.currentUser.observe(viewLifecycleOwner) { user ->
            if (user == null) {
                // Если база данных вернула пустоту (например, юзера удалили из БД)
                kickUserOut("Пользователь не найден в базе данных.")
            } else {
                viewModel.loadPosts(user.username)
            }
        }
    }

    private fun kickUserOut(message: String) {
        // Показываем Toast
        Toast.makeText(requireContext(),
            message,
            Toast.LENGTH_LONG).show()

        // Создаем Intent для перехода на экран Входа/Регистрации (замените AuthActivity на вашу)
        val intent = Intent(requireContext(), AuthValidator::class.java)

        // Очищаем историю (чтобы кнопка "Назад" не вернула обратно в Feed)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
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