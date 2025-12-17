package com.example.homework6

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.example.homework6.data.entities.PostEntity
import com.example.homework6.databinding.ItemPostBinding // Проверь пакет!

class PostsAdapter(

    // Функция, которая принимает пост (PostEntity) и ничего не возвращает (Unit)"
    private val posts: List<PostEntity>,
    private val onPostClick: (PostEntity) -> Unit
) : RecyclerView.Adapter<PostsAdapter.PostViewHolder>() {

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

            // Если есть ссылка, скачает, если null - то картинка по умолчанию
            if (post.imageUrl != null) {
                Glide.with(root.context)
                    .load(post.imageUrl)
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_foreground)
                    .into(ivPostImage)
            } else {
                ivPostImage.setImageResource(R.drawable.ic_launcher_foreground)
            }

            // Обработка клика
            root.setOnClickListener {
                // Вызываем лямбду, передавая туда текущий пост
                onPostClick(post)
            }
        }
    }
    override fun getItemCount(): Int = posts.size

    // перемещение
    fun onItemMove(fromPosition: Int, toPosition: Int) {
        // Если from < to, значит тянем вниз, иначе вверх
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                java.util.Collections.swap(posts, i, i + 1)
            }
        } else {
            for (i in fromPosition downTo toPosition + 1) {
                java.util.Collections.swap(posts, i, i - 1)
            }
        }

        // Сообщаем адаптеру, что элемент визуально переместился
        notifyItemMoved(fromPosition, toPosition)
    }  // конец перемещение
}