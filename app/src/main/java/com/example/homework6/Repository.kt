package com.example.homework6

import androidx.lifecycle.LiveData
import com.example.homework6.data.AppDatabase
import com.example.homework6.data.entities.PostEntity
import com.example.homework6.data.entities.PostTopicCrossRef
import com.example.homework6.data.entities.PostWithTopics
import com.example.homework6.data.entities.TopicEntity

// Вызывает команды, предоставляющие данные с бд.
class PostRepository(private val db: AppDatabase) {

    // Получение постов вместе с темами из БД
    suspend fun getAllPostsWithTopics(): List<PostWithTopics> {
        return db.postDao().getAllPosts()
    }

    suspend fun createPostWithTopics(post: PostEntity, topicIds: List<Long>) {
        val newPostId = db.postDao().insert(post)

        // Создание объектов связи (CrossRef) для таблицы-перемычки
        val crossRefs = topicIds.map { topicId ->
            PostTopicCrossRef(postId = newPostId, topicId = topicId)
        }

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

