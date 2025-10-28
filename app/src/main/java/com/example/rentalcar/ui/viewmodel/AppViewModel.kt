package com.example.rentalcar.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.rentalcar.data.TestData
import com.example.rentalcar.data.models.Car
import com.example.rentalcar.data.models.CarStatus
import com.example.rentalcar.data.models.Contract
import com.example.rentalcar.data.models.ContractStatus
import com.example.rentalcar.data.models.TransmissionType
import com.example.rentalcar.data.models.User

class AppViewModel : ViewModel() {
    var isAuthenticated by mutableStateOf(false)
        private set

    var currentUser by mutableStateOf<User?>(null)
        private set

    var expandedCarId by mutableStateOf<String?>(null)
        private set

    var selectedTab by mutableStateOf("active")
        private set

    var showFilters by mutableStateOf(false)
        private set

    var selectedPriceSegments by mutableStateOf<Set<String>>(emptySet())
        private set

    var hasAcFilter by mutableStateOf<Boolean?>(null)
        private set

    var transmissionFilter by mutableStateOf<TransmissionType?>(null)

    var statusFilter by mutableStateOf<CarStatus?>(null)

    fun togglePriceSegment(segment: String) {
        selectedPriceSegments = if (selectedPriceSegments.contains(segment)) {
            selectedPriceSegments - segment
        } else {
            selectedPriceSegments + segment
        }
    }

    fun toggleAcFilter() {
        hasAcFilter = when (hasAcFilter) {
            true -> false
            false -> null
            null -> true
        }
    }

    fun toggleTransmission() {
        transmissionFilter = when (transmissionFilter) {
            TransmissionType.MANUAL -> TransmissionType.AUTOMATIC
            TransmissionType.AUTOMATIC -> null
            null -> TransmissionType.MANUAL
        }
    }

    fun toggleStatus() {
        statusFilter = when (statusFilter) {
            CarStatus.AVAILABLE -> CarStatus.OCCUPIED
            CarStatus.OCCUPIED -> null
            null -> CarStatus.AVAILABLE
        }
    }

    fun clearFilters() {
        selectedPriceSegments = emptySet()
        hasAcFilter = null
        transmissionFilter = null
        statusFilter = null
    }

    fun toggleFilters() {
        showFilters = !showFilters
    }

    // --- Фильтрация машин ---
    val filteredCars: List<Car>
        get() {
            var result = TestData.cars.toMutableList()

            if (selectedPriceSegments.isNotEmpty()) {
                result = result.filter { car ->
                    val segment = when {
                        car.pricePerDay < 3000 -> "Бюджет"
                        car.pricePerDay <= 6000 -> "Стандарт"
                        else -> "Премиум"
                    }
                    segment in selectedPriceSegments
                }.toMutableList()
            }

            if (hasAcFilter != null) {
                result = result.filter { it.hasAc == hasAcFilter }.toMutableList()
            }

            if (transmissionFilter != null) {
                result = result.filter { it.transmission == transmissionFilter }.toMutableList()
            }

            if (statusFilter != null) {
                result = result.filter { it.status == statusFilter }.toMutableList()
            }

            return result.sortedByDescending { it.pricePerDay }
        }

    fun login(email: String, password: String) {
        val user = TestData.users.values.find { it.email == email && it.password == password }
        if (user != null) {
            currentUser = user
            isAuthenticated = true
        }
    }

    fun register(user: User) {
        TestData.users[user.email] = user
        currentUser = user
        isAuthenticated = true
    }

    fun logout() {
        currentUser = null
        isAuthenticated = false
    }

    fun toggleCarExpanded(carId: String) {
        expandedCarId = if (expandedCarId == carId) null else carId
    }

    fun selectTab(tab: String) {
        selectedTab = tab
    }

    fun addContract(contract: Contract) {
        TestData.contracts.add(contract)
    }

    fun getContractsByStatus(status: ContractStatus) =
        TestData.contracts.filter { it.status == status && it.userId == currentUser?.id }
}