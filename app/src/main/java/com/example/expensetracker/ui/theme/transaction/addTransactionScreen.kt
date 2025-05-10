package com.example.expensetracker.ui.theme.transaction

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.expensetracker.ui.theme.components.PrimaryButton
import com.example.expensetracker.ui.theme.components.SimpleTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(navController: NavController) {
    val context = LocalContext.current
    val stripePattern = Color(0xFFFFEEEE)

    // Transaction type options
    val transactionTypes = listOf("Income", "Expense")
    var selectedTransactionType by remember { mutableStateOf(transactionTypes[0]) }
    var expandedTransactionType by remember { mutableStateOf(false) }

    // Category options
    val incomeCategories = listOf("Salary", "Freelance", "Gift", "Investment", "Other")
    val expenseCategories = listOf("Food", "Transportation", "Housing", "Entertainment", "Shopping", "Utilities", "Other")
    val categories = if (selectedTransactionType == "Income") incomeCategories else expenseCategories
    var selectedCategory by remember { mutableStateOf(categories[0]) }
    var expandedCategory by remember { mutableStateOf(false) }

    // Transaction details
    var valueText by remember { mutableStateOf("") }
    var descriptionText by remember { mutableStateOf("") }

    // Main content
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(stripePattern)
    ) {
        // Top App Bar
        TopAppBar(
            title = { Text("Transaction") },
            navigationIcon = {
                IconButton(onClick = { /* Open drawer menu */ }) {
                    Icon(Icons.Default.Menu, contentDescription = "Menu")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )

        // Form content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Transaction Type Dropdown
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { expandedTransactionType = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A6DF0))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = selectedTransactionType,
                            color = Color.White,
                            fontSize = 16.sp
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Dropdown",
                            tint = Color.White
                        )
                    }
                }
                DropdownMenu(
                    expanded = expandedTransactionType,
                    onDismissRequest = { expandedTransactionType = false },
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    transactionTypes.forEach { type ->
                        DropdownMenuItem(
                            text = { Text(text = type) },
                            onClick = {
                                selectedTransactionType = type
                                // Reset category when transaction type changes
                                selectedCategory = if (type == "Income") incomeCategories[0] else expenseCategories[0]
                                expandedTransactionType = false
                            }
                        )
                    }
                }
            }

            // Value TextField
            SimpleTextField(
                value = valueText,
                onValueChange = { valueText = it },
                placeholder = "Value",
                label = ""
            )

            // Category Dropdown
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = { expandedCategory = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4A6DF0))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = selectedCategory,
                            color = Color.White,
                            fontSize = 16.sp
                        )
                        Icon(
                            imageVector = Icons.Default.ArrowDropDown,
                            contentDescription = "Dropdown",
                            tint = Color.White
                        )
                    }
                }
                DropdownMenu(
                    expanded = expandedCategory,
                    onDismissRequest = { expandedCategory = false },
                    modifier = Modifier.fillMaxWidth(0.9f)
                ) {
                    categories.forEach { category ->
                        DropdownMenuItem(
                            text = { Text(text = category) },
                            onClick = {
                                selectedCategory = category
                                expandedCategory = false
                            }
                        )
                    }
                }
            }

            // Description TextField
            SimpleTextField(
                value = descriptionText,
                onValueChange = { descriptionText = it },
                placeholder = "Description",
                label = ""
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Add Transaction Button
            PrimaryButton(
                text = "Add Transaction",
                onClick = {
                    // Validate input
                    if (valueText.isBlank()) {
                        Toast.makeText(context, "Please enter a value", Toast.LENGTH_SHORT).show()
                        return@PrimaryButton
                    }

                    // Add transaction to database or perform necessary actions
                    // For example:
                    // val transaction = Transaction(
                    //     type = selectedTransactionType,
                    //     value = valueText.toDoubleOrNull() ?: 0.0,
                    //     category = selectedCategory,
                    //     description = descriptionText
                    // )
                    // viewModel.addTransaction(transaction)

                    Toast.makeText(context, "Transaction added successfully", Toast.LENGTH_SHORT).show()

                    // Clear fields
                    valueText = ""
                    descriptionText = ""

                    // Optionally navigate back
                    // navController.popBackStack()
                }
            )

            Spacer(modifier = Modifier.weight(1f))

            // Home Button
            Button(
                onClick = { navController.navigate("home") { popUpTo("home") { inclusive = true } } },
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .height(48.dp)
                    .width(180.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5757))
            ) {
                Text(
                    text = "Go To Home Page",
                    color = Color.White,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}