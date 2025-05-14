package com.example.expensetracker.ui.theme.stats

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.expensetracker.data.Transaction
import com.example.expensetracker.navigation.Screen
import com.example.expensetracker.ui.theme.components.IncomeExpenseChart

@Composable
fun StatsScreen(navController: NavController, viewModel: StatsViewModel) {
    val transactions by viewModel.getRecentTransactions().collectAsState(emptyList())

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFFEEEE))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { /* Open drawer */ }) {
                    Icon(
                        imageVector = Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = Color.Black
                    )
                }
                Text(
                    text = "Statistics",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                // Empty spacer to balance the layout
                Spacer(modifier = Modifier.width(48.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Daily Transactions Chart
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "DAILY TRANSACTIONS",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    IncomeExpenseChart(transactions = transactions)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Transaction Logging
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "TRANSACTION LOGGING",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    TransactionList(transactions = transactions)
                }
            }
        }


    }
}


@Composable
fun TransactionList(transactions: List<Transaction>) {
    LazyColumn {
        items(transactions) { transaction ->
            TransactionItem(transaction)
            Divider(color = Color.LightGray, thickness = 0.5.dp)
        }
    }
}

@Composable
fun TransactionItem(transaction: Transaction) {
    val categoryColor = when (transaction.category.lowercase()) {
        "food" -> Color(0xFFFFA500)
        "transport" -> Color(0xFF4169E1)
        "income" -> Color(0xFF32CD32)
        "others" -> Color(0xFFFF6347)
        else -> Color.Gray
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Category circle
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(categoryColor, CircleShape)
        )

        Spacer(modifier = Modifier.width(16.dp))

        // Transaction details
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = transaction.category.replaceFirstChar { it.uppercase() },
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
            Text(
                text = transaction.description,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        // Amount
        Text(
            text = if (transaction.type == "income") "+${transaction.amount}TND" else "-${transaction.amount}TND",
            fontWeight = FontWeight.Bold,
            color = if (transaction.type == "income") Color(0xFF32CD32) else Color.Black
        )
    }

}