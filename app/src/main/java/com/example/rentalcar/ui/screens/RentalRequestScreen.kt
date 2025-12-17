package com.example.rentalcar.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.rentalcar.data.TestData
import com.example.rentalcar.data.models.Contract
import com.example.rentalcar.data.models.ContractStatus
import com.example.rentalcar.ui.viewmodel.AppViewModel
import java.time.LocalDateTime

@Composable
fun RentalRequestScreen(
    viewModel: AppViewModel,
    onBack: () -> Unit,
    onSubmitted: () -> Unit
) {
    val user = viewModel.currentUser ?: return

    var passportImageUri by remember { mutableStateOf<Uri?>(null) }

    var priceRange by remember { mutableStateOf("Бюджет") }
    var childSeat by remember { mutableStateOf(false) }
    var date by remember { mutableStateOf("") }
    var time by remember { mutableStateOf("") }
    var consent by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        passportImageUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        OutlinedTextField(value = user.fullName, onValueChange = {}, enabled = false, label = { Text("ФИО" ) })
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = user.phone, onValueChange = {}, enabled = false, label = { Text("Телефон") })
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            launcher.launch("image/*")
        }) {
            Text("Прикрепить паспорт")
        }
        Spacer(modifier = Modifier.height(16.dp))

        DropdownMenuExample(priceRange) { priceRange = it }
        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = childSeat, onCheckedChange = { childSeat = it })
            Text("Детское кресло")
        }
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(value = date, onValueChange = { date = it }, label = { Text("Дата аренды") })
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = time, onValueChange = { time = it }, label = { Text("Время (ЧЧ:ММ)") })
        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = consent, onCheckedChange = { consent = it })
            Text("Согласие на обработку персональных данных")
        }
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            enabled = consent,
            onClick = {
                val newContract = Contract(
                    id = (TestData.contracts.size + 1).toString(),
                    userId = user.id,
                    startDateMillis = System.currentTimeMillis(),
                    passportImageUri = passportImageUri?.toString(),
                    endDateMillis = null,
                    totalPrice = 0.0,
                    details = emptyList(),
                    childSeat = childSeat,
                    status = ContractStatus.ACTIVE
                )
                viewModel.addContract(newContract)
                onSubmitted()
            }
        ) {
            Text("Отправить заявку")
        }
        Spacer(modifier = Modifier.height(8.dp))
        TextButton(onClick = onBack) { Text("Назад") }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownMenuExample(
    selected: String,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = selected,
            onValueChange = {},
            readOnly = true,
            label = { Text("Ценовой диапазон") },
            trailingIcon = {
                Icon(
                    imageVector = if (expanded) Icons.Filled.ArrowDropUp else Icons.Filled.ArrowDropDown,
                    contentDescription = null
                )
            },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable)
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            listOf("Бюджет", "Стандарт", "Премиум").forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        onSelected(item)
                        expanded = false
                    }
                )
            }
        }
    }
}