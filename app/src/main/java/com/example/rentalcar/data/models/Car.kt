package com.example.rentalcar.data.models

import android.annotation.SuppressLint
import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties

enum class TransmissionType { MANUAL, AUTOMATIC }
enum class CarStatus { AVAILABLE, OCCUPIED }

@Keep
@IgnoreExtraProperties
@SuppressLint("ParcelCreator")
data class Car(
    val id: String = "",
    val brand: String = "",
    val model: String = "",
    val year: Int = 0,
    val transmission: TransmissionType = TransmissionType.AUTOMATIC,
    val hasAc: Boolean = false,
    val pricePerDay: Int = 0,
    val status: CarStatus = CarStatus.AVAILABLE,
    val imageUrl: String = ""
)