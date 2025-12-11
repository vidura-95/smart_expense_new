package com.example.smartexpense

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs


// Colors for the chart categories
val ColorFood = Color(0xFFE65100)    // Orange
val ColorClothes = Color(0xFF7B1FA2) // Purple
val ColorOther = Color(0xFF0288D1)   // Blue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onNavigateToFood: () -> Unit,
    onNavigateToClothes: () -> Unit,
    onNavigateToOther: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Dashboard", color = Color.White, fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = GreenPrimary)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // 1. Comparison Card
            ComparisonCard(currentMonthTotal = 850.00, lastMonthTotal = 920.00)

            Spacer(modifier = Modifier.height(24.dp))

            // 2. Bar Chart Section
            Text(
                "Monthly Expenses by Category",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(16.dp))

            // UPDATED: New Non-Stacked Bar Chart
            GroupedBarChart()

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                "Category Details",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.height(16.dp))

            // 3. Category Breakdown Cards
            CategoryProgressCard(
                categoryName = "Food & Dining",
                amountSpent = 450.00,
                budget = 600.00,
                color = ColorFood,
                icon = Icons.Default.Restaurant,
                onClick = onNavigateToFood
            )
            Spacer(modifier = Modifier.height(12.dp))

            CategoryProgressCard(
                categoryName = "Clothes & Shopping",
                amountSpent = 29.99,
                budget = 200.00,
                color = ColorClothes,
                icon = Icons.Default.ShoppingBag,
                onClick = onNavigateToClothes
            )
            Spacer(modifier = Modifier.height(12.dp))

            CategoryProgressCard(
                categoryName = "Other Expenses",
                amountSpent = 120.50,
                budget = 150.00,
                color = ColorOther,
                icon = Icons.Default.AttachMoney,
                onClick = onNavigateToOther
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// ----------------------------------------------------------------
// UPDATED CHART COMPOSABLES (Non-Stacked with Axis)
// ----------------------------------------------------------------

@Composable
fun GroupedBarChart() {
    val chartData = listOf(
        ChartData("Sep", 200f, 50f, 30f),
        ChartData("Oct", 250f, 120f, 80f),
        ChartData("Nov", 180f, 200f, 50f),
        ChartData("Dec", 300f, 80f, 100f)
    )
    val maxY = 400f // Max value for Y-axis scaling

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Legends
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                LegendItem(color = ColorFood, label = "Food")
                LegendItem(color = ColorClothes, label = "Clth")
                LegendItem(color = ColorOther, label = "Oth")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Custom Canvas Chart
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp) // Height for the drawing area
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val width = size.width
                    val height = size.height
                    val barGroupWidth = width / (chartData.size + 1) // Space per month group
                    val paddingX = 60f // Space for Y-axis labels on left
                    val availableWidth = width - paddingX
                    val groupGap = 20f // Gap between months

                    // Width of a single bar inside a group (3 bars per group)
                    val singleBarWidth = (availableWidth / chartData.size - groupGap) / 3

                    // Draw Y-Axis Lines & Labels
                    val textPaint = android.graphics.Paint().apply {
                        color = android.graphics.Color.GRAY
                        textSize = 30f
                        textAlign = android.graphics.Paint.Align.RIGHT
                    }

                    val stepCount = 4
                    val stepValue = maxY / stepCount
                    val stepHeight = height / stepCount

                    for (i in 0..stepCount) {
                        val y = height - (i * stepHeight)
                        val value = (i * stepValue).toInt()

                        // Draw horizontal grid line
                        drawLine(
                            color = Color.LightGray.copy(alpha = 0.5f),
                            start = Offset(paddingX, y),
                            end = Offset(width, y),
                            strokeWidth = 2f
                        )

                        // Draw text label using native canvas
                        drawContext.canvas.nativeCanvas.drawText(
                            value.toString(),
                            paddingX - 10f, // Position to the left of the axis
                            y + 10f,        // Center vertically
                            textPaint
                        )
                    }

                    // Draw Bars and X-Axis Labels
                    chartData.forEachIndexed { index, data ->
                        val groupStartX = paddingX + (index * (availableWidth / chartData.size)) + (groupGap / 2)

                        // 1. Food Bar
                        val foodHeight = (data.food / maxY) * height
                        drawRect(
                            color = ColorFood,
                            topLeft = Offset(groupStartX, height - foodHeight),
                            size = Size(singleBarWidth, foodHeight)
                        )

                        // 2. Clothes Bar
                        val clothesHeight = (data.clothes / maxY) * height
                        drawRect(
                            color = ColorClothes,
                            topLeft = Offset(groupStartX + singleBarWidth, height - clothesHeight),
                            size = Size(singleBarWidth, clothesHeight)
                        )

                        // 3. Other Bar
                        val otherHeight = (data.other / maxY) * height
                        drawRect(
                            color = ColorOther,
                            topLeft = Offset(groupStartX + (singleBarWidth * 2), height - otherHeight),
                            size = Size(singleBarWidth, otherHeight)
                        )

                        // X-Axis Month Label
                        val labelPaint = android.graphics.Paint().apply {
                            color = android.graphics.Color.DKGRAY
                            textSize = 32f
                            textAlign = android.graphics.Paint.Align.CENTER
                        }

                        // Center text under the group
                        val centerX = groupStartX + (singleBarWidth * 1.5f)
                        drawContext.canvas.nativeCanvas.drawText(
                            data.month,
                            centerX,
                            height + 40f, // Position below the X-axis line
                            labelPaint
                        )
                    }
                }
            }
            // Extra spacer for the bottom labels
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

data class ChartData(
    val month: String,
    val food: Float,
    val clothes: Float,
    val other: Float
)

@Composable
fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, fontSize = 12.sp, color = Color.Gray)
    }
}

// ----------------------------------------------------------------
// EXISTING HELPERS
// ----------------------------------------------------------------

@Composable
fun ComparisonCard(currentMonthTotal: Double, lastMonthTotal: Double) {
    val isSaving = currentMonthTotal < lastMonthTotal
    val difference = abs(currentMonthTotal - lastMonthTotal)
    val percentage = if (lastMonthTotal > 0) (difference / lastMonthTotal) * 100 else 0.0

    Card(
        colors = CardDefaults.cardColors(containerColor = GreenPrimary),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                "Total Spending (This Month)",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp
            )
            Text(
                "$${currentMonthTotal}",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (isSaving) Icons.Default.TrendingDown else Icons.Default.TrendingUp,
                    contentDescription = null,
                    tint = if (isSaving) Color(0xFFC8E6C9) else Color(0xFFFFCCBC),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${String.format("%.1f", percentage)}% ${if (isSaving) "less" else "more"} than last month",
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
fun CategoryProgressCard(
    categoryName: String,
    amountSpent: Double,
    budget: Double,
    color: Color,
    icon: ImageVector,
    onClick: () -> Unit
) {
    val progress = (amountSpent / budget).toFloat().coerceIn(0f, 1f)

    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(categoryName, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("$${amountSpent}", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = color,
                    trackColor = Color.LightGray.copy(alpha = 0.3f),
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "Budget: $$budget",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.width(12.dp))
            Icon(
                Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = null,
                tint = Color.Gray
            )
        }
    }
}
