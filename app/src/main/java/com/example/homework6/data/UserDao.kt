package com.example.homework6.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.homework6.data.entities.UserEntity
import com.example.homework6.data.entities.UserProfileData
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    // Вставка пользователя.
    // OnConflictStrategy.IGNORE защитит от падения
    // TODO: запретить создание одинаковых username
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: UserEntity)

    @Delete
    suspend fun deleteUser(user: UserEntity)

    @Update
    suspend fun updateUser(user: UserEntity)

    // --- ЗАПРОСЫ ДЛЯ ЭКРАНА РЕГИСТРАЦИИ ---

    // Проверка, существует ли уже пользователь с таким username
    // Временно не используется
    @Query("SELECT * FROM users WHERE username = :username LIMIT 1")
    suspend fun getUserByUserName(username: String): UserEntity?

    // --- ЗАПРОСЫ ДЛЯ ЭКРАНА ЛОГИНА (ВХОДА) ---

    // Поиск пользователя по паре "Почта + Пароль"
    // Временно не используется
    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    suspend fun getUserByUserNameAndPassword(username: String, password: String): UserEntity?

    // --- ЗАПРОСЫ ДЛЯ ЭКРАНА ПРОФИЛЯ ---

    @Query("""
        SELECT 
            u.username as username, 
            u.bio as bio, 
            t.name as topicName 
        FROM users u 
        INNER JOIN topics t ON u.topicId = t.id 
        WHERE u.username = :username
    """)
    suspend fun getUserProfile(username: String): UserProfileData?

    // --- ДЛЯ ОТЛАДКИ (Если нужно смотреть список всех) ---
    @Query("SELECT * FROM users")
    fun getAllUsersFlow(): Flow<List<UserEntity>>
}
