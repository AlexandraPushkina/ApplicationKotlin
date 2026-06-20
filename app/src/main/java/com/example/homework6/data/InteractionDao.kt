package com.example.homework6.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.homework6.data.entities.CommentEntity
import com.example.homework6.data.entities.HiddenPostEntity
import com.example.homework6.data.entities.LikeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InteractionDao {
    // Лайки
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLike(like: LikeEntity)

    @Query("DELETE FROM post_likes WHERE userId = :uId AND postId = :pId")
    suspend fun deleteLike(uId: Int, pId: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM post_likes WHERE userId = :uId AND postId = :pId)")
    fun isLiked(uId: Int, pId: Int): LiveData<Boolean>

    @Query("SELECT COUNT(*) FROM post_likes WHERE userId = :uId AND postId = :pId")
    suspend fun isLikedSync(uId: Int, pId: Int): Int

    // Скрытие
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun hidePost(hidden: HiddenPostEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM hidden_posts WHERE userId = :uId AND postId = :pId)")
    fun isHidden(uId: Int, pId: Int): LiveData<Boolean>

    // Комментарии
    @Insert
    suspend fun insertComment(comment: CommentEntity)

    @Query("SELECT * FROM post_comments WHERE postId = :pId ORDER BY timestamp DESC")
    fun getCommentsForPost(pId: Int): LiveData<List<CommentEntity>>
}