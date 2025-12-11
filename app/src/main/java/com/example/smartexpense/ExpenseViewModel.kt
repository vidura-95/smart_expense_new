package com.example.smartexpense

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class ExpenseViewModel : ViewModel() {
    // We use mutableStateListOf so Jetpack Compose automatically
    // updates the UI when items are added or removed.
    private val _expenses = mutableStateListOf<Expense>()
    val expenses: List<Expense> get() = _expenses

    fun addExpense(title: String, amount: Double, category: String) {
        val newExpense = Expense(
            title = title,
            amount = amount,
            category = category
        )
        _expenses.add(newExpense)
    }

    // Optional: Helper to calculate total
    fun getTotalExpense(): Double {
        return _expenses.sumOf { it.amount }
    }
}
