package com.fiveBoys.rustore

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageInstaller
import android.net.Uri
import android.os.Build
import android.provider.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.concurrent.TimeUnit

object AppInstaller {

    private val http = OkHttpClient.Builder()
        .followRedirects(true)
        .followSslRedirects(true)
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(2, TimeUnit.MINUTES)
        .retryOnConnectionFailure(true)
        .build()

    fun needsUnknownSources(context: Context): Boolean =
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O &&
                !context.packageManager.canRequestPackageInstalls()

    fun unknownSourcesIntent(context: Context): Intent =
        Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES)
            .setData(Uri.parse("package:${context.packageName}"))

    suspend fun downloadApk(
        context: Context,
        url: String,
        suggestedFileName: String? = null,
        onProgress: (Int) -> Unit = {},
        onStatus: (String) -> Unit = {}
    ): File? = withContext(Dispatchers.IO) {
        try {
            val req = Request.Builder()
                .url(url)
                .header("Accept-Encoding","identity")
                .header("User-Agent","RuStore-Demo/1.0")
                .build()

            http.newCall(req).execute().use { resp ->
                if (!resp.isSuccessful) {
                    withContext(Dispatchers.Main) { onStatus("HTTP ${resp.code}") }
                    return@withContext null
                }
                val body = resp.body ?: return@withContext null
                val total = body.contentLength()
                val fileName = suggestedFileName ?: (Uri.parse(url).lastPathSegment ?: "app.apk")
                val dest = File(context.cacheDir, fileName)

                body.byteStream().use { input ->
                    FileOutputStream(dest, false).use { out ->
                        val buf = ByteArray(512 * 1024)
                        var done = 0L
                        var last = 0L
                        while (true) {
                            val r = input.read(buf); if (r == -1) break
                            out.write(buf, 0, r)
                            done += r
                            if (total > 0 && done - last >= 2L * 1024 * 1024) {
                                val pct = ((done*100)/total).toInt().coerceAtMost(99)
                                withContext(Dispatchers.Main) { onProgress(pct) }
                                last = done
                            }
                        }
                        out.flush()
                    }
                }
                if (total > 0) withContext(Dispatchers.Main) { onProgress(100) }
                dest
            }
        } catch (t: Throwable) {
            withContext(Dispatchers.Main) { onStatus("Сетевая ошибка: ${t.message}") }
            null
        }
    }


    fun validateApk(context: Context, file: File): String? {
        if (!file.exists() || file.length() < 128 * 1024) return "Файл слишком мал"
        FileInputStream(file).use { fis ->
            val sig = ByteArray(4)
            if (fis.read(sig) != 4 || sig[0] != 0x50.toByte() || sig[1] != 0x4B.toByte()
                || sig[2] != 0x03.toByte() || sig[3] != 0x04.toByte()
            ) return "Не APK (нет сигнатуры ZIP)"
        }
        val pm = context.packageManager
        val info = if (Build.VERSION.SDK_INT >= 33)
            pm.getPackageArchiveInfo(file.absolutePath,
                android.content.pm.PackageManager.PackageInfoFlags.of(0))
        else @Suppress("DEPRECATION") pm.getPackageArchiveInfo(file.absolutePath, 0)
        return if (info == null) "PackageManager не распознал файл как APK" else null
    }

    data class InstallStartResult(val sessionId: Int, val message: String)


    fun installApk(context: Context, file: File): InstallStartResult {
        val appCtx = context.applicationContext
        return try {
            val installer = appCtx.packageManager.packageInstaller
            val params = PackageInstaller.SessionParams(PackageInstaller.SessionParams.MODE_FULL_INSTALL)
            val sid = installer.createSession(params)
            val session = installer.openSession(sid)

            FileInputStream(file).use { input ->
                session.openWrite("base.apk", 0, -1).use { out ->
                    val buf = ByteArray(512 * 1024)
                    while (true) {
                        val r = input.read(buf); if (r <= 0) break
                        out.write(buf, 0, r)
                    }
                    session.fsync(out)
                }
            }

            val flags = PendingIntent.FLAG_UPDATE_CURRENT or
                    (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE else 0)

            val pi = PendingIntent.getBroadcast(
                appCtx,
                sid,
                Intent(appCtx, com.fiveBoys.rustore.PackageInstallResultReceiver::class.java)
                    .putExtra("sessionId", sid),
                flags
            )

            session.commit(pi.intentSender)
            session.close()

            InstallStartResult(sid, "Отправлено на установку…")
        } catch (t: Throwable) {
            InstallStartResult(-1, "Ошибка установки: ${t.message}")
        }
    }

}
