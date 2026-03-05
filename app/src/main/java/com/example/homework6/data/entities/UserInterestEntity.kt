package com.example.homework6.data.entities;

import androidx.room.ColumnInfo
import androidx.room.Entity;
import androidx.room.ForeignKey

@Entity(
    tableName = "user_interests",
    primaryKeys = ["user_id", "topic_id"],
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE // Если юзер удален совсем, удаляем и интересы
        ),
        ForeignKey(
            entity = TopicEntity::class,
            parentColumns = ["id"],
            childColumns = ["topic_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UserInterestEntity(
    @ColumnInfo(name = "user_id")
    val userId: Int,

    @ColumnInfo(name = "topic_id")
    val topicId: Int,

    @ColumnInfo(name = "weight")
    val weight: Int = 0 // Вес интереса (положительное число)
)