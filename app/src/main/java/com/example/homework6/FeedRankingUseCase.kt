package com.example.homework6
//  перед выводом всех интересующих постов в ленте пользователю, указать им теги и вес, на
//  основе которых будет просчитана показываемая лента.
import com.example.homework6.data.entities.PostEntity
import com.example.homework6.utils.RecommendationAlgorithm.MILLISECOND
import com.example.homework6.utils.RecommendationAlgorithm.SECOND
import com.example.homework6.utils.RecommendationAlgorithm.TIME_DECAY
import kotlin.math.exp

class FeedRankingUseCase(private val repository: PostRepository) {

    suspend operator fun invoke(interestWeightsMap: Map<Int, Int>): List<PostEntity> {
        val allPosts = repository.getAllPostsWithTopics()
        val currentTime = System.currentTimeMillis()

        // 2. Ранжировка
        return allPosts
            .map { item ->
                val post = item.post
                val topics = item.topics

                // 1. Суммирование веса тегов
                val baseScore = topics.sumOf { topic ->
                    interestWeightsMap[topic.id] ?: 0
                }.toDouble()

                // 2. Эффект затухания. Возраст поста в миллисекундах
                val ageInMillis = currentTime - post.createdAt
                // Возраст поста в часах
                val ageInHours = ageInMillis / (MILLISECOND * SECOND * SECOND)

                // Коэффициент затухания (от 1 до 0.01 в зависимости от кого, сколько посту часов)
                val timeDecayMultiplier = if (ageInHours > 0) {
                    exp(-TIME_DECAY * ageInHours)
                } else {
                    1.0
                }

                // Итоговый балл поста
                val finalScore = baseScore * timeDecayMultiplier

                // Создаем пару: пост и его финальный (уже дробный) балл
                post to finalScore
            }
            // 3. Сортировка: наверху посты с самым высоким финальным баллом
            .sortedByDescending { it.second }
            // 4. Достаем обратно только посты
            .map { it.first }
    }
}
