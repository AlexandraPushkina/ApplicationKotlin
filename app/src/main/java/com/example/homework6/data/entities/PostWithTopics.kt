package com.example.homework6.data.entities

// DTO для ROOM, чтобы получать значения полей. Возвращает из запроса 1 объект, который содержит
// пост и его темы. Таблица-перемычка. Не таблица, а результат запроса (объединение)

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class PostWithTopics(
    @Embedded val post: PostEntity, // Берем данные поста
    @Relation(
        parentColumn = "id",        // ID из PostEntity
        entityColumn = "id",        // ID из TopicEntity
        associateBy = Junction(     // Через таблицу-перемычку
            value = PostTopicCrossRef::class,
            parentColumn = "post_id",
            entityColumn = "topic_id"
        )
    )
    val topics: List<TopicEntity>   // Список всех тем этого поста
)
