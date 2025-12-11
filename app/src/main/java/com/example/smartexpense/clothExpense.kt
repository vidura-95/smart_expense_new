package com.example.smartexpense

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClothExpenseScreen(
    onNavigateToFood: () -> Unit = {},
    onNavigateToOther: () -> Unit = {},
    onNavigateToAdd: () -> Unit = {}
) {
    Scaffold(
        topBar = {
            Column(modifier = Modifier.background(GreenPrimary).padding(bottom = 16.dp)) {
                TopAppBar(
                    title = { Text("My Expenses", color = Color.White, fontSize = 18.sp) },
                    navigationIcon = { Icon(Icons.Default.Menu, contentDescription = "Menu", tint = Color.White, modifier = Modifier.padding(start = 8.dp)) },
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
                onClick = { onNavigateToAdd() },
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
                FilterChipItem("Food (2)", false, onNavigateToFood)
                FilterChipItem("Clothes (1)", true, {})
                FilterChipItem("Other (0)", false, onNavigateToOther)
            }

            // Total Expenses Card
            Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), shape = RoundedCornerShape(12.dp), colors = CardDefaults.cardColors(containerColor = GreenPrimary)) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text("Total Expenses", color = Color.White.copy(alpha = 0.8f), fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("$29.99", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // List of Clothes Expenses
            LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp)) {
                item {
                    ExpenseItem(
                        title = "New Shirt",
                        amount = "$29.99",
                        date = "2025-11-18",
                        category = "Clothes",
                        icon = Icons.Default.ShoppingBag,
                        iconBgColor = Color(0xFFE1BEE7), // Purple Light
                        iconTint = Color(0xFF7B1FA2)    // Purple Dark
                    )
                }
            }
        }
    }
}
