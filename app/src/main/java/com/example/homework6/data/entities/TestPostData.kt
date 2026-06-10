package com.example.homework6.data.entities

// Вспомогательный класс, чтобы удобно хранить данные перед вставкой

data class TestPostData(val post: PostEntity,
                        val topicIds: List<Long>)
