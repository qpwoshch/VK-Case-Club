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


// В конце Application.kt добавьте:
fun Application.module() {
    // Перенесите сюда всю конфигурацию из embeddedServer {...}
    install(ContentNegotiation) { json() }
    install(AutoHeadResponse)
    install(Compression) { gzip() }
    install(PartialContent)
    install(CORS) {
        anyHost()
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Range)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Head)
        allowMethod(HttpMethod.Options)
    }
    install(StatusPages) {
        status(HttpStatusCode.NotFound) { call, _ ->
            call.respond(HttpStatusCode.NotFound, error("not_found", "Resource not found"))
        }
        exception<Throwable> { call, cause ->
            cause.printStackTrace()
            call.respond(HttpStatusCode.InternalServerError, error("internal_error", cause.message))
        }
    }

    val publicDir = File(
        System.getenv("PUBLIC_DIR") ?: "/home/kirill/Desktop/VK-Case-Club/backend/public"
    ).absoluteFile

    validateSeeds()
    configureRouting(publicDir)
}


fun main() {
    val port = System.getenv("PORT")?.toIntOrNull() ?: 8080

    embeddedServer(Netty, port = port, host = "0.0.0.0") {
        module()
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
