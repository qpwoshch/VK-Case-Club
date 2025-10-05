// src/main/kotlin/com/example/backend/Routes.kt
package com.example.backend

import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.*
import io.ktor.server.http.content.staticFiles
import io.ktor.server.response.respond
import io.ktor.server.response.respondFile
import io.ktor.server.response.respondText
import io.ktor.server.routing.*
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import java.io.File
import java.util.Locale

fun Application.configureRouting(publicDir: File) {
    routing {
        staticFiles("/static", publicDir)


        get("/apps/start") {
            val base = call.baseUrl()
            val fields = setOf("id", "name", "category", "ratingAge", "shortDesc", "iconUrl")
            val list = appsSeed.map { it.toDto(base, publicDir) }
            val jsonArray: JsonArray = appsToJsonArray(list, fields)
            val wrapped: JsonObject = JsonObject(mapOf("apps" to jsonArray))
            call.respond(wrapped)
        }

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

        get("/apps/{id}") {
            val base = call.baseUrl()
            val idParam = call.parameters["id"]

            if (idParam.isNullOrBlank()) {
                call.respond(HttpStatusCode.BadRequest, error("bad_request", "Missing id"))
                return@get
            }

            val seed = appsSeed.find { it.id == idParam }
            if (seed == null) {
                call.respond(HttpStatusCode.NotFound, error("app_not_found", "App with id=$idParam not found"))
                return@get
            }

            val dto = seed.toDto(base, publicDir)
            val fields = setOf(
                "id", "name", "developer", "category", "ratingAge",
                "fullDesc", "iconUrl", "screenshots", "apkUrl", "apkSize"
            )

            val json: JsonObject = appToJson(dto, fields)
            call.respond(json)
        }

        get("/categories") {
            val categories = appsSeed
                .groupBy { it.category }
                .map { (name, items) -> CategoryDto(name, items.size) }
                .sortedBy { it.name }
                .toMutableList()

            categories.add(0, CategoryDto("Все", appsSeed.size))
            call.respond(categories)
        }

        get("/categories/{category}") {
            val base = call.baseUrl()
            val fields = setOf("id", "name", "category", "ratingAge", "shortDesc", "iconUrl")

            val category = call.parameters["category"]

            val filtered = when {
                category == null -> emptyList()
                category.equals("Все", ignoreCase = true) -> appsSeed
                else -> appsSeed.filter { it.category.equals(category, ignoreCase = true) }
            }

            val list = filtered.map { it.toDto(base, publicDir) }
            val jsonArray: JsonArray = appsToJsonArray(list, fields)
            val wrapped: JsonObject = JsonObject(mapOf("apps" to jsonArray))

            call.respond(wrapped)
        }

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

            call.response.headers.append(HttpHeaders.ContentType, "application/vnd.android.package-archive")
            val contentDisposition = "attachment; filename=\"${app.name}.apk\""
            call.response.headers.append(HttpHeaders.ContentDisposition, contentDisposition)

            call.respondFile(apkFile) // PartialContent включён — Range работает
        }

        get("/health") { call.respondText("OK") }

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
