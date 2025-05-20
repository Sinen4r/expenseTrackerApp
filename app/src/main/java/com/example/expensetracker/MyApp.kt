package com.example.expensetracker

import android.app.Application
import android.util.Log
import com.google.firebase.FirebaseApp

class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        try {
            FirebaseApp.initializeApp(this)
            Log.d("FirebaseInit", "Initialized successfully")
        } catch (e: Exception) {
            Log.e("FirebaseInit", "Initialization failed", e)
        }
    }
}