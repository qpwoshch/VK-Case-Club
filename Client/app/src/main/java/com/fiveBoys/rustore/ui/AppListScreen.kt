package com.fiveBoys.rustore.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.fiveBoys.rustore.network.AppDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppListScreen(
    nav: NavController,
    viewModel: AppListViewModel,
    category: String?,
    onOpenCategories: () -> Unit,
    onOpenDetails: (String) -> Unit
) {
    LaunchedEffect(category) { viewModel.refresh(category) }

    val list by viewModel.apps.collectAsState()
    val loading by viewModel.loading.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (category == null) "Рекомендации" else "Категория: $category") },
                actions = {
                    TextButton(onClick = onOpenCategories) { Text("Категории") }
                }
            )
        }
    ) { padding ->
        Box(Modifier.padding(padding)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(list, key = { _, it -> it.id }) { index, item ->
                    if (index >= list.lastIndex - 4) viewModel.loadMore() // префетч

                    AppListItem(app = item, onClick = { onOpenDetails(item.id) })
                }
                item {
                    if (loading) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.Center
                        ) { CircularProgressIndicator() }
                    }
                }
            }
        }
    }
}

@Composable
private fun AppListItem(app: AppDto, onClick: () -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = app.iconUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(app.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    Spacer(Modifier.width(8.dp))
                    app.age?.let { AssistChip(onClick = {}, label = { Text("$it+") }) }
                }
                Spacer(Modifier.height(4.dp))
                Text(app.description, maxLines = 2, style = MaterialTheme.typography.bodyMedium)
                Spacer(Modifier.height(6.dp))
                AssistChip(onClick = {}, label = { Text(app.category) })
            }
        }
    }
}
