package com.example.expensetracker.ui.theme.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.expensetracker.ui.theme.components.PrimaryButton
import com.example.expensetracker.ui.theme.components.SimpleTextField

@Composable
fun LoginScreen(navController: NavController, viewModel: UserInputViewModel = viewModel()) {
    val emailState by viewModel.emailState
    val passwordState by viewModel.passwordState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Login",
            fontSize = 54.sp,
            color = Color.White,
            modifier = Modifier.align(Alignment.Start)
        )

        SimpleTextField(
            value = emailState.text,
            onValueChange = { viewModel.onEmailChange(it) },
            label = "Email",
            isError = emailState.error != null,
            errorMessage = emailState.error
        )
        Spacer(modifier = Modifier.height(16.dp))

        SimpleTextField(
            value = passwordState.text,
            onValueChange = { viewModel.onPasswordChange(it) },
            label = "Password",
            isPassword = true,
            isError = passwordState.error != null,
            errorMessage = passwordState.error
        )
        Spacer(modifier = Modifier.height(26.dp))

        PrimaryButton("Log In",onClick = {}) {
            viewModel.performLogin()
            if (viewModel.validateLogin()) {
                // Navigate to home screen or dashboard on successful login
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            }
        }
        Text("or", color = Color.White)
        PrimaryButton("Sign Up", onClick = {}) {
            viewModel.performSignup()
            if (viewModel.validateLogin()) {
                // Navigate to signup confirmation or home screen
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            }
        }
    }
}