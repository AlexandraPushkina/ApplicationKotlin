package com.example.homework6.data.entities

import androidx.room.Entity

@Entity(
    tableName = "PostTopicCrossRef",
    primaryKeys = ["postId", "topicId"]
)
data class PostTopicCrossRef(
    val postId: Long,
    val topicId: Long
)

