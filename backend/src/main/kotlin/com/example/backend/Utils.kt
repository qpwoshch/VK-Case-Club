package com.example.backend

import io.ktor.server.application.*
import io.ktor.server.request.*
import java.io.File
import java.security.MessageDigest

// База для абсолютных URL (учитываем X-Forwarded-Proto)
internal fun ApplicationCall.baseUrl(): String {
    val scheme = request.header("X-Forwarded-Proto") ?: "http"
    val host = request.host()
    val port = request.port()
    val defaultPort = (scheme == "http" && port == 80) || (scheme == "https" && port == 443)
    return if (defaultPort) "$scheme://$host" else "$scheme://$host:$port"
}

// SHA-256 файла (без загрузки всего в память)
internal fun sha256(file: File): String {
    val md = MessageDigest.getInstance("SHA-256")
    file.inputStream().use { fis ->
        val buf = ByteArray(8 * 1024)
        while (true) {
            val read = fis.read(buf)
            if (read <= 0) break
            md.update(buf, 0, read)
        }
    }
    return md.digest().joinToString("") { "%02x".format(it) }
}
