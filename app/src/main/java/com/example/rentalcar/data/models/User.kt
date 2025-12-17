package com.example.rentalcar.data.models

import android.annotation.SuppressLint
import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties

@Keep
@IgnoreExtraProperties
@SuppressLint("ParcelCreator")
data class User(
    val id: String = "",
    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val birthDate: String = "",
    val password: String = "", // В реальности не хранится в Firestore
    val deposit: Double = 0.0,
    val isConsentGiven: Boolean = false
)