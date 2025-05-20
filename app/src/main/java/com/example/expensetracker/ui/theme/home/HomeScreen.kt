package com.example.expensetracker.ui.theme.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Stroke
import androidx.compose.foundation.Canvas

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.expensetracker.data.Transaction
import com.example.expensetracker.navigation.Screen
import com.example.expensetracker.ui.theme.components.IncomeExpenseChart
import com.example.expensetracker.ui.theme.stats.StatsViewModel
import kotlinx.coroutines.flow.map

@Composable
fun HomeScreen(navController: NavController, viewModel: StatsViewModel) {
    val scrollState = rememberScrollState()

    // Collect transaction data
    val transactions by viewModel.getRecentTransactions().collectAsState(emptyList())

    // Calculate totals
    val totalIncome by viewModel.getIncomeTransactions()
        .map { it.sumOf { transaction -> transaction.amount.toDouble() } }
        .collectAsState(initial = 0.0)

    val totalExpense by viewModel.getExpenseTransactions()
        .map { it.sumOf { transaction -> transaction.amount.toDouble() } }
        .collectAsState(initial = 0.0)

    // Calculate budget usage
    val budgetLimit = 3000f // This could be dynamic from the viewModel
    val budgetUsedPercentage = (totalExpense / budgetLimit).toFloat().coerceIn(0f, 1f)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        // Top Bar with Search
        TopBar()

        Spacer(modifier = Modifier.height(16.dp))

        // Income and Expense Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FinanceCard(
                title = "Income",
                amount = "$${String.format("%.2f", totalIncome)}",
                backgroundColor = Color(0xFF42A5F5).copy(alpha = 0.15f),
                textColor = Color(0xFF1976EB),
                modifier = Modifier.weight(1f)
            )

            FinanceCard(
                title = "Expense",
                amount = "$${String.format("%.2f", totalExpense)}",
                backgroundColor = Color(0xFFFF5252).copy(alpha = 0.15f),
                textColor = Color(0xFFE53935),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Budget Section
        BudgetSection(budgetUsedPercentage = budgetUsedPercentage)

        Spacer(modifier = Modifier.height(24.dp))

        // Spending Chart - Now using real data
        SpendingSection(transactions = transactions)

        Spacer(modifier = Modifier.height(24.dp))

        // Alerts Section - Show alert if budget is exceeded
        AlertsSection(budgetExceeded = budgetUsedPercentage >= 0.9f, navController = navController)
    }
}

@Composable
fun TopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Overview",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = "",
                onValueChange = { },
                placeholder = { Text("Search for transactions") },
                modifier = Modifier
                    .width(200.dp)
                    .height(44.dp),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                singleLine = true,
                shape = RoundedCornerShape(8.dp)
            )
        }
    }
}

@Composable
fun FinanceCard(
    title: String,
    amount: String,
    backgroundColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(100.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = textColor
            )

            Text(
                text = amount,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                ),
                color = textColor
            )
        }
    }
}

@Composable
fun BudgetSection(budgetUsedPercentage: Float) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "BUDGETS",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )

            Text(
                text = "${(budgetUsedPercentage * 100).toInt()}%",
                style = MaterialTheme.typography.bodySmall,
                color = when {
                    budgetUsedPercentage >= 0.9f -> Color(0xFFE53935) // Red for high usage
                    budgetUsedPercentage >= 0.7f -> Color(0xFFFFA500) // Orange for moderate usage
                    else -> MaterialTheme.colorScheme.primary
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = budgetUsedPercentage,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = when {
                budgetUsedPercentage >= 0.9f -> Color(0xFFE53935) // Red for high usage
                budgetUsedPercentage >= 0.7f -> Color(0xFFFFA500) // Orange for moderate usage
                else -> MaterialTheme.colorScheme.primary
            },
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

@Composable
fun SpendingSection(transactions: List<Transaction>) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "SPENDING",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (transactions.isNotEmpty()) {
            DonutChartWithData(transactions = transactions)
        } else {
            Text(
                text = "No transaction data available",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun DonutChartWithData(transactions: List<Transaction>) {
    // Filter only expense transactions
    val expenseTransactions = transactions.filter { it.type == "expense" }

    // Group transactions by category and calculate totals
    val categoryTotals = expenseTransactions
        .groupBy { it.category }
        .mapValues { (_, transactions) ->
            transactions.sumOf { it.amount.toDouble() }
        }

    // Calculate percentages
    val total = categoryTotals.values.sum()
    val categoryPercentages = if (total > 0) {
        categoryTotals.mapValues { (_, amount) -> amount / total }
    } else {
        emptyMap()
    }

    // Define colors for categories
    val categoryColors = mapOf(
        "Groceries" to Color(0xFF3366CC),
        "Food" to Color(0xFFFF9900),
        "Transport" to Color(0xFF109618),
        "Entertainment" to Color(0xFFDC3912),
        "Others" to Color(0xFF990099)
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Donut Chart
        Box(
            modifier = Modifier
                .size(150.dp)
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.size(150.dp)) {
                val innerRadius = size.minDimension * 0.3f // 30% of diameter for hole
                var startAngle = 0f
                if (categoryPercentages.isEmpty()) {
                    // Draw grey ring for empty state
                    drawArc(
                        color = Color.LightGray,
                        startAngle = 0f,
                        sweepAngle = 360f,
                        useCenter = false,
                        size = size,
                        style = Stroke(width = size.minDimension * 0.2f)
                    )
                } else {
                    categoryPercentages.forEach { (category, percentage) ->
                        val sweepAngle = (percentage * 360f).toFloat()
                        drawArc(
                            color = categoryColors[category] ?: Color.Gray,
                            startAngle = startAngle,
                            sweepAngle = sweepAngle,
                            useCenter = false,
                            size = size,
                            style = Stroke(width = size.minDimension * 0.2f)
                        )
                        startAngle += sweepAngle
                    }
                }
                // Draw inner circle to create donut hole
                drawCircle(
                    color = MaterialTheme.colorScheme.background,
                    radius = innerRadius
                )
            }
        }

        // Legend
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            if (categoryPercentages.isEmpty()) {
                Text(
                    text = "No expense data",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            } else {
                categoryPercentages.forEach { (category, percentage) ->
                    ChartLegendItem(
                        label = category,
                        color = categoryColors[category] ?: Color.Gray,
                        percentage = "${(percentage * 100).toInt()}%"
                    )
                }
            }
        }
    }
}

@Composable
fun ChartLegendItem(label: String, color: Color, percentage: String) {
    Row(
        modifier = Modifier.padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color, CircleShape)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = percentage,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}

@Composable
fun AlertsSection(budgetExceeded: Boolean, navController: NavController) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "ALERTS",
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
            )

            if (budgetExceeded) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .background(Color.Red, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "1",
                        color = Color.White,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (budgetExceeded) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Notifications,
                        contentDescription = "Alert",
                        tint = MaterialTheme.colorScheme.error
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = "Budget limit exceeded! Check your spending.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
        } else {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Notifications,
                        contentDescription = "Alert",
                        tint = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Text(
                        text = "No alerts at the moment. Keep it up!",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = { navController.navigate(Screen.stats.route) },
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("View Detailed Statistics")
            }
        }
    }
}