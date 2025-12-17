package com.example.homework6.data

import com.example.homework6.data.entities.PostEntity
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PostDao {

    // Получить все посты из таблицы 'posts'
    @Query("SELECT * FROM posts")
    suspend fun getAllPosts(): List<PostEntity>

    // Посчитать количество постов
    @Query("SELECT COUNT(*) FROM posts")
    suspend fun getCount(): Int

    @Query("DELETE FROM posts")
    suspend fun deleteAll()

    // Вставить список постов
    // onConflict = REPLACE значит: если пост с ID=1 уже есть, он обновится
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(posts: List<PostEntity>)

    // Вставить один пост
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: PostEntity)

}
