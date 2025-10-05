package com.fiveBoys.rustore.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDetailsScreen(appId: String, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Детали приложения") },
                navigationIcon = {
                    // Добавьте кнопку назад если нужно
                }
            )
        }
    ) { padding ->
        Text(
            text = "Детали приложения ID: $appId\n(Экран в разработке)",
            modifier = androidx.compose.ui.Modifier.padding(padding)
        )
    }
}

