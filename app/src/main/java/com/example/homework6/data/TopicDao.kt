package com.example.homework6.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.homework6.data.entities.TopicEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TopicDao {
    // Названия тем
    @Query("SELECT id FROM topics")
    suspend fun getAllTopicsIds(): List<Int>

    @Query("SELECT * FROM topics")
    suspend fun getAllTopicsList():List<TopicEntity>

    @Query("SELECT * FROM topics")
    fun getAllTopicsFlow(): Flow<List<TopicEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAllTopics(topics: List<TopicEntity>)

    @Query("SELECT COUNT(*) FROM topics")
    suspend fun getTopicCount(): Int

}