package com.example.smartexpense

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")

data class Expense(
    @PrimaryKey(autoGenerate = true) val eid: Int = 0,
    val title: String,
    val category: String,
    val cost: Double,
    val date: String
)

