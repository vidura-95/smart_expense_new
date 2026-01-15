package com.example.smartexpense

class ExpenseRepository(private val expenseDao: ExpenseDAO) {

    suspend fun getAll(): List<Expense> {
        return expenseDao.getAll()
    }

    suspend fun getById(id: Int): Expense? {
        return expenseDao.getById(id)
    }

    suspend fun getTotalForCategory(category: String): Double? {
        return expenseDao.getTotalForCategory(category)
    }

    suspend fun insert(expense: Expense) {
        expenseDao.insert(expense)
    }

    suspend fun delete(expense: Expense) {
        expenseDao.delete(expense)
    }

    suspend fun update(expense: Expense) {
        expenseDao.update(expense)
    }
}
