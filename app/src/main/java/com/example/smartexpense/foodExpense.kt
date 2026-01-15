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
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodExpenseScreen(
    onNavigateToClothes: () -> Unit = {},
    onNavigateToOther: () -> Unit = {},
    onNavigateToExpenseInput: () -> Unit,
    onNavigateBack: () -> Unit,
    onNavigateToExpenseUpdate: (Int) -> Unit,
    expenseViewModel: ExpenseViewModel = viewModel()
) {
    LaunchedEffect(Unit) {
        expenseViewModel.loadExpensesByCategory("Food")
    }

    val expenses by expenseViewModel.expenses.collectAsState()

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .background(GreenPrimary)
                    .padding(bottom = 16.dp)
            ) {
                TopAppBar(
                    title = { Text("My Expenses", color = Color.White, fontSize = 18.sp) },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color.White)
                        }
                    },
                    actions = {
                        Icon(Icons.Default.Search, contentDescription = "Search", tint = Color.White, modifier = Modifier.padding(end = 16.dp))
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = GreenPrimary)
                )

                // Search Bar
                TextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text("Search expenses...", color = Color.LightGray) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.LightGray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(50.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color(0xFF4CA773), // Lighter green
                        unfocusedContainerColor = Color(0xFF4CA773),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNavigateToExpenseInput,
                containerColor = GreenPrimary,
                contentColor = Color.White,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
            .background(Color(0xFFF5F5F5))) {

            // Filter Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)
            ) {
                FilterChipItem("Food (${expenses.size})", true, {})
                FilterChipItem("Clothes (${expenses.size})", false, onNavigateToClothes)
                FilterChipItem("Other (${expenses.size})", false, onNavigateToOther)
            }

            // Total Expenses Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = GreenPrimary)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Total Expenses", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    val total = expenses.sumOf { it.cost }
                    Text(color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold, text = "$${"%.2f".format(total)}")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // List of Food Expenses
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
                            icon = Icons.Default.Restaurant,
                            iconBgColor = Color(0xFFFFE0B2),
                            iconTint = Color(0xFFE65100),
                            onDelete = {
                                expenseViewModel.deleteExpense(expense)
                                expenseViewModel.loadExpensesByCategory("Food")
                            }
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }
    }
}

@Composable
fun FilterChipItem(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = Modifier.clickable { onClick() },
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) GreenPrimary else Color.White,
        border = if (!isSelected) androidx.compose.foundation.BorderStroke(1.dp, Color.LightGray) else null
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            color = if (isSelected) Color.White else Color.Gray,
            fontSize = 14.sp
        )
    }
}

@Composable
fun ExpenseItem(
    title: String,
    amount: String,
    date: String,
    category: String,
    icon: ImageVector,
    iconBgColor: Color,
    iconTint: Color
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(24.dp))
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Medium, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(color = Color(0xFFF0F0F0), shape = RoundedCornerShape(4.dp)) {
                        Text(category, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), fontSize = 10.sp, color = Color.Gray)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(date, fontSize = 12.sp, color = Color.Gray)
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(amount, color = GreenPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Icon(Icons.Default.MoreHoriz, contentDescription = "More", tint = Color.Gray, modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
fun ExpenseItem(
    title: String,
    amount: String,
    date: String,
    category: String,
    icon: ImageVector,
    iconBgColor: Color,
    iconTint: Color,
    onDelete: () -> Unit
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(iconBgColor),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(24.dp))
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Medium, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(color = Color(0xFFF0F0F0), shape = RoundedCornerShape(4.dp)) {
                        Text(
                            category,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            fontSize = 10.sp,
                            color = Color.Gray
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(date, fontSize = 12.sp, color = Color.Gray)
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(amount, color = GreenPrimary, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(4.dp))
                IconButton(onClick = { showDeleteDialog = true }) {
                    Icon(Icons.Default.MoreHoriz, contentDescription = "More", tint = Color.Gray, modifier = Modifier.size(20.dp))
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete Expense") },
            text = { Text("Are you sure you want to delete this expense?") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    onDelete()
                }) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

