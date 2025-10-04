package com.example.backend

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File
import java.util.Locale

fun Application.configureRouting(publicDir: File) {
    routing {
        // Раздача статики
        staticFiles("/static", publicDir)

        // Список приложений (+ фильтр по категории)
        get("/apps") {
            val base = call.baseUrl()
            // нормализуем запрос категории
            val raw = call.request.queryParameters["category"]?.trim()
            val norm = raw?.lowercase(Locale.ROOT)

            val filtered = if (norm.isNullOrEmpty()) {
                appsSeed
            } else {
                appsSeed.filter { app ->
                    val cat = app.category.trim().lowercase(Locale.ROOT)
                    // точное или частичное совпадение
                    cat == norm || cat.contains(norm)
                }
            }

            call.respond(filtered.map { it.toDto(base, publicDir) })
        }


        // Карточка приложения
        get("/apps/{id}") {
            val id = call.parameters["id"]
            val base = call.baseUrl()
            val app = appsSeed.find { it.id == id }
            if (app == null) {
                call.respond(HttpStatusCode.NotFound, mapOf("error" to "App not found"))
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

        // Healthcheck
        get("/health") { call.respondText("OK") }

        get("/debug/public") {
            val icons = File(publicDir, "icons").list()?.sorted() ?: emptyList()
            val screenshots = File(publicDir, "screenshots").list()?.sorted() ?: emptyList()
            val apks = File(publicDir, "apks").list()?.sorted() ?: emptyList()
            call.respond(
                mapOf(
                    "publicPath" to publicDir.absolutePath,
                    "exists" to publicDir.exists(),
                    "icons" to icons,
                    "screenshots" to screenshots,
                    "apks" to apks
                )
            )
        }

    }
}
