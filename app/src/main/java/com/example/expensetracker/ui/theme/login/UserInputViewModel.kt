package com.example.expensetracker.ui.theme.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

data class TextFieldState(
    val text: String = "",
    val error: String? = null
) {
    fun isValid(): Boolean = text.isNotEmpty() && error == null
}

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val message: String) : LoginState()
    data class Error(val message: String) : LoginState()
}

class UserInputViewModel : ViewModel() {
    private val _emailState = mutableStateOf(TextFieldState())
    val emailState: State<TextFieldState> = _emailState

    private val _passwordState = mutableStateOf(TextFieldState())
    val passwordState: State<TextFieldState> = _passwordState

    private val _loginState = mutableStateOf<LoginState>(LoginState.Idle)
    val loginState: State<LoginState> = _loginState

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

    private fun validateInputs(): Boolean {
        // Re-validate to ensure errors are up-to-date
        onEmailChange(_emailState.value.text)
        onPasswordChange(_passwordState.value.text)
        return _emailState.value.isValid() && _passwordState.value.isValid()
    }

    fun performLogin() {
        _loginState.value = LoginState.Loading
        if (validateInputs()) {
            // Simulate API call (replace with actual backend call)
            try {
                Thread.sleep(1000) // Simulate network delay
                _loginState.value = LoginState.Success("Login successful with email: ${_emailState.value.text}")
            } catch (e: Exception) {
                _loginState.value = LoginState.Error("Login failed: ${e.message}")
            }
        } else {
            _loginState.value = LoginState.Error("Please fix the errors above")
        }
    }



    fun resetLoginState() {
        _loginState.value = LoginState.Idle
    }
}