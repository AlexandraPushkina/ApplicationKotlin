package com.example.homework6.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.homework6.data.entities.TopicEntity
import com.example.homework6.data.entities.UserEntity
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
    suspend fun getUser(username: String): UserEntity?

    // --- ЗАПРОСЫ ДЛЯ ЭКРАНА ЛОГИНА (ВХОДА) ---

    // Поиск пользователя по паре "Почта + Пароль"
    // Временно не используется
    @Query("SELECT * FROM users WHERE username = :username AND password = :password")
    suspend fun getUserByUserNameAndPassword(username: String, password: String): UserEntity?

    // --- ЗАПРОСЫ ДЛЯ ЭКРАНА ПРОФИЛЯ ---
    

    // --- ДЛЯ ОТЛАДКИ (Если нужно смотреть список всех) ---
    @Query("SELECT * FROM users")
    fun getAllUsersFlow(): Flow<List<UserEntity>>
}

