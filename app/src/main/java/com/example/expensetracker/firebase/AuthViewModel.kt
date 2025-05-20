package com.example.expensetracker.firebase

import androidx.activity.result.launch
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
class AuthViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()

    fun signIn(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            try {
                auth.signInWithEmailAndPassword(email, password).await()
                onComplete(true, null)
            } catch (e: Exception) {
                onComplete(false, e.message ?: "Login failed")
            }
        }
    }

    fun signUp(email: String, password: String, name: String, onComplete: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            try {
                val result = auth.createUserWithEmailAndPassword(email, password).await()
                FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(result.user!!.uid)
                    .set(mapOf(
                        "name" to name,
                        "email" to email,
                        "joinDate" to FieldValue.serverTimestamp()
                    ))
                onComplete(true, null)
            } catch (e: Exception) {
                onComplete(false, e.message ?: "Signup failed")
            }
        }
    }

    fun signOut() {
        auth.signOut()
    }

    val currentUser = auth.currentUser
}