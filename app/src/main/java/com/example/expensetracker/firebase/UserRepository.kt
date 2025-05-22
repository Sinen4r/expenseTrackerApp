package com.example.expensetracker.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ServerTimestamp
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

class UserRepository {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    val currentUser get() = auth.currentUser

    val userProfileFlow: Flow<UserProfile?> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            firebaseAuth.currentUser?.let { user ->
                db.collection("users").document(user.uid)
                    .addSnapshotListener { snapshot, _ ->
                        trySend(snapshot?.toObject(UserProfile::class.java))
                    }
            } ?: trySend(null)
        }
        auth.addAuthStateListener(authStateListener)

        awaitClose {
            auth.removeAuthStateListener(authStateListener)
        }
    }

    suspend fun signUp(email: String, password: String, name: String): Boolean {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            db.collection("users").document(result.user!!.uid)
                .set(UserProfile(name = name, email = email)).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun signIn(email: String, password: String): Boolean {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun signOut() = auth.signOut()
}

data class UserProfile(
    val name: String = "",
    val email: String = "",
    @ServerTimestamp val joinDate: Date? = null
)