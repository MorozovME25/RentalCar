package com.example.rentalcar.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.rentalcar.ui.screens.AuthScreen
import com.example.rentalcar.ui.screens.MainScreen
import com.example.rentalcar.ui.screens.ProfileScreen
import com.example.rentalcar.ui.screens.RegisterScreen
import com.example.rentalcar.ui.screens.RentalRequestScreen
import com.example.rentalcar.ui.viewmodel.AppViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AutoRentalApp(
    modifier: Modifier = Modifier,
    viewModel: AppViewModel = viewModel()
) {
    val context = LocalContext.current
    var currentScreen by remember { mutableStateOf("main") }

    BackHandler(enabled = currentScreen == "auth" || currentScreen == "register") {
        currentScreen = "main"
    }

    Column(modifier = modifier) {
        when (currentScreen) {
            "auth" -> AuthScreen(
                onLogin = { email, password ->
                    viewModel.login(email, password)
                    if (viewModel.isAuthenticated) currentScreen = "main"
                },
                onNavigateToRegister = { currentScreen = "register" }
            )

            "register" -> RegisterScreen(
                onRegister = { user ->
                    viewModel.register(user)
                    currentScreen = "main"
                },
                onBack = { currentScreen = "auth" }
            )

            "main" -> MainScreen(
                viewModel = viewModel,
                onNavigateToProfile = {
                    if (viewModel.isAuthenticated) {
                        currentScreen = "profile"
                    } else {
                        currentScreen = "auth"
                    }
                },                onNavigateToLogin = { currentScreen = "auth" },
                onNavigateToRegister = { currentScreen = "register" },
                onCarClick = { carId -> viewModel.toggleCarExpanded(carId) }
            )

            "profile" -> ProfileScreen(
                viewModel = viewModel,
                onLogout = {
                    viewModel.logout()
                    currentScreen = "main"
                },
                onNavigateToRental = { currentScreen = "rental" },
                onBack = { currentScreen = "main" }
            )

            "rental" -> RentalRequestScreen(
                viewModel = viewModel,
                onBack = { currentScreen = "profile" },
                onSubmitted = {
                    android.widget.Toast.makeText(
                        context,
                        "Ваша заявка принята на рассмотрение",
                        android.widget.Toast.LENGTH_SHORT
                    ).show()
                    currentScreen = "main"
                }
            )
        }
    }
}