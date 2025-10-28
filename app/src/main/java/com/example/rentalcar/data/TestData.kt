package com.example.rentalcar.data

import com.example.rentalcar.data.models.Car
import com.example.rentalcar.data.models.CarStatus
import com.example.rentalcar.data.models.Contract
import com.example.rentalcar.data.models.TransmissionType
import com.example.rentalcar.data.models.User

object TestData {
    val cars = listOf(
        Car("1", "BMW", "X5", 2022, TransmissionType.AUTOMATIC, true, 7000, CarStatus.AVAILABLE),
        Car("2", "Toyota", "Camry", 2020, TransmissionType.AUTOMATIC, true, 4000, CarStatus.OCCUPIED),
        Car("3", "Lada", "Granta", 2019, TransmissionType.MANUAL, false, 2000, CarStatus.AVAILABLE),
        Car("4", "Mercedes", "E-Class", 2023, TransmissionType.AUTOMATIC, true, 8000, CarStatus.AVAILABLE),
        Car("5", "Kia", "Rio", 2021, TransmissionType.MANUAL, true, 3500, CarStatus.AVAILABLE)
    )

    val users = mutableMapOf<String, User>()
    var currentUser: User? = null

    val contracts = mutableListOf<Contract>()
}