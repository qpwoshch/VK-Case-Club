package com.example.backend

import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.host
import io.ktor.server.request.port
import io.ktor.server.request.header
import java.io.File
import java.security.MessageDigest

// Mapping SeedApp -> AppDto with computed fields and absolute URLs
internal fun SeedApp.toDto(base: String, publicDir: File): AppDto {
    val apkFile = File(publicDir, apkPath)
    val size = if (apkFile.exists()) apkFile.length() else null
    val hash = if (apkFile.exists()) sha256(apkFile) else null

    return AppDto(
        id = id,
        name = name,
        developer = developer,
        category = category,
        ratingAge = ratingAge,
        shortDesc = shortDesc,
        fullDesc = fullDesc,
        iconUrl = "$base/static/$iconPath",
        screenshots = screenshotPaths.map { "$base/static/$it" },
        apkUrl = "$base/static/$apkPath",
        apkSize = size,
        apkSha256 = hash
    )
}

// Base URL builder (respects X-Forwarded-Proto)
internal fun ApplicationCall.baseUrl(): String {
    val scheme = request.header("X-Forwarded-Proto") ?: "http"
    val host = request.host()
    val port = request.port()
    val defaultPort = (scheme == "http" && port == 80) || (scheme == "https" && port == 443)
    return if (defaultPort) "$scheme://$host" else "$scheme://$host:$port"
}

// Streaming SHA-256
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
