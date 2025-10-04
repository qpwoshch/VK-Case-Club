package com.fiveBoys.rustore

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenshotViewerScreen(
    navController: NavController,
    startIndex: Int,
    screenshots: List<String>
) {
    val pagerState = rememberPagerState(initialPage = startIndex, pageCount = { screenshots.size })
    val scope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Скриншоты") },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Назад")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentAlignment = Alignment.Center
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                AsyncImage(
                    model = screenshots[page],
                    contentDescription = "Screenshot $page",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxSize()
                )
            }

            IconButton(
                onClick = {
                    scope.launch {
                        val prev = (pagerState.currentPage - 1).coerceAtLeast(0)
                        pagerState.animateScrollToPage(prev)
                    }
                },
                modifier = Modifier.align(Alignment.CenterStart).padding(16.dp)
            ) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Назад")
            }

            IconButton(
                onClick = {
                    scope.launch {
                        val next = (pagerState.currentPage + 1).coerceAtMost(screenshots.size - 1)
                        pagerState.animateScrollToPage(next)
                    }
                },
                modifier = Modifier.align(Alignment.CenterEnd).padding(16.dp)
            ) {
                Icon(imageVector = Icons.Filled.ArrowForward, contentDescription = "Вперёд")
            }
        }
    }
}
