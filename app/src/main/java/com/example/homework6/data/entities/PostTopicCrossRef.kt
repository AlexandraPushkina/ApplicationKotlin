package com.example.homework6.data.entities
// таблица-связка, показывающая связь между постами и его темами
import androidx.room.Entity

@Entity(
    tableName = "PostTopicCrossRef",
    primaryKeys = ["postId", "topicId"]
)
data class PostTopicCrossRef(
    val postId: Long,
    val topicId: Long
)

