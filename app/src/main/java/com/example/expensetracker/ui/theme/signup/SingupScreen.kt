package com.example.expensetracker.ui.theme.signup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import com.example.expensetracker.ui.theme.components.PrimaryButton
import com.example.expensetracker.ui.theme.components.SimpleTextField

@Composable
fun SignupScreen(navController: NavController, viewModel: SignupViewModel = viewModel()) {
    val nameState by viewModel.nameState
    val emailState by viewModel.emailState
    val passwordState by viewModel.passwordState
    val confirmPasswordState by viewModel.confirmPasswordState
    val signupState by viewModel.signupState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Sign Up",
            fontSize = 54.sp,
            color = Color.White,
            modifier = Modifier.align(Alignment.Start)
        )

        SimpleTextField(
            value = nameState.text,
            onValueChange = { viewModel.onNameChange(it) },
            label = "Full Name",
            isError = nameState.error != null,
            errorMessage = nameState.error
        )
        Spacer(modifier = Modifier.height(16.dp))

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
        Spacer(modifier = Modifier.height(16.dp))

        SimpleTextField(
            value = confirmPasswordState.text,
            onValueChange = { viewModel.onConfirmPasswordChange(it) },
            label = "Confirm Password",
            isPassword = true,
            isError = confirmPasswordState.error != null,
            errorMessage = confirmPasswordState.error
        )
        Spacer(modifier = Modifier.height(26.dp))

        // Show buttons or loading indicator based on signupState
        when (signupState) {
            is SignupState.Loading -> {
                CircularProgressIndicator(color = Color.White)
            }
            else -> {
                PrimaryButton(
                    text = "Create Account",
                    onClick = {
                        viewModel.performSignup()
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
                    text = "Back to Login",
                    onClick = { navController.popBackStack() }
                )
            }
        }

        // Handle signup result
        when (signupState) {
            is SignupState.Success -> {
                LaunchedEffect(signupState) {
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                    viewModel.resetSignupState()
                }
            }
            is SignupState.Error -> {
                Snackbar(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    action = {
                        Text(
                            text = "Dismiss",
                            color = Color.White,
                            modifier = Modifier.clickable { viewModel.resetSignupState() }
                        )
                    }
                ) {
                    Text(
                        text = (signupState as SignupState.Error).message,
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
fun SignupScreenPreview() {
    SignupScreen(
        navController = rememberNavController(),
        viewModel = SignupViewModel().apply {
            onNameChange("John Doe")
            onEmailChange("john@example.com")
            onPasswordChange("password123")
            onConfirmPasswordChange("password123")
        }
    )
}