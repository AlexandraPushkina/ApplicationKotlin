package com.example.homework6

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.example.homework6.data.entities.PostEntity
import com.example.homework6.databinding.ItemPostBinding // Проверь пакет!

class PostsAdapter(private val posts: List<PostEntity>) :
    RecyclerView.Adapter<PostsAdapter.PostViewHolder>() {

    class PostViewHolder(val binding: ItemPostBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {

        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = posts[position]

        with(holder.binding) {
            tvAuthorName.text = post.authorName
            tvPostTitle.text = post.title

            // Если ссылка есть - скачает, если null - не скачает
            if (post.imageUrl != null) {
                Glide.with(root.context)
                    .load(post.imageUrl)
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher_background) // Пока грузится
                    .error(R.drawable.ic_launcher_foreground)       // Если ошибка
                    .into(ivPostImage)
            } else {
                // Если картинки у поста нет = ставим стандартную
                ivPostImage.setImageResource(R.drawable.ic_launcher_foreground)
            }
        }
    }

    override fun getItemCount(): Int = posts.size

}