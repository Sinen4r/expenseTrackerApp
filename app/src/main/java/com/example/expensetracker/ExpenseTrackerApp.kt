package com.example.expensetracker

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.expensetracker.navigation.BottomNavBar
import com.example.expensetracker.navigation.Screen
import com.example.expensetracker.ui.theme.transaction.AddTransactionScreen
import com.example.expensetracker.ui.theme.home.HomeScreen
import com.example.expensetracker.ui.theme.login.LoginScreen
import com.example.expensetracker.ui.theme.profile.profileScreen
import com.example.expensetracker.ui.theme.signup.signupScreen
import com.example.expensetracker.ui.theme.stats.StatsScreen


// ExpenseTrackerApp.kt
@Composable
fun ExpenseTrackerApp() {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavBar(navController) },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(Screen.AddTransaction.route)
            }) {
                Icon(Icons.Default.Add, "Add Transaction")
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Home.route) { HomeScreen(navController) }
            composable(Screen.Login.route) { LoginScreen(navController) }
            composable(Screen.AddTransaction.route) { AddTransactionScreen(navController) }
            composable(Screen.stats.route){StatsScreen(navController)}
            composable(Screen.signup.route){ signupScreen(navController) }
            composable(Screen.profile.route){profileScreen(navController)}

        }
    }
}