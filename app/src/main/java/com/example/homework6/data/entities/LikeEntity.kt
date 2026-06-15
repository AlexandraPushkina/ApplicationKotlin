package com.example.homework6.data.entities

import androidx.room.Entity

@Entity(
    tableName = "post_likes",
    primaryKeys = ["userId", "postId"] // Составной ключ: юзер может лайкнуть конкретный пост только один раз
)
data class LikeEntity(
    val userId: Int,
    val postId: Int
)
