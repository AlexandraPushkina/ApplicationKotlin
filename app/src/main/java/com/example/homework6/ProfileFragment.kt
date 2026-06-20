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
import com.example.homework6.databinding.FragmentProfileBinding
import com.example.homework6.extensions.EXTRA_USER_ID
import com.example.homework6.viewmodels.AppViewModelFactory
import com.example.homework6.viewmodels.ProfileViewModel
import androidx.recyclerview.widget.GridLayoutManager

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var postAdapter: PostAdapter

    // Подключаем ViewModel
    private val viewModel: ProfileViewModel by viewModels {
        AppViewModelFactory(requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. ПОДПИСЫВАЕМСЯ на данные пользователя (Имя, Био)
        viewModel.userProfile.observe(viewLifecycleOwner) { user ->
            binding.tvUsername.text = user.username
            binding.tvBio.text = user.bio?.ifEmpty { "Биография не заполнена" }
        }

        // 2. ПОДПИСЫВАЕМСЯ на список тем (Чипы)
        viewModel.topicNames.observe(viewLifecycleOwner) { topicNames ->
            binding.chipGroupInterests.removeAllViews() // Очищаем старые
            for (name in topicNames) {
                addChipToGroup(name)
            }
        }

        // 3. ПОЛУЧАЕМ ID И ЗАПУСКАЕМ ЗАГРУЗКУ
        val userId = requireActivity().intent.getIntExtra(EXTRA_USER_ID, -1)

        if (userId != -1) {
            viewModel.loadProfile(userId)
        } else {
            Toast.makeText(context, "Ошибка: пользователь не найден", Toast.LENGTH_SHORT).show()
        }

        // 4. НАСТРОЙКА КНОПКИ ВЫХОДА
        binding.toolbarProfile.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_logout -> {
                    performLogout()
                    true
                }
                else -> false
            }
        }

        postAdapter = PostAdapter(
            posts = mutableListOf(),
            onPostClick = {clickedPost ->
                val dialog = DialogPostDetailFragment()
                val bundle = Bundle()
                bundle.putParcelable("POST_DATA", clickedPost)
                dialog.arguments = bundle

                dialog.show(parentFragmentManager, "PostDetailDialog")
            }
        )

        binding.recyclerViewLikedPosts.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = postAdapter
            isNestedScrollingEnabled = false
        }

        // 6. ПОДПИСКА НА ПОНРАВИВШИЕСЯ ПОСТЫ
        viewModel.likedPosts.observe(viewLifecycleOwner) { posts ->
            postAdapter.updatePosts(posts) // В прошлом сообщении мы назвали метод updateData
            binding.tvLikedPostsLabel.visibility = if (posts.isEmpty()) View.GONE else View.VISIBLE
        }
    }

    private fun performLogout() {
        // 1. ОЧИСТКА ДАННЫХ ПОЛЬЗОВАТЕЛЯ
        val sharedPref = requireActivity().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        sharedPref.edit().clear().apply()

        // 2. ПЕРЕХОД НА ЭКРАН ЛОГИНА
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
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