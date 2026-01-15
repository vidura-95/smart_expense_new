package com.example.smartexpense

import androidx.room.*

@Dao
interface ExpenseDAO {
    @Insert
    suspend fun insert(expense: Expense)
    @Update
    suspend fun update(expense: Expense)
    @Delete
    suspend fun delete(expense: Expense)
    @Query("SELECT * FROM expenses")
    suspend fun getAll(): List<Expense>

    @Query("SELECT * FROM expenses WHERE eid = :id")
    suspend fun getById(id: Int): Expense?

    @Query("SELECT SUM(cost) FROM expenses WHERE category = :category")
    suspend fun getTotalForCategory(category: String): Double?
}