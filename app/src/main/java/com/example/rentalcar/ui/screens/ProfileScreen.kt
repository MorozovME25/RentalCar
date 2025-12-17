package com.example.rentalcar.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.rentalcar.data.models.ContractStatus
import com.example.rentalcar.ui.components.ContractItem
import com.example.rentalcar.ui.viewmodel.AppViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    viewModel: AppViewModel,
    onLogout: () -> Unit,
    onNavigateToRental: () -> Unit,
    onBack: () -> Unit
) {
    val user = viewModel.currentUser ?: return
    val activeContracts = viewModel.getContractsByStatus(ContractStatus.ACTIVE)
    val completedContracts = viewModel.getContractsByStatus(ContractStatus.COMPLETED)

    var selectedTab by remember { mutableStateOf("active") }
    var showLogoutDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Личный кабинет") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = {showLogoutDialog = true}) {
                        Icon(
                            imageVector = Icons.Filled.ExitToApp,
                            contentDescription = "Выйти"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            item {
                Text(
                    text = "Баланс залога: ${user.deposit} ₽",
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            item {
                Button(
                    onClick = onNavigateToRental,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Оставить заявку на аренду")
                }
            }

            item {
                Row {
                    TextButton(onClick = { selectedTab = "active" }) {
                        Text("Действующие")
                    }
                    TextButton(onClick = { selectedTab = "completed" }) {
                        Text("Завершённые")
                    }
                }
            }

            val contractsToShow = if (selectedTab == "active") activeContracts else completedContracts
            items(contractsToShow) { contract ->
                ContractItem(
                    contract = contract,
                    isExpanded = false,
                    onToggle = { }
                )
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Выход") },
            text = { Text("Вы уверены, что хотите выйти?") },
            confirmButton = {
                TextButton(onClick = {
                    showLogoutDialog = false
                    onLogout()
                }) {
                    Text("Да")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Отмена")
                }
            }
        )
    }
}