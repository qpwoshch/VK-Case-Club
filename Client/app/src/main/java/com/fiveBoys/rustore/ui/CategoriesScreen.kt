package com.fiveBoys.rustore.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.fiveBoys.rustore.network.CategoryDto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoriesScreen(
    nav: NavController,
    viewModel: CategoriesViewModel,
    onPick: (String) -> Unit
) {
    LaunchedEffect(Unit) { viewModel.load() }
    val cats by viewModel.cats.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("Категории") }) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(cats) { c -> CategoryRow(c) { onPick(c.name) } }
        }
    }
}

@Composable
private fun CategoryRow(c: CategoryDto, onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text(c.name) },
        supportingContent = { Text("${c.count} приложений") },
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    )
    HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
}
