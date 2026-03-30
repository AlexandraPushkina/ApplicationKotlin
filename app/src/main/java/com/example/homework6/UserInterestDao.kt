package com.example.homework6

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.homework6.data.entities.UserInterestsEntity

@Dao
interface UserInterestDao {

    //OnConflictStrategy.IGNORE - если запись существует, то не вызовет ошибку и не выполнит команду
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEmptyInterest(interest: UserInterestsEntity): Long

    //delta - сколько прибавить (отнять)
    @Query("UPDATE user_interests SET weight = weight + :delta " +
            "WHERE user_id = :userId AND topic_id = :topicId")
    suspend fun updateWeight(userId: Int, topicId: Int, delta: Int)

    @Transaction
    suspend fun incrementWeight(userId: Int, topicId: Int, delta: Int) {
        // Пытаемся вставить пустую запись с начальным весом 0, если её еще нет
        val result = insertEmptyInterest(UserInterestsEntity(userId, topicId, 0))

        // Теперь точно знаем, что запись есть — обновляем вес
        updateWeight(userId, topicId, delta)
    }
}
