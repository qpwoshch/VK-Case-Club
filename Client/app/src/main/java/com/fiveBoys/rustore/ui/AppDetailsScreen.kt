package com.fiveBoys.rustore.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDetailsScreen(appId: String, onBack: () -> Unit) {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Приложение #$appId") }) }
    ) { padding ->
        Column(Modifier.padding(padding).padding(16.dp)) {
            Text("Экран карточки. Здесь можно будет показать скриншоты, описание, кнопку «Установить».")
        }
    }
}
