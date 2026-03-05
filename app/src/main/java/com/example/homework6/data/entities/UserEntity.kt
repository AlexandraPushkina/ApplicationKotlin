package com.example.homework6.data.entities

import android.provider.ContactsContract
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [Index(value = ["useremail"], unique = true)])  // Гарантия уникальности почты
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val useremail: String?,
    val username: String?,
    val password: String?,
    val bio: String?,

    // список из ID тем (1, 2, 3...)
    @ColumnInfo(name = "topic_ids")
    val topicId: String?
)