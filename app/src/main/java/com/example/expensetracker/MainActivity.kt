package com.example.expensetracker

import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.expensetracker.firebase.AuthViewModel
import com.example.expensetracker.ui.theme.ExpenseTrackerTheme
import com.example.expensetracker.ui.theme.stats.StatsViewModel
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class MainActivity : ComponentActivity() {
    private val viewModel: StatsViewModel by viewModels()
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_NO_TITLE)
        installSplashScreen()
        testAuth()
        super.onCreate(savedInstanceState)

        setContent {
            ExpenseTrackerTheme { // Your theme (defined in Theme.kt)
                ExpenseTrackerApp(viewModel,authViewModel)
            }
        }
    }
    // Temporary test function in your MainActivity
    fun testAuth() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val auth = Firebase.auth
                auth.signInWithEmailAndPassword("real_test@example.com", "StrongPassword123!").await()
                Log.d("AuthTest", "Success! User: ${auth.currentUser?.uid}")
            } catch (e: Exception) {
                Log.e("AuthTest", "Failed", e)
            }
        }
    }
}