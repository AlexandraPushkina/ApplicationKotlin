package com.example.homework6.utils

object InteractionWeights {

    const val NEUTRAL = 0
    // Начальный вес при выборе темы на этапе регистрации
    const val REGISTER_CHOICE = 10

    // Пользователь посмотрел пост
    const val VIEW_POST = 1

    // Пользователь лайкнул пост / добавил в избранное
    const val LIKE_POST = 5

    // Пользователь оставил комментарий
    const val COMMENT_POST = 7

    // Пользователь нажал "Не показывать больше"
    const val DONT_SHOW_AGAIN = -10
}