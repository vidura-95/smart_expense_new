package com.example.smartexpense

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExpenseViewModel (app: Application) : AndroidViewModel(app) {
    private val db = AppDatabase.getInstance(app)
    private val expenseDao = db.expenseDao()

    fun addExpense(title: String, cost: Double, category: String, date: String) {
        viewModelScope.launch(Dispatchers.IO) {
            expenseDao.insert(Expense(title = title, category = category, cost = cost, date = date))
        }
    }

    fun updateExpense(expense: Expense) {
        viewModelScope.launch(Dispatchers.IO) {
            expenseDao.update(expense)
        }
    }

    suspend fun getExpenseById(id: Int): Expense? = expenseDao.getById(id)

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch(Dispatchers.IO) {
            expenseDao.delete(expense)
        }
    }

    fun getExpenses() {
        viewModelScope.launch(Dispatchers.IO) {
            expenseDao.getAll()
        }
    }

    // Exposed state for the UI
    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())
    val expenses: StateFlow<List<Expense>> = _expenses

    fun loadExpensesByCategory(category: String) {
        viewModelScope.launch(Dispatchers.IO) {
            // You can filter in SQL (preferred) or in Kotlin.
            val list = expenseDao.getAll().filter { it.category == category }
            _expenses.value = list
        }
    }

    private val _foodTotal = MutableStateFlow(0.0)
    val foodTotal: StateFlow<Double> = _foodTotal

    private val _clothesTotal = MutableStateFlow(0.0)
    val clothesTotal: StateFlow<Double> = _clothesTotal

    private val _otherTotal = MutableStateFlow(0.0)
    val otherTotal: StateFlow<Double> = _otherTotal

    fun loadCategoryTotals() {
        viewModelScope.launch(Dispatchers.IO) {
            val food = expenseDao.getTotalForCategory("Food") ?: 0.0
            val clothes = expenseDao.getTotalForCategory("Clothes") ?: 0.0
            val other = expenseDao.getTotalForCategory("Other") ?: 0.0

            _foodTotal.value = food
            _clothesTotal.value = clothes
            _otherTotal.value = other
        }
    }
}