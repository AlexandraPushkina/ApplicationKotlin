package com.example.homework6

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.homework6.data.AppDatabase
import com.example.homework6.data.entities.CommentEntity
import com.example.homework6.data.entities.HiddenPostEntity
import com.example.homework6.data.entities.LikeEntity
import com.example.homework6.data.entities.PostEntity
import com.example.homework6.data.entities.PostTopicCrossRef
import com.example.homework6.data.entities.PostWithTopics
import com.example.homework6.data.entities.TopicEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking

// Вызывает команды, предоставляющие данные с бд.
class PostRepository(private val db: AppDatabase) {

    // Получение постов вместе с темами из БД
    suspend fun getAllPostsWithTopics(): List<PostWithTopics> {
        return db.postDao().getAllPosts()
    }

    // В PostRepository
    fun getAllPostsFlow(): Flow<List<PostEntity>> {
        return db.postDao().getAllPostsFlow()
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

    fun isLiked(email: String, postId: Int): LiveData<Boolean> {
        val user = runBlocking { getUserByEmail(email) } ?: return MutableLiveData(false)
        return db.InteractionDao().isLiked(user.id, postId)
    }
    suspend fun toggleLike(email: String, postId: Int) {
        val user = getUserByEmail(email) ?: return
        val userId = user.id
        val alreadyLiked = db.InteractionDao().isLikedSync(userId, postId)
        if (alreadyLiked > 0) {
            db.InteractionDao().deleteLike(userId, postId)
        } else {
            db.InteractionDao().insertLike(LikeEntity(userId = userId, postId = postId))
        }
    }

    suspend fun hidePost(email: String, postId: Int) {
        val user = getUserByEmail(email) ?: return
        val userId = user.id
        db.InteractionDao().hidePost(HiddenPostEntity(userId = userId, postId = postId))
        val tags = db.postDao().getTopicsForPostSync(postId)
        tags.forEach { tag ->
            db.UserInterestsDao().updateInterestWeight(user.id, tag.id, -5)
        }
    }
    fun getComments(postId: Int): LiveData<List<CommentEntity>> {
        return db.InteractionDao().getCommentsForPost(postId)
    }

    fun searchPosts(text: String): Flow<List<PostEntity>> {
        return db.postDao().searchPosts(text)
    }

    suspend fun addComment(email: String, postId: Int, content: String) {
        val user = db.userDao().getUserByEmail(email)?: return
        val comment = CommentEntity(
            postId = postId,
            userId = user.id,
            authorName = user.username,
            content = content
        )
        db.InteractionDao().insertComment(comment)
    }
}

