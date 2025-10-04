package com.fiveBoys.rustore

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun AppCardScreen(
    navController: NavController,
    app: App
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            TopAppBar(title = { Text(app.name) })

            Spacer(Modifier.height(16.dp))

            // 🔹 Иконка + информация
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = app.iconUrl,
                    contentDescription = "App icon",
                    modifier = Modifier.size(72.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(app.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text("Разработчик: ${app.developer}", style = MaterialTheme.typography.bodySmall)
                    Text("Категория: ${app.category}", style = MaterialTheme.typography.bodySmall)
                    Text("Возраст: ${app.ratingAge}", style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = { /* TODO: установка APK через app.apkUrl */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Установить")
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = app.fullDesc,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Скриншоты",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(8.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(app.screenshots.size) { index ->
                    AsyncImage(
                        model = app.screenshots[index],
                        contentDescription = "screenshot $index",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(180.dp)
                            .clickable {
                                // Передаём весь объект App и индекс
                                navController.currentBackStackEntry?.arguments?.putSerializable("app", app)
                                navController.navigate("screenshot_viewer/$index")
                            }
                    )
                }
            }
        }
    }
}
