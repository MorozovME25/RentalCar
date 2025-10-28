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
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.rentalcar.data.models.Car
import com.example.rentalcar.data.models.CarStatus
import com.example.rentalcar.data.models.TransmissionType

@Composable
fun CarItem(
    car: Car,
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
                Text("${car.brand} ${car.model}", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = if (isExpanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                    contentDescription = null
                )
            }

            if (isExpanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Год выпуска: ${car.year}")
                Text("Коробка: ${if (car.transmission == TransmissionType.AUTOMATIC) "Автомат" else "Механика"}")
                Text("Кондиционер: ${if (car.hasAc) "Есть" else "Нет"}")
                Text("Цена: ${car.pricePerDay} ₽/сутки")
                Text(
                    text = if (car.status == CarStatus.AVAILABLE) "Свободно" else "Занято",
                    color = if (car.status == CarStatus.AVAILABLE) Color.Green else Color.Red
                )
            }
        }
    }
}
