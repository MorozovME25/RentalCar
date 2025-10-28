package com.example.rentalcar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.rentalcar.ui.AutoRentalApp
import com.example.rentalcar.ui.theme.RentalCarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RentalCarTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AutoRentalApp(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}