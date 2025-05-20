package com.example.expensetracker.ui.theme.signup

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.util.Log

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.expensetracker.firebase.AuthViewModel
import com.example.expensetracker.navigation.Screen
import com.example.expensetracker.ui.theme.components.PrimaryButton
import com.example.expensetracker.ui.theme.components.SimpleTextField

@Composable
fun SignupScreen(
    navController: NavController,
    authViewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var newpassword by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

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
            modifier = Modifier.align(Alignment.Start).padding(20.dp)
        )

        SimpleTextField(
            value = name,
            onValueChange = { name=it },
            label = "Full Name",

        )
        Spacer(modifier = Modifier.height(16.dp))

        SimpleTextField(
            value = email,
            onValueChange = { email=it },
            label = "Email",

        )
        Spacer(modifier = Modifier.height(16.dp))

        SimpleTextField(
            value = password,
            onValueChange = { password=it },
            label = "Password",
            isPassword = true,

        )
        Spacer(modifier = Modifier.height(16.dp))

        SimpleTextField(
            value = newpassword,
            onValueChange = { newpassword=it },
            label = "Confirm Password",
            isPassword = true,

        )
        Spacer(modifier = Modifier.height(26.dp))
        if (!isLoading) {
            PrimaryButton(
                text = "Create Account",
                onClick = {
                    if (email.isBlank() || password.isBlank() || name.isBlank()) {
                        error = "Please fill all fields"
                    } else {
                        isLoading = true
                        authViewModel.signUp(email, password, name) { success, message ->
                            isLoading = false
                            if (success) {
                                Log.d("SignupScreen", "Signup successful")
                                navController.navigate("profile") {
                                    popUpTo("login") { inclusive = true }
                                }
                            } else {
                                error = message
                            }
                        }
                    }
                },
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = {
                navController.navigate("login")
            }
        ) {
            Text("Already have an account? Login", color = Color.White)
        }

        error?.let {
            Text(
                text = it,
                color = Color.Red,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}