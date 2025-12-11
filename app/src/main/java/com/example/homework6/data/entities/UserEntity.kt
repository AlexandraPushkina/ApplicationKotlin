package com.example.homework6.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val username: String,
    val password: String,
    val bio: String,

    // список из ID тем (1, 2, 3...)
    @ColumnInfo(name = "topic_ids")
    val topicId: String
)