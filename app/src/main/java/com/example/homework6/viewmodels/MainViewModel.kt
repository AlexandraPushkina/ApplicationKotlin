package com.example.homework6.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homework6.data.AppDatabase
import com.example.homework6.data.entities.PostEntity
import com.example.homework6.data.entities.PostTopicCrossRef
import com.example.homework6.data.entities.TopicEntity
import com.example.homework6.data.test_data.generateTestPosts
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val db: AppDatabase) : ViewModel() {

    // Функция инициализации данных
    fun initDatabase() {
        viewModelScope.launch(Dispatchers.IO) {

            // 1. Проверяем топики
            val topicCount = db.topicDao().getTopicCount()
            if (topicCount == 0) {
                val initialTopics = listOf(
                    TopicEntity(1, "Природа"),
                    TopicEntity(2, "Искусство"),
                    TopicEntity(3, "Косметика"),
                    TopicEntity(4, "Спорт"),
                    TopicEntity(5, "Еда")
                )
                db.topicDao().insertAllTopics(initialTopics)
                Log.d("DEBUG_DB", "Топики успешно созданы через DAO")
            }

            val count = db.postDao().getCount()
            Log.d("DEBUG_DB", "Текущее количество постов: $count")

            // Если постов нет (равно 0), то загружаем тестовые посты
            if (count == 13) {
                Log.d("DEBUG_DB", "База пуста, генерируем посты...")
                val testPosts = generateTestPosts() // List<TestPostData>

                testPosts.forEach { testData ->
                    // 1. Вставляем сам пост
                    val postToInsert = testData.post.copy(id = 0)
                    val insertedPostId = db.postDao().insert(postToInsert).toLong()

                    // 2. Формируем связи для таблицы post_topic
                    val crossRefs = testData.topicIds.map { tId ->
                        PostTopicCrossRef(
                            postId = insertedPostId,
                            topicId = tId
                        )
                    }

                    // 3. Вставляем связи в базу
                    if (crossRefs.isNotEmpty()) {
                        db.postDao().insertPostTopics(crossRefs)
                        Log.d("DEBUG_DB", "Для поста $insertedPostId добавлено тем: ${crossRefs.size}")
                    }
                }
                Log.d("DEBUG_DB", "Генерация тестовых данных завершена")
            }

//                // Преобразуем List<TestPostData> -> List<PostEntity>
//                val entities = testPosts.map { testData ->
//                    PostEntity(
//                        id = 0,
//                        userId = 1,
//                        authorName = testData.post.authorName,
//                        title = testData.post.title,
//                        content = testData.post.content,
//                        imageUrl = testData.post.imageUrl
//                    )
//                }
//
//                db.postDao().insertAll(entities)
//                Log.d("DEBUG_DB", "Посты успешно вставлены: ${entities.size}")
//            }
        }
    }
}
