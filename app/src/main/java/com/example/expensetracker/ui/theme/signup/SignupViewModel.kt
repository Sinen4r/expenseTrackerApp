package com.example.expensetracker.ui.theme.signup

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

data class SignupFieldState(
    val text: String = "",
    val error: String? = null
) {
    fun isValid(): Boolean = text.isNotEmpty() && error == null
}

sealed class SignupState {
    object Idle : SignupState()
    object Loading : SignupState()
    data class Success(val message: String) : SignupState()
    data class Error(val message: String) : SignupState()
}

class SignupViewModel : ViewModel() {
    private val _emailState = mutableStateOf(SignupFieldState())
    val emailState: State<SignupFieldState> = _emailState

    private val _passwordState = mutableStateOf(SignupFieldState())
    val passwordState: State<SignupFieldState> = _passwordState

    private val _confirmPasswordState = mutableStateOf(SignupFieldState())
    val confirmPasswordState: State<SignupFieldState> = _confirmPasswordState

    private val _nameState = mutableStateOf(SignupFieldState())
    val nameState: State<SignupFieldState> = _nameState

    private val _signupState = mutableStateOf<SignupState>(SignupState.Idle)
    val signupState: State<SignupState> = _signupState

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
            error = if (newValue.isEmpty()) "Password cannot be empty"
            else if (newValue.length < 6) "Password must be at least 6 characters"
            else null
        )

        // Also validate confirm password when password changes
        if (_confirmPasswordState.value.text.isNotEmpty()) {
            validatePasswordMatch(_confirmPasswordState.value.text)
        }
    }

    fun onConfirmPasswordChange(newValue: String) {
        validatePasswordMatch(newValue)
    }

    private fun validatePasswordMatch(confirmPassword: String) {
        _confirmPasswordState.value = _confirmPasswordState.value.copy(
            text = confirmPassword,
            error = if (confirmPassword.isEmpty()) "Confirm password cannot be empty"
            else if (confirmPassword != _passwordState.value.text) "Passwords do not match"
            else null
        )
    }

    fun onNameChange(newValue: String) {
        _nameState.value = _nameState.value.copy(
            text = newValue,
            error = if (newValue.isEmpty()) "Name cannot be empty" else null
        )
    }

    private fun validateInputs(): Boolean {
        // Re-validate to ensure errors are up-to-date
        onEmailChange(_emailState.value.text)
        onPasswordChange(_passwordState.value.text)
        onConfirmPasswordChange(_confirmPasswordState.value.text)
        onNameChange(_nameState.value.text)

        return _emailState.value.isValid() &&
                _passwordState.value.isValid() &&
                _confirmPasswordState.value.isValid() &&
                _nameState.value.isValid()
    }

    fun performSignup() {
        _signupState.value = SignupState.Loading
        if (validateInputs()) {
            // Simulate API call (replace with actual backend call)
            try {
                Thread.sleep(1000) // Simulate network delay
                _signupState.value = SignupState.Success("Account created successfully for ${_nameState.value.text}")
            } catch (e: Exception) {
                _signupState.value = SignupState.Error("Signup failed: ${e.message}")
            }
        } else {
            _signupState.value = SignupState.Error("Please fix the errors above")
        }
    }

    fun resetSignupState() {
        _signupState.value = SignupState.Idle
    }
}