package com.example.rentalcar.data.models

import android.annotation.SuppressLint
import androidx.annotation.Keep
import com.google.firebase.firestore.IgnoreExtraProperties

enum class ContractStatus { ACTIVE, COMPLETED }

@Keep
@IgnoreExtraProperties
@SuppressLint("ParcelCreator")
data class Contract(
    val id: String = "",
    val userId: String = "",
    val passportImageUri: String? = null,
    val carId: String? = null,
    val startDateMillis: Long = 0,
    val endDateMillis: Long? = null,
    val initialMileage: Int? = null,
    val finalMileage: Int? = null,
    val totalPrice: Double = 0.0,
    val details: List<PaymentDetail> = emptyList(),
    val childSeat: Boolean = false,
    val status: ContractStatus = ContractStatus.ACTIVE
)

@Keep
@IgnoreExtraProperties
data class PaymentDetail(
    val description: String = "",
    val amount: Double = 0.0,
    val timestamp: Long = 0
)