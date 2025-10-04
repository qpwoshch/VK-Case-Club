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
import java.net.Inet4Address
import java.net.NetworkInterface

fun main() {
    // Порт можно переопределить через ENV PORT
    val port = System.getenv("PORT")?.toIntOrNull() ?: 8080

    embeddedServer(Netty, port = port, host = "0.0.0.0") {
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
        val publicDir = File("public").absoluteFile

        // ----------- Полезные логи старта -----------
        val lanIps = localLanIPv4()
        log.info("===========================================")
        log.info(" RuStore MVP backend is up")
        log.info(" Host: 0.0.0.0  |  Port: $port")
        log.info(" Public dir: ${publicDir.path} (exists=${publicDir.exists()})")
        log.info(" Base API URLs:")
        log.info("   • Local:  http://localhost:$port/")
        lanIps.forEach { ip -> log.info("   • LAN:    http://$ip:$port/") }
        log.info(" Useful endpoints:")
        log.info("   • Health:      http://localhost:$port/health")
        log.info("   • Apps:        http://localhost:$port/apps")
        log.info("   • Apps Start:  http://localhost:$port/apps/start")
        log.info("   • Categories:  http://localhost:$port/categories")
        log.info("===========================================")
        // --------------------------------------------

        // Валидация сидов
        validateSeeds()

        // Роутинг
        configureRouting(publicDir)
    }.start(wait = true)
}

/** Список локальных IPv4 адресов (исключая loopback/виртуальные/выключенные интерфейсы). */
private fun localLanIPv4(): List<String> =
    NetworkInterface.getNetworkInterfaces()
        .toList()
        .asSequence()
        .filter { it.isUp && !it.isLoopback && !it.isVirtual }
        .flatMap { it.inetAddresses.toList().asSequence() }
        .filterIsInstance<Inet4Address>()
        .map { it.hostAddress }
        .filter { it != "127.0.0.1" }
        .distinct()
        .toList()
