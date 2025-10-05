@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage



@Composable
fun ScreenshotViewerScreen(
    screens: List<String>,
    startIndex: Int,
    onBack: () -> Unit
) {
    if (screens.isEmpty()) {
        LaunchedEffect(Unit) { onBack() }
        return
    }

    val pagerState = rememberPagerState(
        initialPage = startIndex.coerceIn(0, screens.lastIndex),
        pageCount = { screens.size }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${pagerState.currentPage + 1} / ${screens.size}") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { inner ->
        Box(Modifier.fillMaxSize().padding(inner)) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                AsyncImage(
                    model = screens[page],
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}
