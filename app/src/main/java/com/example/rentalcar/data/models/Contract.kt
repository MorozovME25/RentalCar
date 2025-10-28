package com.example.rentalcar.data.models

import java.time.LocalDateTime

enum class ContractStatus { ACTIVE, COMPLETED }

data class Contract(
    val id: String,
    val userId: String,
    val carId: String? = null,
    val startDateMillis: Long,
    val endDateMillis: Long? = null,
    val initialMileage: Int? = null,
    val finalMileage: Int? = null,
    val totalPrice: Double,
    val details: List<PaymentDetail>,
    val childSeat: Boolean = false,
    val status: ContractStatus = ContractStatus.ACTIVE
)

data class PaymentDetail(
    val description: String,
    val amount: Double,
    val timestamp: Long
)