package com.example.homework6
//  перед выводом всех интересующих постов в ленте пользователю, указать им теги и вес, на
//  основе которых будет просчитана показываемая лента.
import com.example.homework6.data.entities.PostEntity

class FeedRankingUseCase(private val repository: PostRepository) {

    suspend operator fun invoke(interestWeightsMap: Map<Int, Int>): List<PostEntity> {
        // 1. Получаем все посты со всеми их темами
        val allPosts = repository.getAllPostsWithTopics()

        // 2. Ранжируем
        return allPosts
            .map { item ->
                val post = item.post
                val topics = item.topics

                // Считаем СУММАРНЫЙ вес всех тегов поста
                // Если тега нет в интересах пользователя, прибавляем 0
                val totalPostScore = topics.sumOf { topic ->
                    interestWeightsMap[topic.id] ?: 0
                }

                // Создаем пару: пост и его итоговый балл
                post to totalPostScore
            }
            // 3. Сортируем: сначала самые релевантные (где сумма баллов выше)
            .sortedByDescending { it.second }
            // 4. Оставляем только объекты постов
            .map { it.first }
    }
}
