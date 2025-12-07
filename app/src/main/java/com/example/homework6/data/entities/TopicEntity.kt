package com.example.homework6.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "topics")
data class TopicEntity(
    @PrimaryKey val id: Int, // id задаем сами (1, 2, 3...), чтобы жестко привязать темы
    val name: String
)