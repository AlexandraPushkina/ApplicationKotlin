package com.example.homework6.data.entities

// Возвращает из запроса 1 объект, который содержит тему и посты, имеющую эту тему

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

data class TopicWithPosts(@Embedded val topic: TopicEntity,

                          @Relation(
                              parentColumn = "id",
                              entityColumn = "id",
                              associateBy = Junction(
                                  value = PostTopicEntity::class,
                                  parentColumn = "topic_id",
                                  entityColumn = "post_id"
                              )
                          )
                          val posts: List<PostEntity>)
