package com.example.homework6.data.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    tableName = "post_topic",
    primaryKeys = ["postId", "topicId"],
    foreignKeys = [
        ForeignKey(
            entity = PostEntity::class,
            parentColumns = ["id"],
            childColumns = ["postId"],
            onDelete = ForeignKey.CASCADE  // удалили пост — удалились строки связки
        ),
        ForeignKey(
            entity = TopicEntity::class,
            parentColumns = ["id"],
            childColumns = ["topicId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["postId"]),
        Index(value = ["topicId"])
    ]
)
data class PostTopicEntity(
    @ColumnInfo(name = "postId") val postId: Long,
    @ColumnInfo(name = "topicId") val topicId: Int
)