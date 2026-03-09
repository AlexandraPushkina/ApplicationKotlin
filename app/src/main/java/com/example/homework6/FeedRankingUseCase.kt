package com.example.homework6

import com.example.homework6.data.entities.PostEntity

class FeedRankingUseCase {

    /**
     * @param allPosts Все посты из базы данных
     * @param interestWeightsMap Словарь весов интересов юзера, где Ключ = ID темы, Значение = Вес
     * @return Отфильтрованный и отсортированный список постов для ленты
     */
    operator fun invoke(allPosts: List<PostEntity>, interestWeightsMap: Map<Int, Int>): List<PostEntity> {

        // 1. ОЦЕНИВАЕМ КАЖДЫЙ ПОСТ И ФИЛЬТРУЕМ
        val scoredPosts = allPosts.mapNotNull { post ->

            // Разбиваем строку "1, 4" на список чисел [1, 4]
            val postTopicIds = post.topicIds.split(",").mapNotNull { it.trim().toIntOrNull() }

            // Ищем максимальный вес среди тем этого поста
            var postScore = -100 // Стартовое минимальное значение

            for (topicId in postTopicIds) {
                // Если темы нет в словаре юзера, считаем вес = 0
                val weight = interestWeightsMap[topicId] ?: 0

                if (weight > postScore) {
                    postScore = weight
                }
            }

            // Показываем пост, только если его оценка больше 0.
            if (postScore > 0) {
                Pair(post, postScore) // Связываем пост с его оценкой
            } else {
                null // Выбрасываем пост из итогового списка
            }
        }

        // 2. СОРТИРОВКА И ВЫДАЧА
        return scoredPosts
            .sortedByDescending { it.second } // Сортируем по убыванию оценки (самые интересные сверху)
            .map { it.first } // Оставляем только сами посты (убираем цифру оценки)
    }
}