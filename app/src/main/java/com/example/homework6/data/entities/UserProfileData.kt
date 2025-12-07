package com.example.homework6.data.entities


data class UserProfileData(
    val username: String,
    val bio: String,
    val topicName: String // Уже расшифрованное название ("Спорт", а не "4")
)