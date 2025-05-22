package com.example.expensetracker

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.expensetracker.agenda.AgendaScreen
import com.example.expensetracker.firebase.AuthViewModel
import com.example.expensetracker.navigation.BottomNavBar
import com.example.expensetracker.navigation.Screen
import com.example.expensetracker.ui.theme.transaction.AddTransactionScreen
import com.example.expensetracker.ui.theme.home.HomeScreen
import com.example.expensetracker.ui.theme.login.LoginScreen
import com.example.expensetracker.ui.theme.profile.profileScreen
import com.example.expensetracker.ui.theme.signup.SignupScreen
import com.example.expensetracker.ui.theme.stats.StatsScreen
import com.example.expensetracker.ui.theme.stats.StatsViewModel


private const val TAG = "NavigationUtils"

@Composable
fun ExpenseTrackerApp(viewModel: StatsViewModel,authViewModel: AuthViewModel) {

    val navController = rememberNavController()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val isAuthRoute = remember(currentRoute) {
        currentRoute in listOf(Screen.Login.route, Screen.signup.route,Screen.agenda.route,Screen.profile.route)
    }
    val routea = remember(currentRoute) {
        currentRoute in listOf(Screen.Login.route, Screen.signup.route)
    }


    Scaffold(
        bottomBar =  {if (!routea) {
            BottomNavBar(navController)
        }
        },
        floatingActionButton = { if (!isAuthRoute) {
            FloatingActionButton(onClick = {
                navController.navigate(Screen.AddTransaction.route)
            }) {
                    Icon(Icons.Default.Add, "Add Transaction")
                }
            }
        }
    ){ padding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route,
            modifier = Modifier.padding(padding)
        ) {
            composable(Screen.Home.route) { HomeScreen(navController,viewModel = viewModel) }
            composable(Screen.Login.route) { LoginScreen(navController) }
            composable(Screen.AddTransaction.route) { AddTransactionScreen(navController,viewModel = viewModel) }
            composable(Screen.stats.route){StatsScreen(navController,viewModel)}
            composable(Screen.signup.route){ SignupScreen(navController) }
            composable(Screen.profile.route){profileScreen(navController)}
            composable(Screen.agenda.route){AgendaScreen(navController)}

        }
    }
}