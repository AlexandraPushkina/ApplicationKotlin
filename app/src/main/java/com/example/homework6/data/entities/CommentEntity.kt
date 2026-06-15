package com.example.homework6.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "post_comments")
data class CommentEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val postId: Int,
    val userId: Int,
    val content: String,
    val timestamp: Long = System.currentTimeMillis()
)
