package com.example.smartexpense

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,
    val username: String,
    val passwordHash: String // Storing password hash instead of plain text
)
