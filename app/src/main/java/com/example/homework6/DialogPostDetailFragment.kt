package com.example.homework6

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
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
    private var isPostHidden = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Настройка диалога полноэкранным
        setStyle(STYLE_NORMAL, R.style.Theme_Homework6)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Связывание класса с XML файлом dialog_post_detail
        _binding = DialogPostDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Получение поста из аргументов
        val post = arguments?.getParcelable<PostEntity>("POST_DATA")
        val sharedPref = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE)
        val currentUserEmail = sharedPref.getString("current_user_email", null)
        if (currentUserEmail != null) {
            // Слушатель для кнопки "Скрыть"
            binding.btnHide.setOnClickListener {
                isPostHidden = !isPostHidden

                if (isPostHidden) {
                    binding.btnHide.setImageResource(R.drawable.ic_eye_closed)
                    viewModel.hidePost(currentUserEmail, post?.id)
                } else {
                    binding.btnHide.setImageResource(R.drawable.ic_eye_open)
                    // Метод для удаления из скрытых, если нужно
                }
            }

            // Слушатель для Лайка
            binding.btnLike.setOnClickListener {
                viewModel.toggleLike(currentUserEmail, post?.id)
            }

            // Подписка на состояние лайка (чтобы менять иконку)
            viewModel.isLiked(currentUserEmail, post?.id).observe(viewLifecycleOwner) { liked ->
                if (liked) {
                    binding.btnLike.setImageResource(R.drawable.ic_heart_filled)
                    binding.btnLike.setColorFilter(Color.RED)
                } else {
                    binding.btnLike.setImageResource(R.drawable.ic_heart_outline)
                    binding.btnLike.setColorFilter(Color.BLACK)
                }
            }

            // Кнопка комментария
            binding.btnComment.setOnClickListener {
                post?.id?.let { postId ->
                    val commentSheet = CommentBottomSheet(postId, currentUserEmail, viewModel)
                    commentSheet.show(parentFragmentManager, "CommentDialog")
                }
            }

            post?.let { item ->
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
                    viewModel.incrementInterestWeights(currentUserEmail, tags)
                }
            }


            // Настраивание кнопки закрытия
            binding.toolbar.setNavigationOnClickListener {
                dismiss() // Закрывает диалог
            }
        } else
            Log.d("DEBUG_DB", "При попытке просмотра поста пользователь не был найден!")
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
            args.putParcelable("POST_DATA", post)
            fragment.arguments = args
            return fragment
        }
    }
}