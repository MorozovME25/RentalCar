package com.example.rentalcar.ui.viewmodel

//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.setValue
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.rentalcar.data.TestData
//import com.example.rentalcar.data.models.Car
//import com.example.rentalcar.data.models.CarStatus
//import com.example.rentalcar.data.models.Contract
//import com.example.rentalcar.data.models.ContractStatus
//import com.example.rentalcar.data.models.TransmissionType
//import com.example.rentalcar.data.models.User
//import com.google.firebase.auth.FirebaseAuth
//import com.google.firebase.firestore.FirebaseFirestore
//import kotlinx.coroutines.launch
//import kotlinx.coroutines.tasks.await


import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rentalcar.data.TestData
import com.example.rentalcar.data.models.Car
import com.example.rentalcar.data.models.CarStatus
import com.example.rentalcar.data.models.Contract
import com.example.rentalcar.data.models.ContractStatus
import com.example.rentalcar.data.models.TransmissionType
import com.example.rentalcar.data.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AppViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    var isAuthenticated by mutableStateOf(false)
        private set

    var currentUser by mutableStateOf<User?>(null)
        private set

    var expandedCarId by mutableStateOf<String?>(null)
        private set

    var showFilters by mutableStateOf(false)
        private set

    var selectedPriceSegments by mutableStateOf<Set<String>>(emptySet())
        private set

    var hasAcFilter by mutableStateOf<Boolean?>(null)
        private set

    var transmissionFilter by mutableStateOf<TransmissionType?>(null)

    var statusFilter by mutableStateOf<CarStatus?>(null)

    var isLoading by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    private var errorClearJob: Job? = null

    init {
        checkAuthState()
        loadCarsFromFirebase()
    }

    private fun checkAuthState() {
        auth.currentUser?.let { firebaseUser ->
            viewModelScope.launch {
                loadUserData(firebaseUser)
            }
        }
    }

    private suspend fun loadUserData(firebaseUser: FirebaseUser) {
        try {
            val userDoc = db.collection("users").document(firebaseUser.uid).get().await()
            if (userDoc.exists()) {
                val user = userDoc.toObject(User::class.java)?.copy(id = firebaseUser.uid)
                currentUser = user
                isAuthenticated = true
            }
        } catch (e: Exception) {
            handleError("Ошибка загрузки пользователя: ${e.message}")
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            isLoading = true
            error = null
            try {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                result.user?.let { firebaseUser ->
                    loadUserData(firebaseUser)
                }
            } catch (e: Exception) {
                handleError("Ошибка входа: ${e.message ?: "Неизвестная ошибка"}")
            } finally {
                isLoading = false
            }
        }
    }

    fun register(user: User) {
        viewModelScope.launch {
            isLoading = true
            error = null
            try {
                // Создаем пользователя в Firebase Auth
                val authResult = auth.createUserWithEmailAndPassword(
                    user.email,
                    user.password
                ).await()

                authResult.user?.let { firebaseUser ->
                    // Сохраняем данные пользователя в Firestore
                    val userToSave = user.copy(
                        id = firebaseUser.uid,
                        // Не сохраняем пароль в Firestore!
                        password = ""
                    )

                    db.collection("users").document(firebaseUser.uid)
                        .set(userToSave)
                        .await()

                    currentUser = userToSave.copy(password = user.password) // Для локального использования
                    isAuthenticated = true

                    // Инициализируем депозит
                    updateUserDeposit(firebaseUser.uid, 0.0)
                }
            } catch (e: Exception) {
                handleError("Ошибка регистрации: ${e.message ?: "Неизвестная ошибка"}")
            } finally {
                isLoading = false
            }
        }
    }

    fun logout() {
        auth.signOut()
        currentUser = null
        isAuthenticated = false
    }

    private fun handleError(message: String?) {
        error = message

        // Отменяем предыдущую задачу очистки ошибки
        errorClearJob?.cancel()

        // Запускаем новую задачу очистки через 5 секунд
        errorClearJob = viewModelScope.launch {
            delay(5000)
            if (error == message) {
                error = null
            }
        }
    }

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

    fun clearFilters() {
        selectedPriceSegments = emptySet()
        hasAcFilter = null
        transmissionFilter = null
        statusFilter = null
    }

    fun toggleFilters() {
        showFilters = !showFilters
    }

    fun toggleCarExpanded(carId: String) {
        expandedCarId = if (expandedCarId == carId) null else carId
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

    // --- Работа с Firebase ---
    private fun loadCarsFromFirebase() {
        viewModelScope.launch {
            try {
                val carsQuery = db.collection("cars").get().await()
                val cars = carsQuery.toObjects(Car::class.java)

                // Обновляем тестовые данные для совместимости
                TestData.cars = cars
            } catch (e: Exception) {
                handleError("Ошибка загрузки машин: ${e.message ?: "Неизвестная ошибка"}")
            }
        }
    }

    fun getContractsByStatus(status: ContractStatus): List<Contract> {
        return TestData.contracts.filter { it.status == status && it.userId == currentUser?.id }
    }

    fun addContract(contract: Contract) {
        viewModelScope.launch {
            try {
                // Генерируем новый ID для контракта
                val contractId = db.collection("contracts").document().id
                val contractToSave = contract.copy(id = contractId)

                db.collection("contracts").document(contractId)
                    .set(contractToSave)
                    .await()

                // Обновляем локальные данные
                TestData.contracts.add(contractToSave)
            } catch (e: Exception) {
                handleError("Ошибка создания контракта: ${e.message ?: "Неизвестная ошибка"}")
            }
        }
    }

    fun updateUserDeposit(userId: String, amount: Double) {
        viewModelScope.launch {
            try {
                db.collection("users").document(userId)
                    .update("deposit", amount)
                    .await()

                currentUser?.let { user ->
                    currentUser = user.copy(deposit = amount)
                }
            } catch (e: Exception) {
                handleError("Ошибка обновления депозита: ${e.message ?: "Неизвестная ошибка"}")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        errorClearJob?.cancel()
    }
}

//class AppViewModel : ViewModel() {
//    var isAuthenticated by mutableStateOf(false)
//        private set
//
//    var currentUser by mutableStateOf<User?>(null)
//        private set
//
//    var expandedCarId by mutableStateOf<String?>(null)
//        private set
//
//    var selectedTab by mutableStateOf("active")
//        private set
//
//    var showFilters by mutableStateOf(false)
//        private set
//
//    var selectedPriceSegments by mutableStateOf<Set<String>>(emptySet())
//        private set
//
//    var hasAcFilter by mutableStateOf<Boolean?>(null)
//        private set
//
//    var transmissionFilter by mutableStateOf<TransmissionType?>(null)
//
//    var statusFilter by mutableStateOf<CarStatus?>(null)
//
//    fun togglePriceSegment(segment: String) {
//        selectedPriceSegments = if (selectedPriceSegments.contains(segment)) {
//            selectedPriceSegments - segment
//        } else {
//            selectedPriceSegments + segment
//        }
//    }
//
//    fun toggleAcFilter() {
//        hasAcFilter = when (hasAcFilter) {
//            true -> false
//            false -> null
//            null -> true
//        }
//    }
//
//    fun toggleTransmission() {
//        transmissionFilter = when (transmissionFilter) {
//            TransmissionType.MANUAL -> TransmissionType.AUTOMATIC
//            TransmissionType.AUTOMATIC -> null
//            null -> TransmissionType.MANUAL
//        }
//    }
//
//    fun toggleStatus() {
//        statusFilter = when (statusFilter) {
//            CarStatus.AVAILABLE -> CarStatus.OCCUPIED
//            CarStatus.OCCUPIED -> null
//            null -> CarStatus.AVAILABLE
//        }
//    }
//
//    fun clearFilters() {
//        selectedPriceSegments = emptySet()
//        hasAcFilter = null
//        transmissionFilter = null
//        statusFilter = null
//    }
//
//    fun toggleFilters() {
//        showFilters = !showFilters
//    }
//
//    // --- Фильтрация машин ---
//    val filteredCars: List<Car>
//        get() {
//            var result = TestData.cars.toMutableList()
//
//            if (selectedPriceSegments.isNotEmpty()) {
//                result = result.filter { car ->
//                    val segment = when {
//                        car.pricePerDay < 3000 -> "Бюджет"
//                        car.pricePerDay <= 6000 -> "Стандарт"
//                        else -> "Премиум"
//                    }
//                    segment in selectedPriceSegments
//                }.toMutableList()
//            }
//
//            if (hasAcFilter != null) {
//                result = result.filter { it.hasAc == hasAcFilter }.toMutableList()
//            }
//
//            if (transmissionFilter != null) {
//                result = result.filter { it.transmission == transmissionFilter }.toMutableList()
//            }
//
//            if (statusFilter != null) {
//                result = result.filter { it.status == statusFilter }.toMutableList()
//            }
//
//            return result.sortedByDescending { it.pricePerDay }
//        }
//
//    fun login(email: String, password: String) {
//        val user = TestData.users.values.find { it.email == email && it.password == password }
//        if (user != null) {
//            currentUser = user
//            isAuthenticated = true
//        }
//    }
//
//    fun register(user: User) {
//        TestData.users[user.email] = user
//        currentUser = user
//        isAuthenticated = true
//    }
//
//    fun logout() {
//        currentUser = null
//        isAuthenticated = false
//    }
//
//    fun toggleCarExpanded(carId: String) {
//        expandedCarId = if (expandedCarId == carId) null else carId
//    }
//
//    fun selectTab(tab: String) {
//        selectedTab = tab
//    }
//
//    fun addContract(contract: Contract) {
//        TestData.contracts.add(contract)
//    }
//
//    fun getContractsByStatus(status: ContractStatus) =
//        TestData.contracts.filter { it.status == status && it.userId == currentUser?.id }
//}