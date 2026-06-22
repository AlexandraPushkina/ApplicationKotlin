package com.example.homework6.data

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.homework6.data.entities.PostEntity
import com.example.homework6.data.entities.PostTopicEntity
import com.example.homework6.data.entities.TopicEntity
import com.example.homework6.data.entities.UserEntity
import com.example.homework6.data.entities.UserInterestsEntity
import com.example.homework6.data.entities.PostTopicCrossRef
import com.example.homework6.data.entities.LikeEntity
import com.example.homework6.data.entities.HiddenPostEntity
import com.example.homework6.data.entities.CommentEntity
import com.example.homework6.data.test_data.generateTestPosts
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [
    UserEntity::class,
    TopicEntity::class,
    PostEntity::class,
    UserInterestsEntity::class,
    PostTopicEntity::class,
    PostTopicCrossRef::class,
    LikeEntity::class,
    HiddenPostEntity::class,
    CommentEntity::class],
    version = 18, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDao(): UserDao
    abstract fun topicDao(): TopicDao
    abstract fun postDao(): PostDao
    abstract fun userInterestsDao(): UserInterestsDao
    abstract fun interactionDao(): InteractionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "my_app_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()

                INSTANCE = instance

                CoroutineScope(Dispatchers.IO).launch {
                    populateDatabaseIfEmpty(instance)
                }
                instance
            }
        }

        private suspend fun populateDatabaseIfEmpty(db: AppDatabase) {
            try {
                val topicCount = db.topicDao().getTopicCount()

                if (topicCount == 0) {
                    Log.d("DEBUG_DB", "База пуста, начинаем заливку топиков...")
                    val initialTopics = listOf(
                        TopicEntity(1, "Природа"),
                        TopicEntity(2, "Искусство"),
                        TopicEntity(3, "Косметика"),
                        TopicEntity(4, "Спорт"),
                        TopicEntity(5, "Еда"),
                        TopicEntity(6, "IT"),
                        TopicEntity(7, "Путешествия"),
                        TopicEntity(8, "Наука")
                    )
                    db.topicDao().insertAllTopics(initialTopics)
                    Log.d("DEBUG_DB", "Топики успешно созданы!")


                    Log.d("DEBUG_DB", "Создаем тестового пользователя...")
                    val defaultUser = UserEntity(
                        id = 1,
                        useremail = "pushkina@mail.ru", // если есть такое поле
                        username = "Александра Пушкина",
                        password = "password",
                        bio = "Программист и оптимист"
                    )
                    db.userDao().insertUser(defaultUser)
                    val defaultUserInterests = listOf(UserInterestsEntity(
                        userId = defaultUser.id,
                        topicId = 6,
                        weight = 10
                    ))
                    db.userInterestsDao().insertInterests(defaultUserInterests)
                    Log.d("DEBUG_DB", "Начинаем заливку тестовых постов...")
                    val testPosts = generateTestPosts()

                    testPosts.forEach { testData ->
                        val postToInsert = testData.post.copy(id = 0)
                        val insertedPostId = db.postDao().insert(postToInsert)

                        val crossRefs = testData.topicIds.map { tId ->
                            PostTopicCrossRef(
                                postId = insertedPostId,
                                topicId = tId
                            )
                        }

                        if (crossRefs.isNotEmpty()) {
                            db.postDao().insertPostTopics(crossRefs)
                            Log.d("DEBUG_DB", "Для поста $insertedPostId добавлено тем: ${crossRefs.size}")
                        }
                    }
                    Log.d("DEBUG_DB", "Генерация всех тестовых данных завершена!")
                } else {
                    Log.d("DEBUG_DB", "База уже заполнена ($topicCount топиков). Пропускаем заливку.")
                }
            } catch (e: Exception) {
                Log.e("DEBUG_DB", "Ошибка при автозаполнении БД: ${e.message}")
            }
        }
    }
}