package com.example.rentalcar.data.base

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.multidex.MultiDex
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.storage.FirebaseStorage

@Suppress("DEPRECATION")
class RentalCarApplication : Application() {
    companion object {
        const val TAG = "RentalCarApp"
    }

//    override fun attachBaseContext(base: Context) {
//        super.attachBaseContext(base)
//        MultiDex.install(this)
//    }

    override fun onCreate() {
        super.onCreate()

        try {
            setupFirestore()
            Log.d(TAG, "Firestore initialized successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error: ${e.message}", e)
        }
    }

    private fun setupFirestore() {
        FirebaseFirestore.setLoggingEnabled(true)

        val db = FirebaseFirestore.getInstance()
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
        db.firestoreSettings = settings
    }
}