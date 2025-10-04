package com.example.backend

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import java.io.File

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        // JSON
        install(ContentNegotiation) { json() }

        // CORS (dev)
        install(CORS) {
            anyHost()
            allowHeader(HttpHeaders.ContentType)
            allowMethod(HttpMethod.Get)
            allowMethod(HttpMethod.Options)
        }

        // Единая обработка ошибок
        install(StatusPages) {
            exception<Throwable> { call, cause ->
                cause.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "internal_error"))
            }
        }

        // Путь к public
        val publicDir = File(
            System.getenv("PUBLIC_DIR")
                ?: "/home/kirill/Desktop/VK-Case-Club/backend/public"
        ).absoluteFile

        log.info("Serving static from: ${publicDir.path} | exists=${publicDir.exists()}")

        // Роутинг
        configureRouting(publicDir)
    }.start(wait = true)
}
