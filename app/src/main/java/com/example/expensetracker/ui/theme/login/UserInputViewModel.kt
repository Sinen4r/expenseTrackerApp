package com.example.expensetracker.ui.theme.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.expensetracker.ui.theme.components.TextFieldState

class UserInputViewModel : ViewModel() {
    private val _emailState = mutableStateOf(TextFieldState())
    val emailState: State<TextFieldState> = _emailState

    private val _passwordState = mutableStateOf(TextFieldState())
    val passwordState: State<TextFieldState> = _passwordState

    fun onEmailChange(newValue: String) {
        val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
        _emailState.value = _emailState.value.copy(
            text = newValue,
            error = if (newValue.isEmpty()) "Email cannot be empty" else if (!emailRegex.matches(newValue)) "Invalid email" else null
        )
    }

    fun onPasswordChange(newValue: String) {
        _passwordState.value = _passwordState.value.copy(
            text = newValue,
            error = if (newValue.isEmpty()) "Password cannot be empty" else if (newValue.length < 6) "Password must be at least 6 characters" else null
        )
    }

    fun validateLogin(): Boolean {
        return emailState.value.isValid() && passwordState.value.isValid()
    }

    fun performLogin() {
        if (validateLogin()) {
            // Perform login (e.g., call an API or authenticate with a backend)
            // For now, just log or handle success
            println("Login successful with email: ${emailState.value.text}")
        } else {
            // Update states to show errors if not already set
            onEmailChange(emailState.value.text)
            onPasswordChange(passwordState.value.text)
        }
    }

    fun performSignup() {
        if (validateLogin()) {
            // Perform signup (e.g., register user in backend)
            println("Signup successful with email: ${emailState.value.text}")
        } else {
            onEmailChange(emailState.value.text)
            onPasswordChange(passwordState.value.text)
        }
    }
}