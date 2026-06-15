package com.example.homework6.data

import android.content.Context
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

// 1. Указываем ВСЕ сущности (таблицы), которые есть в базе
@Database(entities = [UserEntity::class,
                    TopicEntity::class,
                    PostEntity::class,
                    UserInterestsEntity::class,
                    PostTopicEntity::class,
                    PostTopicCrossRef::class,
                    LikeEntity::class,
                    HiddenPostEntity::class,
                    CommentEntity::class ],
    version = 9, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // 2. Объявляем абстрактный метод для получения DAO
    abstract fun userDao(): UserDao
    abstract fun topicDao(): TopicDao
    abstract fun postDao(): PostDao
    abstract fun UserInterestsDao(): UserInterestsDao

    abstract fun InteractionDao(): InteractionDao

    // 3. Создаем Singleton (чтобы база открывалась только один раз)
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
                instance
            }
        }
    }
}