package com.example.homework6.extensions

import com.example.homework6.data.entities.TopicEntity

fun TopicEntity.getNameWithEmoji(): String {
    val emoji = when (this.id) {
        1 -> "🌿" // Природа
        2 -> "🎨" // Искусство
        3 -> "💄" // Косметика
        4 -> "⚽️" // Спорт
        5 -> "🍔" // Еда
        6 -> "💻" // IT
        7 -> "✈️" // Путешествия
        8 -> "🔬" // Наука
        else -> "📌" // Дефолтный эмодзи, если ID не совпал
    }

    return "$emoji ${this.name}"
}