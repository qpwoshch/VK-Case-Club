package com.fiveBoys.rustore

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.foundation.clickable

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
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "RuStore",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = MaterialTheme.typography.titleMedium.fontSize * 1.5f
                    ),
                    modifier = Modifier.padding(8.dp)
                )
            }

            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface),
            )

            Spacer(Modifier.height(2.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = app.iconUrl,
                    contentDescription = "App icon",
                    modifier = Modifier
                        .size(72.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(app.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("Разработчик: ${app.developer}", style = MaterialTheme.typography.bodySmall)
                    Text("Категория: ${app.category}", style = MaterialTheme.typography.bodySmall)
                    Text("Возраст: ${app.ratingAge}", style = MaterialTheme.typography.bodySmall)
                }
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Установить", style = MaterialTheme.typography.bodyLarge)
            }

            Spacer(Modifier.height(16.dp))

            Text(
                text = app.fullDesc,
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(Modifier.height(24.dp))

            Text(
                text = "Скриншоты",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(8.dp))

            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(app.screenshots.size) { index ->
                    AsyncImage(
                        model = app.screenshots[index],
                        contentDescription = "Screenshot $index",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(180.dp)
                            .clickable {
                                navController.navigate("screenshot_viewer/$index")
                            }
                    )
                }
            }
        }
    }
}
