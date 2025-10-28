package com.example.rentalcar.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.rentalcar.data.models.CarStatus
import com.example.rentalcar.data.models.TransmissionType
import com.example.rentalcar.ui.viewmodel.AppViewModel

@Composable
fun FilterPopup(
    viewModel: AppViewModel,
    onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            tonalElevation = 6.dp
        ) {
            Column(
                modifier = Modifier
                    .width(300.dp)
                    .padding(16.dp)
            ) {
                Text("Фильтры", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(12.dp))
                Text("Ценовой сегмент", style = MaterialTheme.typography.titleMedium)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    FilterChip(
                        selected = "Бюджет" in viewModel.selectedPriceSegments,
                        onClick = { viewModel.togglePriceSegment("Бюджет") },
                        label = { Text("Бюджет") }
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    FilterChip(
                        selected = "Стандарт" in viewModel.selectedPriceSegments,
                        onClick = { viewModel.togglePriceSegment("Стандарт") },
                        label = { Text("Стандарт") }
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    FilterChip(
                        selected = "Премиум" in viewModel.selectedPriceSegments,
                        onClick = { viewModel.togglePriceSegment("Премиум") },
                        label = { Text("Премиум") }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                FilterChip(
                    selected = viewModel.hasAcFilter == true,
                    onClick = { viewModel.toggleAcFilter() },
                    label = { Text("С кондиционером") }
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row {
                    FilterChip(
                        selected = viewModel.transmissionFilter == TransmissionType.MANUAL,
                        onClick = {
                            if (viewModel.transmissionFilter == TransmissionType.MANUAL) {
                                viewModel.transmissionFilter = null
                            } else {
                                viewModel.transmissionFilter = TransmissionType.MANUAL
                            }
                        },
                        label = { Text("Механика") }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    FilterChip(
                        selected = viewModel.transmissionFilter == TransmissionType.AUTOMATIC,
                        onClick = {
                            if (viewModel.transmissionFilter == TransmissionType.AUTOMATIC) {
                                viewModel.transmissionFilter = null
                            } else {
                                viewModel.transmissionFilter = TransmissionType.AUTOMATIC
                            }
                        },
                        label = { Text("Автомат") }
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row {
                    FilterChip(
                        selected = viewModel.statusFilter == CarStatus.AVAILABLE,
                        onClick = {
                            if (viewModel.statusFilter == CarStatus.AVAILABLE) {
                                viewModel.statusFilter = null
                            } else {
                                viewModel.statusFilter = CarStatus.AVAILABLE
                            }
                        },
                        label = { Text("Свободно") }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    FilterChip(
                        selected = viewModel.statusFilter == CarStatus.OCCUPIED,
                        onClick = {
                            if (viewModel.statusFilter == CarStatus.OCCUPIED) {
                                viewModel.statusFilter = null
                            } else {
                                viewModel.statusFilter = CarStatus.OCCUPIED
                            }
                        },
                        label = { Text("Занято") }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    Button(onClick = {
                        viewModel.clearFilters()
                    }) {
                        Text("Сбросить")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(onClick = onDismiss) {
                        Text("Готово")
                    }
                }
            }
        }
    }
}