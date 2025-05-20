package com.example.expensetracker.navigation

// Inside `navigation/Screen.kt`
sealed class Screen(val route: String) {
    object Home : Screen("home")       // Route for HomeScreen
    object Login : Screen("login")     // Route for LoginScreen
    object AddTransaction : Screen("transaction")
    object signup :Screen("signup")
    object profile :Screen("profile")
    object stats :Screen("stats")
    object agenda :Screen("agenda")



    // For passing arguments (e.g., "details/{id}")
    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg -> append("/$arg") }
        }
    }
}