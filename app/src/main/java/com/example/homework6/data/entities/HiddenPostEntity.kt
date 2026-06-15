package com.example.homework6.data.entities

import androidx.room.Entity

@Entity(
    tableName = "hidden_posts",
    primaryKeys = ["userId", "postId"]
)
data class HiddenPostEntity(
    val userId: Int,
    val postId: Int
)
