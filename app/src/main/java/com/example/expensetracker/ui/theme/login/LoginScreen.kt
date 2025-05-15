package com.example.expensetracker.ui.theme.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.expensetracker.navigation.Screen
import com.example.expensetracker.ui.theme.components.PrimaryButton
import com.example.expensetracker.ui.theme.components.SimpleTextField

@Composable
fun LoginScreen(navController: NavController, viewModel: UserInputViewModel = viewModel()) {
    val emailState by viewModel.emailState
    val passwordState by viewModel.passwordState
    val loginState by viewModel.loginState

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
            modifier = Modifier.align(Alignment.Start).padding(20.dp)
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

        // Show buttons or loading indicator based on loginState
        when (loginState) {
            is LoginState.Loading -> {
                CircularProgressIndicator(color = Color.White)
            }
            else -> {
                PrimaryButton(
                    text = "Log In",
                    onClick = {
                        viewModel.performLogin() // Login action only on button press
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "or",
                    color = Color.White,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                PrimaryButton(
                    text = "Sign Up",
                    onClick =
                        { navController.navigate(Screen.signup.route) } // Signup action only on button press

                )
            }
        }

        // Handle login/signup result
        when (loginState) {
            is LoginState.Success -> {
                LaunchedEffect(loginState) {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                    viewModel.resetLoginState()
                }
            }
            is LoginState.Error -> {
                Snackbar(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    action = {
                        Text(
                            text = "Dismiss",
                            color = Color.White,
                            modifier = Modifier.clickable { viewModel.resetLoginState() }
                        )
                    }
                ) {
                    Text(
                        text = (loginState as LoginState.Error).message,
                        color = Color.White
                    )
                }
            }
            else -> {}
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen(
        navController = rememberNavController(),
        viewModel = UserInputViewModel().apply {
            onEmailChange("test@example.com")
            onPasswordChange("password123")
        }
    )
}