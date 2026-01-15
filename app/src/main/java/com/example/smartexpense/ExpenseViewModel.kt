package com.example.smartexpense

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ExpenseViewModel (app: Application) : AndroidViewModel(app) {
    private val expenseRepository: ExpenseRepository

    init {
        val expenseDao = AppDatabase.getInstance(app).expenseDao()
        expenseRepository = ExpenseRepository(expenseDao)
    }

    fun addExpense(title: String, cost: Double, category: String, date: String) {
        viewModelScope.launch(Dispatchers.IO) {
            expenseRepository.insert(Expense(title = title, category = category, cost = cost, date = date))
        }
    }

    fun updateExpense(expense: Expense) {
        viewModelScope.launch(Dispatchers.IO) {
            expenseRepository.update(expense)
        }
    }

    suspend fun getExpenseById(id: Int): Expense? = expenseRepository.getById(id)

    fun deleteExpense(expense: Expense) {
        viewModelScope.launch(Dispatchers.IO) {
            expenseRepository.delete(expense)
        }
    }

    fun getExpenses() {
        viewModelScope.launch(Dispatchers.IO) {
            expenseRepository.getAll()
        }
    }

    // Exposed state for the UI
    private val _expenses = MutableStateFlow<List<Expense>>(emptyList())
    val expenses: StateFlow<List<Expense>> = _expenses

    fun loadExpensesByCategory(category: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = expenseRepository.getAll().filter { it.category == category }
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
            val food = expenseRepository.getTotalForCategory("Food") ?: 0.0
            val clothes = expenseRepository.getTotalForCategory("Clothes") ?: 0.0
            val other = expenseRepository.getTotalForCategory("Other") ?: 0.0

            _foodTotal.value = food
            _clothesTotal.value = clothes
            _otherTotal.value = other
        }
    }
}
