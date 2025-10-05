package com.fiveBoys.rustore.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.fiveBoys.rustore.AppCardScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDetailsRoute(
    navController: NavController,
    viewModel: AppDetailsViewModel
) {
    val ui by viewModel.ui.collectAsState()

    when (ui) {
        is AppDetailsViewModel.Ui.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        is AppDetailsViewModel.Ui.Error -> {
            val msg = (ui as AppDetailsViewModel.Ui.Error).message
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Детали приложения") },
                        navigationIcon = {
                            IconButton(onClick = { navController.popBackStack() }) {
                                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                            }
                        }
                    )
                }
            ) { padding ->
                Text(
                    text = msg,
                    modifier = Modifier.padding(padding).padding(16.dp)
                )
            }
        }
        is AppDetailsViewModel.Ui.Ready -> {
            val app = (ui as AppDetailsViewModel.Ui.Ready).app
            // Вот он — твой рабочий экран
            AppCardScreen(navController = navController, app = app)
        }
    }
}
