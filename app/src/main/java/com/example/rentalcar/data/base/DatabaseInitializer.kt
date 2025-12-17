package com.example.rentalcar.data.base

import com.example.rentalcar.data.models.Car
import com.example.rentalcar.data.models.CarStatus
import com.example.rentalcar.data.models.TransmissionType
import com.example.rentalcar.data.models.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

object DatabaseInitializer {
    fun initializeTestData() {
        val db = FirebaseFirestore.getInstance()
        val scope = CoroutineScope(Dispatchers.IO)

        // Инициализация пользователей
        val users = listOf(
            User(
                id = "", // Будет сгенерирован при регистрации
                fullName = "Иван Иванов",
                email = "ivan@example.com",
                phone = "+79991234567",
                birthDate = "01.01.1990",
                password = "password123", // В реальности пароли хранятся в хэшированном виде
                deposit = 10000.0,
                isConsentGiven = true
            ),
            User(
                id = "",
                fullName = "Мария Петрова",
                email = "maria@example.com",
                phone = "+79997654321",
                birthDate = "15.05.1985",
                password = "securepass",
                deposit = 5000.0,
                isConsentGiven = true
            )
        )

        // Инициализация машин
        scope.launch {
            try {
                // Инициализация машин
                val cars = listOf(
                    Car("1", "BMW", "X5", 2022, TransmissionType.AUTOMATIC, true, 7000, CarStatus.AVAILABLE, ""),
                    Car("2", "Toyota", "Camry", 2020, TransmissionType.AUTOMATIC, true, 4000, CarStatus.OCCUPIED, ""),
                    Car("3", "Lada", "Granta", 2019, TransmissionType.MANUAL, false, 2000, CarStatus.AVAILABLE, ""),
                    Car("4", "Mercedes", "E-Class", 2023, TransmissionType.AUTOMATIC, true, 8000, CarStatus.AVAILABLE, ""),
                    Car("5", "Kia", "Rio", 2021, TransmissionType.MANUAL, true, 3500, CarStatus.AVAILABLE, "")
                )

                // Очистка и заполнение коллекции cars
                val carsCollection = db.collection("cars").get().await()
                for (document in carsCollection.documents) {
                    db.collection("cars").document(document.id).delete().await()
                }

                for (car in cars) {
                    db.collection("cars").document(car.id).set(car).await()
                }

                println("Тестовые данные успешно инициализированы!")
            } catch (e: Exception) {
                println("Ошибка инициализации данных: ${e.message}")
            }
        }

        println("Тестовые данные успешно инициализированы!")
    }
}