package com.example.homework6

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 2
        private const val DATABASE_NAME = "android_app_database.db"
        const val TABLE_TOPICS = "topics"
        const val COLUMN_TOPIC_ID = "topic_id"
        const val COLUMN_TOPIC_NAME = "topic_name"
        private const val TABLE_USERS = "users"
        private const val COLUMN_ID = "id"
        private const val COLUMN_EMAIL = "email"
        private const val COLUMN_PASSWORD_HASH = "password_hash"
        private const val COLUMN_BIO = "bio"
        private const val COLUMN_USER_TOPIC_ID = "topic_id"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        // 1. СНАЧАЛА создаем таблицу тем (она независимая)
        val createTopicsTable = """
            CREATE TABLE $TABLE_TOPICS (
                $COLUMN_TOPIC_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TOPIC_NAME TEXT NOT NULL UNIQUE
            )
        """.trimIndent()
        db?.execSQL(createTopicsTable)

        // 2. Заполняем темы
        addInitialTopics(db)

        // 3. ПОТОМ создаем таблицу пользователей со ссылкой на темы
        val createUsersTable = """
            CREATE TABLE $TABLE_USERS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT NOT NULL,
                $COLUMN_EMAIL TEXT NOT NULL UNIQUE,
                $COLUMN_PASSWORD_HASH TEXT NOT NULL,
                $COLUMN_BIO TEXT,
                $COLUMN_USER_TOPIC_ID INTEGER, 
                FOREIGN KEY($COLUMN_USER_TOPIC_ID) REFERENCES $TABLE_TOPICS($COLUMN_TOPIC_ID)
            )
        """.trimIndent()
        db?.execSQL(createUsersTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Удаляем сначала зависимую таблицу (пользователи), потом главную (темы)
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_TOPICS")
        onCreate(db)
    }

    private fun addInitialTopics(db: SQLiteDatabase?) {
        val topics = listOf("Природа", "Искусство", "Декоративная косметика", "Спорт", "Еда")
        db?.beginTransaction()
        try {
            topics.forEach { topic ->
                val values = ContentValues().apply { put(COLUMN_TOPIC_NAME, topic) }
                db?.insert(TABLE_TOPICS, null, values)
            }
            db?.setTransactionSuccessful()
        } finally {
            db?.endTransaction()
        }
    }

    // topicId (число)
    // Например: 1 (Природа), 2 (Искусство) и т.д.
    fun addUser(email: String, passwordHash: String, bio: String, topicId: Int) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_EMAIL, email)
            put(COLUMN_PASSWORD_HASH, passwordHash)
            put("username", email.substringBefore('@'))
            put(COLUMN_USER_TOPIC_ID, topicId) // Сохраняем выбор пользователя
            put(COLUMN_BIO, bio) // Сохраняем выбор пользователя
        }

        db.insert(TABLE_USERS, null, values)
        // db.close() - не закрываем, чтобы работал инспектор
    }

    // Взять информацию для профиля (имя, био, интерес)
    fun getUserProfile(email: String): Cursor? {
        val db = this.readableDatabase

        val query = """
            SELECT u.username, u.bio, t.$COLUMN_TOPIC_NAME 
            FROM $TABLE_USERS u 
            LEFT JOIN $TABLE_TOPICS t ON u.$COLUMN_USER_TOPIC_ID = t.$COLUMN_TOPIC_ID 
            WHERE u.$COLUMN_EMAIL = ?
        """
        return db.rawQuery(query, arrayOf(email))
    }

}