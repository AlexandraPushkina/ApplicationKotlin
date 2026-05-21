package com.example.homework6
//  перед выводом всех интересующих постов в ленте пользователю, указать им теги и вес, на
//  основе которых будет просчитана показываемая лента.
import com.example.homework6.data.entities.PostEntity

class FeedRankingUseCase(private val repository: PostRepository) {

    suspend operator fun invoke(interestWeightsMap: Map<Int, Int>): List<PostEntity> {
        // 1. Берем данные из репозитория
        val allPosts = repository.getAllPostsWithTopics()

        // 2. Логика ранжирования (твой код)
        return allPosts.mapNotNull { item ->
            val post = item.post
            val topics = item.topics

            val postScore = topics.maxOfOrNull { topic ->
                interestWeightsMap[topic.id] ?: 0
            } ?: -10

            if (postScore > 0) Pair(post, postScore) else null
        }
            .sortedByDescending { it.second }
            .map { it.first }
    }
}