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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.expensetracker.navigation.Screen

@Composable
fun HomeScreen(navController: NavController) {
    val scrollState = rememberScrollState()

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
                amount = "$8,579.00",
                backgroundColor = Color(0xFF42A5F5).copy(alpha = 0.15f),
                textColor = Color(0xFF1976EB),
                modifier = Modifier.weight(1f)
            )

            FinanceCard(
                title = "Expense",
                amount = "$5,820.00",
                backgroundColor = Color(0xFFFF5252).copy(alpha = 0.15f),
                textColor = Color(0xFFE53935),
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Budget Section
        BudgetSection()

        Spacer(modifier = Modifier.height(24.dp))

        // Spending Chart
        SpendingSection()

        Spacer(modifier = Modifier.height(24.dp))

        // Alerts Section
        AlertsSection()
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
fun BudgetSection() {
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
                text = "50%",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LinearProgressIndicator(
            progress = 0.5f,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant
        )
    }
}

@Composable
fun SpendingSection() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "SPENDING",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Pie chart placeholder
        DonutChartPlaceholder()
    }
}

@Composable
fun DonutChartPlaceholder() {
    val colors = listOf(
        Color(0xFF3366CC), // Blue
        Color(0xFFFF9900), // Orange
        Color(0xFF109618), // Green
        Color(0xFFDC3912), // Red
        Color(0xFF990099)  // Purple
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        // Creating a simple pie chart representation
        Box(
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
        )

        // Legend
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(start = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            ChartLegendItem("Rent", colors[0], "40%")
            ChartLegendItem("Food", colors[1], "30%")
            ChartLegendItem("Utilities", colors[2], "15%")
            ChartLegendItem("Shopping", colors[3], "10%")
            ChartLegendItem("Others", colors[4], "5%")
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
fun AlertsSection() {
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

        Spacer(modifier = Modifier.height(16.dp))

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
                    text = "Budget limit exceeded!",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            FloatingActionButton(
                onClick = { /* Add new alert */ },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    Icons.Default.Notifications,
                    contentDescription = "Add Alert",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}