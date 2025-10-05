import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.mutableIntStateOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenshotViewerScreen(
    screens: List<String>,
    startIndex: Int,
    onBack: () -> Unit
) {
    var index by rememberSaveable {
        mutableIntStateOf(startIndex.coerceIn(0, (screens.size - 1).coerceAtLeast(0)))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("${index + 1}/${screens.size}") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                },
                actions = {
                    IconButton(
                        enabled = index > 0,
                        onClick = { index-- }
                    ) { Text("◀️") }
                    IconButton(
                        enabled = index < screens.lastIndex,
                        onClick = { index++ }
                    ) { Text("▶️") }
                }
            )
        }
    ) { inner ->
        AsyncImage(
            model = screens.getOrNull(index),
            contentDescription = null,
            modifier = Modifier
                .padding(inner)
                .fillMaxSize(),
            contentScale = ContentScale.Fit
        )
    }
}
