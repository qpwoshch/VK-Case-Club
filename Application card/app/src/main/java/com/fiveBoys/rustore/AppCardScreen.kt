package com.fiveBoys.rustore

import android.content.pm.PackageInstaller
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import kotlinx.coroutines.launch

private const val LOG_TAG = "RuStoreInstall"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppCardScreen(
    navController: NavController,
    app: App
) {
    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()

    var status by remember { mutableStateOf<String?>(null) }
    var busy by remember { mutableStateOf(false) }

    fun post(msg: String) {
        status = msg
        Log.d(LOG_TAG, msg)
    }

    val settingsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { _: ActivityResult ->
        post("Вернулись из настроек. Нажмите «Установить» ещё раз, если готовы.")
    }

    DisposableEffect(Unit) {
        val installer = ctx.packageManager.packageInstaller
        val callback = object : PackageInstaller.SessionCallback() {
            override fun onCreated(sessionId: Int) {}
            override fun onBadgingChanged(sessionId: Int) {}
            override fun onActiveChanged(sessionId: Int, active: Boolean) {}

            override fun onProgressChanged(sessionId: Int, progress: Float) {
                post("Идёт установка…")
            }

            override fun onFinished(sessionId: Int, success: Boolean) {
                if (success) {
                    post("Приложение установлено ✅")
                } else {
                    post("Установка завершилась с ошибкой")
                }
            }
        }
        installer.registerSessionCallback(callback)
        onDispose { installer.unregisterSessionCallback(callback) }
    }

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
                Image(
                    painter = painterResource(id = R.drawable.rustoree),
                    contentDescription = "RuStore Logo",
                    modifier = Modifier.size(40.dp),
                    contentScale = ContentScale.Fit
                )
                Spacer(Modifier.width(8.dp))
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
            )

            Spacer(Modifier.height(2.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = app.iconUrl,
                    contentDescription = "App icon",
                    modifier = Modifier.size(72.dp),
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
                onClick = {
                    if (busy) return@Button

                    if (AppInstaller.needsUnknownSources(ctx)) {
                        post("Нужно разрешение на установку из неизвестных источников…")
                        settingsLauncher.launch(AppInstaller.unknownSourcesIntent(ctx))
                        return@Button
                    }

                    busy = true
                    post("Подключаюсь к серверу…")

                    scope.launch {
                        try {
                            val safeName = app.name.replace("\\s+".toRegex(), "_") + ".apk"
                            val file = AppInstaller.downloadApk(
                                context = ctx,
                                url = app.apkUrl,
                                suggestedFileName = safeName,
                                onProgress = { p -> post("Скачивание: $p%") },
                                onStatus = { m -> post(m) }
                            )
                            if (file == null) {
                                if (status == null) post("Ошибка загрузки")
                                busy = false
                                return@launch
                            }
                            post("Загрузка завершена. Проверяю APK…")

                            AppInstaller.validateApk(ctx, file)?.let { err ->
                                post("Невалидный APK: $err")
                                busy = false
                                return@launch
                            }
                            post("APK ок. Идёт установка…")


                            val res = AppInstaller.installApk(ctx, file)
                            post(res.message)
                        } catch (t: Throwable) {
                            post("Ошибка: ${t.message}")
                        } finally {
                            busy = false
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                enabled = !busy,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(if (busy) "Идёт…" else "Установить", style = MaterialTheme.typography.bodyLarge)
            }

            Spacer(Modifier.height(8.dp))
            status?.let { Text(it, style = MaterialTheme.typography.bodySmall) }

            Spacer(Modifier.height(16.dp))

            Text(text = app.fullDesc, style = MaterialTheme.typography.bodyMedium)

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
                            .size(210.dp)
                            .clickable {
                                navController.navigate("screenshot_viewer/$index")
                            }
                    )
                }
            }
        }
    }
}
