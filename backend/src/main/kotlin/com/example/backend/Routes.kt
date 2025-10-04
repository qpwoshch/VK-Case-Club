// src/main/kotlin/com/example/backend/Routes.kt
package com.example.backend

import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.http.content.staticFiles
import io.ktor.server.response.respond
import io.ktor.server.response.respondFile
import io.ktor.server.response.respondText
import io.ktor.server.routing.*
import java.io.File
import java.util.Locale

fun Application.configureRouting(publicDir: File) {
    routing {
        // Раздача статики
        staticFiles("/static", publicDir)

        // Список приложений: фильтр, поиск, пагинация
        get("/apps") {
            val base = call.baseUrl()

            val rawCat = call.request.queryParameters["category"]?.trim()
            val q = call.request.queryParameters["q"]?.trim()?.lowercase(Locale.ROOT)
            val limit = call.request.queryParameters["limit"]?.toIntOrNull()?.coerceIn(1, 100) ?: 50
            val offset = call.request.queryParameters["offset"]?.toIntOrNull()?.coerceAtLeast(0) ?: 0

            var seq = appsSeed.asSequence()

            if (!rawCat.isNullOrEmpty()) {
                val norm = rawCat.lowercase(Locale.ROOT)
                seq = seq.filter { app ->
                    val cat = app.category.lowercase(Locale.ROOT)
                    cat == norm || cat.contains(norm)
                }
            }

            if (!q.isNullOrEmpty()) {
                seq = seq.filter { app ->
                    app.name.lowercase(Locale.ROOT).contains(q) ||
                            app.shortDesc.lowercase(Locale.ROOT).contains(q) ||
                            app.developer.lowercase(Locale.ROOT).contains(q)
                }
            }

            val all = seq.toList()
            val page = all.drop(offset).take(limit).map { it.toDto(base, publicDir) }

            call.respond(PagedAppsDto(items = page, total = all.size, limit = limit, offset = offset))
        }

        // Карточка приложения
        get("/apps/{id}") {
            val id = call.parameters["id"]
            val base = call.baseUrl()
            val app = appsSeed.find { it.id == id }
            if (app == null) {
                call.respond(HttpStatusCode.NotFound, error("app_not_found", "App with id=$id not found"))
            } else {
                call.respond(app.toDto(base, publicDir))
            }
        }

        // Категории
        get("/categories") {
            val categories = appsSeed
                .groupBy { it.category }
                .map { (name, items) -> CategoryDto(name, items.size) }
                .sortedBy { it.name }
            call.respond(categories)
        }

        // APK-скачивание с корректными заголовками и Range
        get("/apps/{id}/apk") {
            val id = call.parameters["id"]
            val app = appsSeed.find { it.id == id }
            if (app == null) {
                call.respond(HttpStatusCode.NotFound, error("app_not_found", "App with id=$id not found"))
                return@get
            }

            val apkFile = File(publicDir, app.apkPath)
            if (!apkFile.exists()) {
                call.respond(HttpStatusCode.NotFound, error("apk_not_found", "APK not found for app id=$id"))
                return@get
            }

            // MIME для APK: можно оставить octet-stream, но для Android понятнее так:
            call.response.headers.append(HttpHeaders.ContentType, "application/vnd.android.package-archive")
            val contentDisposition = "attachment; filename=\"${app.name}.apk\""
            call.response.headers.append(HttpHeaders.ContentDisposition, contentDisposition)

            call.respondFile(apkFile) // PartialContent включён — Range работает
        }

        // Healthcheck
        get("/health") { call.respondText("OK") }

        // Диагностика статики (типизированный DTO — без проблем сериализации)
        get("/debug/public") {
            val icons = File(publicDir, "icons").list()?.sorted()?.toList() ?: emptyList()
            val screenshots = File(publicDir, "screenshots").list()?.sorted()?.toList() ?: emptyList()
            val apks = File(publicDir, "apks").list()?.sorted()?.toList() ?: emptyList()

            call.respond(
                DebugPublicDto(
                    publicPath = publicDir.absolutePath,
                    exists = publicDir.exists(),
                    icons = icons,
                    screenshots = screenshots,
                    apks = apks
                )
            )
        }
    }
}
