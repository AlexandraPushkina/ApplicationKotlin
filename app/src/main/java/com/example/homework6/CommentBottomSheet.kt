package com.example.homework6

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.homework6.databinding.DialogCommentsBinding
import com.example.homework6.viewmodels.FeedViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CommentBottomSheet(
    private val postId: Int,
    private val currentUserEmail: String,
    private val viewModel: FeedViewModel
) : BottomSheetDialogFragment() {

    private lateinit var binding: DialogCommentsBinding // Создай xml с RecyclerView и EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DialogCommentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = CommentAdapter() // адаптер для списка
        binding.rvComments.layoutManager = LinearLayoutManager(requireContext())
        binding.rvComments.adapter = adapter

        // Наблюдаем за комментариями
        viewModel.getComments(postId).observe(viewLifecycleOwner) { comments ->
            adapter.submitList(comments){
                binding.rvComments.scrollToPosition(0)
            }
        }

        // Кнопка отправить
        binding.btnSend.setOnClickListener {
            val text = binding.etComment.text.toString()
            if (text.isNotBlank()) {
                viewModel.sendComment(currentUserEmail, postId, text)
                binding.etComment.text.clear()
            }
        }
    }
}
