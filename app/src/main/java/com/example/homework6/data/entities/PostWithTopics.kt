package com.example.homework6.data.entities

// DTO для ROOM, чтобы получать значения полей. Возвращает из запроса 1 объект, который содержит
// пост и его темы

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class PostWithTopics(@Embedded val post: PostEntity,

                          @Relation(
                              parentColumn = "id",
                              entityColumn = "id",
                              associateBy = Junction(
                                  value = PostTopicEntity::class,
                                  parentColumn = "post_id",
                                  entityColumn = "topic_id"
                              )
                          )
                          val topics: List<TopicEntity>)
