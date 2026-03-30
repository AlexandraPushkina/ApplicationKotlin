package com.example.homework6.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.homework6.data.entities.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    // Вставка пользователя.
    // OnConflictStrategy.ABORT выдаст исключение, если почта уже зарегистрирована
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertUser(user: UserEntity): Long

    @Update
    suspend fun updateUser(user: UserEntity)

    @Query("SELECT * FROM users WHERE useremail = :useremail LIMIT 1")
    suspend fun getUserByEmail(useremail: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: Int): UserEntity?

    @Query("SELECT * FROM users WHERE useremail = :useremail AND password = :password LIMIT 1")
    suspend fun checkUserByEmailAndPassword(useremail: String, password: String): UserEntity?

    @Query("SELECT id FROM users WHERE useremail = :useremail")
    suspend fun getUserId(useremail: String): Int?

    // Анонимизация профиля, вместо удаления. Остается только id
    @Query("""
        UPDATE users 
        SET useremail = '', 
            username = 'Удаленный пользователь', 
            password = '', 
            bio = null, 
            isDeleted = 1
        WHERE id = :userId
    """)
    suspend fun deactivateUser(userId: Int)

    // Существует ли профиль
    @Query("SELECT EXISTS(SELECT 1 FROM users WHERE useremail = :email)")
    suspend fun isEmailTaken(email: String): Boolean





    // Поиск пользователя по паре "Почта + Пароль"
    // Временно не используется
    //@Query("SELECT * FROM users WHERE username = :username AND password = :password")
    //suspend fun getUserByUserNameAndPassword(username: String, password: String): UserEntity?


    

    // --- ДЛЯ ОТЛАДКИ (Если нужно смотреть список всех) ---
    @Query("SELECT * FROM users")
    fun getAllUsersFlow(): Flow<List<UserEntity>>
}

