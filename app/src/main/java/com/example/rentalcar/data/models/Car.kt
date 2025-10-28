package com.example.rentalcar.data.models

enum class TransmissionType { MANUAL, AUTOMATIC }
enum class CarStatus { AVAILABLE, OCCUPIED }

data class Car(
    val id: String,
    val brand: String,
    val model: String,
    val year: Int,
    val transmission: TransmissionType,
    val hasAc: Boolean,
    val pricePerDay: Int,
    val status: CarStatus,
    val imageUrl: String = "" //placeholder
)