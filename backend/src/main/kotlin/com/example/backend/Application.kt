package com.example.backend

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.http.content.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import java.io.File

// ---------- DTO для ответов ----------
@Serializable
data class AppDto(
    val id: String,
    val name: String,
    val developer: String,
    val category: String,      // "Финансы", "Инструменты", "Игры", "Государственные", "Транспорт"
    val ratingAge: String,     // "0+", "6+", "12+", "16+", "18+"
    val shortDesc: String,
    val fullDesc: String,
    val iconUrl: String,
    val screenshots: List<String>,
    val apkUrl: String
)

@Serializable
data class CategoryDto(
    val name: String,
    val count: Int
)

// ---------- Seed-модель (храним относительные пути к статике) ----------
private data class SeedApp(
    val id: String,
    val name: String,
    val developer: String,
    val category: String,
    val ratingAge: String,
    val shortDesc: String,
    val fullDesc: String,
    val iconPath: String,           // относительные пути, чтобы уметь подставлять правильный host:port
    val screenshotPaths: List<String>,
    val apkPath: String
)

private val appsSeed = listOf(
    SeedApp(
        id = "1",
        name = "Demo App",
        developer = "Team",
        category = "Инструменты",
        ratingAge = "12+",
        shortDesc = "Короткое описание",
        fullDesc = "Полное описание приложения...",
        iconPath = "icons/demo.png",
        screenshotPaths = listOf("screenshots/s1.png", "screenshots/s2.png"),
        apkPath = "apks/demo.apk"
    ),
    SeedApp(
        id = "2",
        name = "Taxi City",
        developer = "City LLC",
        category = "Транспорт",
        ratingAge = "6+",
        shortDesc = "Официальный городской такси-сервис",
        fullDesc = "Описание, фичи, преимущества...",
        iconPath = "icons/demo.png",
        screenshotPaths = listOf("screenshots/s1.png"),
        apkPath = "apks/demo.apk"
    ),
    SeedApp(
        id = "3",
        name = "Gov Services",
        developer = "GovTech",
        category = "Государственные",
        ratingAge = "0+",
        shortDesc = "Госуслуги в кармане",
        fullDesc = "Записи, документы, уведомления...",
        iconPath = "icons/demo.png",
        screenshotPaths = listOf("screenshots/s2.png"),
        apkPath = "apks/demo.apk"
    )
)

// --------- Хелперы ---------
private fun ApplicationCall.baseUrl(): String {
    // на случай если захотим прокси
    val scheme = request.header("X-Forwarded-Proto") ?: "http"
    val host = request.host()
    val port = request.port()
    val defaultPort = (scheme == "http" && port == 80) || (scheme == "https" && port == 443)
    return if (defaultPort) "$scheme://$host" else "$scheme://$host:$port"
}

private fun SeedApp.toDto(base: String) = AppDto(
    id = id,
    name = name,
    developer = developer,
    category = category,
    ratingAge = ratingAge,
    shortDesc = shortDesc,
    fullDesc = fullDesc,
    iconUrl = "$base/static/$iconPath",
    screenshots = screenshotPaths.map { "$base/static/$it" },
    apkUrl = "$base/static/$apkPath"
)

// --------- Entry point ---------
fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        install(ContentNegotiation) { json() }
        install(CORS) {
            anyHost() // только для этапа разработки, потом убрать
            allowHeader(HttpHeaders.ContentType)
            allowMethod(HttpMethod.Get)
        }

        routing {
            val publicDir = File("/home/kirill/Desktop/VK-Case-Club/backend/public").absoluteFile
            println("Serving static from: ${publicDir.path} | exists=${publicDir.exists()}")

            // Раздача статики
            staticFiles("/static", publicDir)

            // Список приложений
            get("/apps") {
                val base = call.baseUrl()
                call.respond(appsSeed.map { it.toDto(base) })
            }

            // Детальная карточка
            get("/apps/{id}") {
                val id = call.parameters["id"]
                val base = call.baseUrl()
                val app = appsSeed.find { it.id == id }?.toDto(base)
                if (app == null) {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "App not found"))
                } else {
                    call.respond(app)
                }
            }

            // Категории с количеством приложений
            get("/categories") {
                val categories = appsSeed
                    .groupBy { it.category }
                    .map { (name, items) -> CategoryDto(name, items.size) }
                    .sortedBy { it.name }
                call.respond(categories)
            }

            // Быстрый healthcheck
            get("/health") { call.respondText("OK") }
        }
    }.start(wait = true)
}
