package com.example.homework6.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.homework6.data.entities.UserInterestsEntity

@Dao
interface UserInterestsDao {

    // 1. Добавление сразу списка тем (используется при регистрации)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInterests(interests: List<UserInterestsEntity>)

    // 2. Изменение веса одной темы.
    @Query("""
        UPDATE user_interests 
        SET weight = weight + :weightDelta 
        WHERE user_id = :userId AND topic_id = :topicId
    """)
    suspend fun updateInterestWeight(userId: Int, topicId: Int, weightDelta: Int)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertInterestIfNotExists(interest: UserInterestsEntity)

    @Query("SELECT * FROM user_interests WHERE user_id = :userId")
    suspend fun getUserInterests(userId: Int): List<UserInterestsEntity>

}