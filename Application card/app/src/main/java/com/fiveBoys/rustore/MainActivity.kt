package com.fiveBoys.rustore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.fiveBoys.rustore.ui.theme.RuStoreTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    val opener = AppOpener()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()



        setContent {
            RuStoreTheme {
                val navController = rememberNavController()
                val appObject = opener.open("2")

                AppNavHost(navController, appObject)
            }
        }

    }
}

@Composable
fun AppNavHost(navController: NavHostController, app: App) {
    NavHost(
        navController = navController,
        startDestination = "app_card"
    ) {
        composable("app_card") {
            AppCardScreen(navController, app)
        }
        composable("screenshot_viewer/{index}") { backStackEntry ->
            val index = backStackEntry.arguments?.getString("index")?.toIntOrNull() ?: 0
            ScreenshotViewerScreen(navController, index, app.screenshots)
        }

    }
}
