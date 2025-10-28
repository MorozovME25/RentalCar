package com.example.rentalcar.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.rentalcar.data.TestData
import com.example.rentalcar.ui.components.CarItem
import com.example.rentalcar.ui.components.FilterPopup
import com.example.rentalcar.ui.viewmodel.AppViewModel

@Composable
fun MainScreen(
    viewModel: AppViewModel,
    onNavigateToProfile: () -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToRegister: () -> Unit,
    onCarClick: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                if (viewModel.isAuthenticated) {
                    TextButton(onClick = onNavigateToProfile) {
                        Text("Личный кабинет")
                    }
                } else {
                    Button(onClick = onNavigateToLogin) { Text("Войти") }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = onNavigateToRegister) { Text("Регистрация") }
                }
            }

            val activeFilterCount = viewModel.selectedPriceSegments.size +
                    (if (viewModel.hasAcFilter != null) 1 else 0) +
                    (if (viewModel.transmissionFilter != null) 1 else 0) +
                    (if (viewModel.statusFilter != null) 1 else 0)

            Button(
                onClick = { viewModel.toggleFilters() },
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            ) {
                Text("Фильтры${if (activeFilterCount > 0) " ($activeFilterCount)" else ""}")
            }

            LazyColumn {
                items(
                    items = viewModel.filteredCars,
                    key = { it.id }
                ) { car ->
                    CarItem(
                        car = car,
                        isExpanded = viewModel.expandedCarId == car.id,
                        onToggle = { onCarClick(car.id) }
                    )
                }
            }
        }

        if (viewModel.showFilters) {
            FilterPopup(viewModel = viewModel, onDismiss = { viewModel.toggleFilters() })
        }
    }
}