package com.example.homework6

import androidx.lifecycle.LiveData
import com.example.homework6.data.AppDatabase
import com.example.homework6.data.PostDao
import com.example.homework6.data.UserDao
import com.example.homework6.data.UserInterestsDao
import com.example.homework6.data.entities.PostEntity
import com.example.homework6.data.entities.PostTopicCrossRef
import com.example.homework6.data.entities.PostWithTopics
import com.example.homework6.data.entities.TopicEntity

// Вызывает команды, предоставляющие данные с бд.
class PostRepository(private val db: AppDatabase) {

    // Получаем посты вместе с темами из БД
    suspend fun getAllPostsWithTopics(): List<PostWithTopics> {
        return db.postDao().getAllPosts()
    }

    // Твой старый метод создания поста теперь живет тут
    suspend fun createPost(post: PostEntity) {
        db.postDao().insert(post)
    }

    suspend fun createPostWithTopics(post: PostEntity, topicIds: List<Long>) {
        // 1. Вставляем пост и получаем его сгенерированный ID
        val newPostId = db.postDao().insert(post)

        // 2. Создаем объекты связи (CrossRef) для таблицы-перемычки
        val crossRefs = topicIds.map { topicId ->
            PostTopicCrossRef(postId = newPostId, topicId = topicId)
        }

        // 3. Сохраняем эти связи в базу
        db.postDao().insertPostTopics(crossRefs)
    }

    suspend fun getAllTopics(): List<TopicEntity> = db.topicDao().getAllTopicsList()

    suspend fun getUserByEmail(email: String) = db.userDao().getUserByEmail(email)

    suspend fun getUserWeights(userId: Int): Map<Int, Int> {
        return db.UserInterestsDao().getUserInterests(userId).associate { it.topicId to it.weight }
    }

    suspend fun incrementInterestWeight(userId: Int, topicId: Int) {
        db.userDao().incrementWeight(userId, topicId)
    }

    fun getTopicsForPost(postId: Int): LiveData<List<TopicEntity>> {
        return db.postDao().getTopicsForPost(postId)
    }
}

