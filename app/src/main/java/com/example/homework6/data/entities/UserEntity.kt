package com.example.homework6.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val username: String,
    val password: String,
    val bio: String,
    val topicId: Int // ID темы (1, 2, 3...)
)