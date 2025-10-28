package com.example.rentalcar.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.rentalcar.data.TestData
import com.example.rentalcar.data.models.Contract
import com.example.rentalcar.ui.formatTimestamp

@Composable
fun ContractItem(
    contract: Contract,
    isExpanded: Boolean,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onToggle() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Договор №${contract.id}", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = if (isExpanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                    contentDescription = null
                )
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(8.dp))
                contract.carId?.let {
                    val car = TestData.cars.find { c -> c.id == it }
                    Text("Машина: ${car?.brand} ${car?.model} (${car?.year})")
                } ?: Text("Машина: не назначена")

                Text("Пробег: начальный ${contract.initialMileage ?: "—"}, конечный ${contract.finalMileage ?: "—"}")
                Text("Итого: ${contract.totalPrice} ₽")

                contract.details.forEach { detail ->
                    Text("${detail.description}: ${detail.amount} ₽ (${formatTimestamp(detail.timestamp)})")
                }
            }
        }
    }
}