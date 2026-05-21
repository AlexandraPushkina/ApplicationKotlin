package com.example.homework6.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "post_topic",
    primaryKeys = ["post_id", "topic_id"],
    foreignKeys = [
        ForeignKey(
            entity = PostEntity::class,
            parentColumns = ["id"],
            childColumns = ["post_id"],
            onDelete = ForeignKey.CASCADE  // удалили пост — удалились строки связки
        ),
        ForeignKey(
            entity = TopicEntity::class,
            parentColumns = ["id"],
            childColumns = ["topic_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["post_id"]),
        Index(value = ["topic_id"])
    ]
)
data class PostTopicEntity(
    @ColumnInfo(name = "post_id") val postId: Int,
    @ColumnInfo(name = "topic_id") val topicId: Int
)