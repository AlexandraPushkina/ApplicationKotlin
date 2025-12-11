package com.example.homework6.data

import androidx.room.Dao
import androidx.room.Query
import com.example.homework6.data.entities.TopicEntity

@Dao
interface TopicDao {
    // Названия тем
    @Query("SELECT * FROM topics")
    suspend fun getAllTopics(): List<TopicEntity>
}