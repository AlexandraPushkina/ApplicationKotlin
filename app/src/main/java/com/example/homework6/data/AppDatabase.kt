package com.example.homework6.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.homework6.data.entities.TopicEntity
import com.example.homework6.data.entities.UserEntity

// 1. Указываем ВСЕ сущности (таблицы), которые есть в базе
@Database(entities = [UserEntity::class, TopicEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // 2. Объявляем абстрактный метод для получения DAO
    abstract fun userDao(): UserDao

    // 3. Создаем Singleton (чтобы база открывалась только один раз)
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            // Если экземпляр уже есть — возвращаем его
            return INSTANCE ?: synchronized(this) {
                // Если нет — создаем новый
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "my_app_database" // Имя файла базы данных на телефоне
                )
                    // ДАННЫЕ ПРИ СТАРТЕ
                    .addCallback(object : RoomDatabase.Callback() {
                        // Метод onCreate вызывается ТОЛЬКО ОДИН РАЗ,
                        // когда приложение запускается впервые и файла базы еще нет.
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)

                            // Заполняем таблицу topics жесткими значениями
                            // id = 1 -> Природа
                            db.execSQL("INSERT INTO topics (id, name) VALUES (1, 'Природа')")
                            // id = 2 -> Искусство
                            db.execSQL("INSERT INTO topics (id, name) VALUES (2, 'Искусство')")
                            // id = 3 -> Косметика
                            db.execSQL("INSERT INTO topics (id, name) VALUES (3, 'Косметика')")
                            // id = 4 -> Спорт
                            db.execSQL("INSERT INTO topics (id, name) VALUES (4, 'Спорт')")
                            // id = 5 -> Еда
                            db.execSQL("INSERT INTO topics (id, name) VALUES (5, 'Еда')")
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}