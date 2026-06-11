package com.example.homework6

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.homework6.data.AppDatabase
import com.example.homework6.data.entities.PostEntity
import com.example.homework6.databinding.DialogPostDetailBinding
import com.example.homework6.viewmodels.FeedViewModel
import com.example.homework6.viewmodels.FeedViewModelFactory
import com.google.android.material.chip.Chip

class DialogPostDetailFragment : DialogFragment() {

    // Настройка ViewBinding
    private var _binding: DialogPostDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FeedViewModel by viewModels {
        val db = AppDatabase.getDatabase(requireContext())
        val repository = PostRepository(db)
        val rankingUseCase = FeedRankingUseCase(repository)

        FeedViewModelFactory(db.userDao(), rankingUseCase, repository)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Делаем диалог полноэкранным
        setStyle(STYLE_NORMAL, R.style.Theme_Homework6)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Связываем класс с XML файлом dialog_post_detail
        _binding = DialogPostDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Получаем пост из аргументов
        // getParcelable может вернуть null, поэтому используем ?.let
        val post = arguments?.getParcelable<PostEntity>("POST_DATA")

        post?.let { item ->
            // Заполняем данные
            binding.tvAuthorName.text = item.authorName
            binding.tvPostTitle.text = item.title
            binding.tvPostContent.text = item.content // Здесь полный текст

            // Загрузка картинки
            if (item.imageUrl != null) {
                Glide.with(this)
                    .load(item.imageUrl)
                    .into(binding.imgPostDetail)
            }
            viewModel.getTopicsForPost(item.id).observe(viewLifecycleOwner) { tags ->
                binding.chipGroupTags.removeAllViews()
                tags.forEach { tag ->
                    val chip = Chip(requireContext()).apply {
                        text = tag.name
                        isClickable = false
                        isCheckable = false
                    }
                    binding.chipGroupTags.addView(chip)
                }
            }
        }

        // 2. Настраиваем кнопку закрытия
        binding.toolbar.setNavigationOnClickListener {
            dismiss() // Закрывает диалог
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        // Метод для создания диалога и передачи в него поста
        fun newInstance(post: PostEntity): DialogPostDetailFragment {
            val fragment = DialogPostDetailFragment()
            val args = Bundle()
            // Кладем пост в "конверт" (Bundle) под ключом "POST_DATA"
            args.putParcelable("POST_DATA", post)
            fragment.arguments = args
            return fragment
        }
    }
}