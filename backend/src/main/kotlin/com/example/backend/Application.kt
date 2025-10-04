package com.example.backend

import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.autohead.AutoHeadResponse
import io.ktor.server.plugins.compression.Compression
import io.ktor.server.plugins.compression.* // gzip()
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.plugins.partialcontent.PartialContent
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import java.io.File

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        // JSON
        install(ContentNegotiation) { json() }

        // HEAD-ответы автоматически
        install(AutoHeadResponse)

        // Сжатие (gzip)
        install(Compression) { gzip() }

        // Range/Partial Content (для APK)
        install(PartialContent)

        // CORS (dev)
        install(CORS) {
            anyHost()
            allowHeader(HttpHeaders.ContentType)
            allowHeader(HttpHeaders.Range)
            allowMethod(HttpMethod.Get)
            allowMethod(HttpMethod.Head)
            allowMethod(HttpMethod.Options)
        }

        // Единая обработка ошибок
        install(StatusPages) {
            status(HttpStatusCode.NotFound) { call, _ ->
                call.respond(HttpStatusCode.NotFound, error("not_found", "Resource not found"))
            }
            exception<Throwable> { call, cause ->
                cause.printStackTrace()
                call.respond(HttpStatusCode.InternalServerError, error("internal_error", cause.message))
            }
        }

        // Путь к public
        val publicDir = File(
            System.getenv("PUBLIC_DIR")
                ?: "/home/kirill/Desktop/VK-Case-Club/backend/public"
        ).absoluteFile

        log.info("Serving static from: ${publicDir.path} | exists=${publicDir.exists()}")

        // Валидация сидов
        validateSeeds()

        // Роутинг
        configureRouting(publicDir)
    }.start(wait = true)
}