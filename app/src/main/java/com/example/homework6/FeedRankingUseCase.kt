package com.example.homework6
//  перед выводом всех интересующих постов в ленте пользователю, указать им теги и вес, на
//  основе которых будет просчитана показываемая лента.
import com.example.homework6.data.entities.PostEntity

class FeedRankingUseCase(private val repository: PostRepository) {

    suspend operator fun invoke(interestWeightsMap: Map<Int, Int>): List<PostEntity> {
        // 1. Получение всех постов со всеми их темами
        val allPosts = repository.getAllPostsWithTopics()

        // 2. Ранжировка
        return allPosts
            .map { item ->
                val post = item.post
                val topics = item.topics

                //  Суммарный вес всех тегов поста
                val totalPostScore = topics.sumOf { topic ->
                    interestWeightsMap[topic.id] ?: 0
                }

                // Создание пары: пост и его итоговый балл
                post to totalPostScore
            }
            // 3. Сортировка, где сначала самые релевантные
            .sortedByDescending { it.second }
            // 4. Оставляем только объекты постов
            .map { it.first }
    }
}
