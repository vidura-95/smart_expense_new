package com.example.smartexpense

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClothExpenseScreen(
    onNavigateToFood: () -> Unit = {},
    onNavigateToOther: () -> Unit = {},
    onNavigateToExpenseInput: () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToExpenseUpdate: (Int) -> Unit,
    expenseViewModel: ExpenseViewModel = viewModel()
) {

    LaunchedEffect(Unit) {
        expenseViewModel.loadExpensesByCategory("Clothes")
    }

    val expenses by expenseViewModel.expenses.collectAsState()

    Scaffold(
        topBar = {
            Column(modifier = Modifier.background(GreenPrimary).padding(bottom = 16.dp)) {
                TopAppBar(
                    title = { Text("My Expenses", color = Color.White, fontSize = 18.sp) },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                    },
                    actions = { Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White, modifier = Modifier.padding(end = 16.dp)) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = GreenPrimary)
                )
                TextField(
                    value = "", onValueChange = {},
                    placeholder = { Text("Search expenses...", color = Color.LightGray) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.LightGray) },
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(50.dp).clip(RoundedCornerShape(8.dp)),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF4CA773), unfocusedContainerColor = Color(0xFF4CA773),
                        focusedIndicatorColor = Color.Transparent, unfocusedIndicatorColor = Color.Transparent
                    )
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onNavigateToExpenseInput() },
                containerColor = GreenPrimary,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).fillMaxSize().background(Color(0xFFF5F5F5))) {

            // Filter Chips
            Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)) {
                FilterChipItem("Food (${expenses.size})", false, onNavigateToFood)
                FilterChipItem("Clothes (${expenses.size})", true, {})
                FilterChipItem("Other (${expenses.size})", false, onNavigateToOther)
            }

            // Total Expenses Card
            Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = GreenPrimary)) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Total Expenses", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    val total = expenses.sumOf { it.cost }
                    Text(color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold, text = "$${"%.2f".format(total)}")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // List of Clothes Expenses
            LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp)) {
                items(expenses) { expense ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onNavigateToExpenseUpdate(expense.eid) } // <– use auto-generated id
                    )
                    {
                        ExpenseItem(
                            title = expense.title,
                            amount = "$${expense.cost}",
                            date = expense.date,
                            category = expense.category,
                            icon = Icons.Default.ShoppingBag,
                            iconBgColor = Color(0xFFFFE0B2),
                            iconTint = Color(0xFFE65100),
                            onDelete = {
                                expenseViewModel.deleteExpense(expense)
                                expenseViewModel.loadExpensesByCategory("Clothes")
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}


