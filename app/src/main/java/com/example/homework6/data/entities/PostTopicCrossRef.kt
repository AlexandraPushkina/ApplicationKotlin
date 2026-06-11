package com.example.homework6.data.entities
// таблица-связка, показывающая связь между постами и его темами
import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "post_topic_cross_ref",
    primaryKeys = ["post_id", "topic_id"]
)
data class PostTopicCrossRef(
    @ColumnInfo(name = "post_id")
    val postId: Long,
    @ColumnInfo(name = "topic_id")
    val topicId: Long
)

