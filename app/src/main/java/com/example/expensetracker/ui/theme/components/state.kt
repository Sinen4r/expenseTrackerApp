package com.example.expensetracker.ui.theme.components

data class TextFieldState(
    val text: String = "",
    val error: String? = null
) {
    fun isValid(): Boolean = error == null
}