package com.example.rentalcar.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.rentalcar.data.models.User
import com.example.rentalcar.ui.viewmodel.AppViewModel

@Composable
fun RegisterScreen(
    viewModel: AppViewModel,
    onBack: () -> Unit
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var birthDate by remember { mutableStateOf("") }
    var consent by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        if (viewModel.error != null) {
            Text(
                text = viewModel.error!!,
                color = Color.Red,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        OutlinedTextField(value = fullName, onValueChange = { fullName = it }, label = { Text("ФИО") })
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Телефон") })
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = birthDate, onValueChange = { birthDate = it }, label = { Text("Дата рождения (дд.мм.гггг)") })
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Пароль") },
            visualTransformation = PasswordVisualTransformation()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = consent, onCheckedChange = { consent = it })
            Text("Согласие на обработку персональных данных")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            enabled = consent && !viewModel.isLoading,
            onClick = {
                viewModel.register(
                    User(
                        id = "", // Будет сгенерирован Firebase
                        fullName = fullName,
                        email = email,
                        phone = phone,
                        birthDate = birthDate,
                        password = password,
                        isConsentGiven = consent
                    )
                )
            }
        ) {
            if (viewModel.isLoading) {
                Text("Регистрация...")
            } else {
                Text("Зарегистрироваться")
            }
        }
        TextButton(onClick = onBack) {
            Text("Назад")
        }
    }
}