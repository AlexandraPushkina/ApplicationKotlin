package com.example.homework6.data

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.homework6.data.entities.PostTopicEntity

interface PostTopicDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCrossRefs(items: List<PostTopicEntity>)

    @Query("DELETE FROM post_topic WHERE postId = :postId")
    suspend fun deleteTopicsForPost(postId: Long)
}