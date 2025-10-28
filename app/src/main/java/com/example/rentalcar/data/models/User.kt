package com.example.rentalcar.data.models

data class User(
    val id: String,
    val fullName: String,
    val email: String,
    val phone: String,
    val birthDate: String,
    val password: String,
    val deposit: Double = 0.0,
    val isConsentGiven: Boolean = true
)