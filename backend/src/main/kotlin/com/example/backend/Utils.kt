package com.example.backend

import io.ktor.server.application.ApplicationCall
import io.ktor.server.request.header
import io.ktor.server.request.host
import io.ktor.server.request.port
import kotlinx.serialization.json.*
import java.io.File
import java.security.MessageDigest

internal val ALL_APPDTO_FIELDS: Set<String> = setOf(
    "id", "name", "developer", "category", "ratingAge",
    "shortDesc", "fullDesc", "iconUrl", "screenshots",
    "apkUrl", "apkSize", "apkSha256"
)

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
        apkUrl = "$base/static/apks/$id/app-debug.apk",
        apkSize = size,
        apkSha256 = hash
    )
}


internal fun appToJson(app: AppDto, fields: Set<String>): JsonObject {
    val map = mutableMapOf<String, JsonElement>()

    fun put(name: String, value: String?) {
        if (name in fields && value != null) map[name] = JsonPrimitive(value)
    }

    fun putL(name: String, value: Long?) {
        if (name in fields && value != null) map[name] = JsonPrimitive(value)
    }

    fun putArr(name: String, values: List<String>?) {
        if (name in fields && values != null) {
            map[name] = JsonArray(values.map { JsonPrimitive(it) })
        }
    }

    put("id", app.id)
    put("name", app.name)
    put("developer", app.developer)
    put("category", app.category)
    put("ratingAge", app.ratingAge)
    put("shortDesc", app.shortDesc)
    put("fullDesc", app.fullDesc)
    put("iconUrl", app.iconUrl)
    putArr("screenshots", app.screenshots)
    put("apkUrl", app.apkUrl)
    putL("apkSize", app.apkSize)
    put("apkSha256", app.apkSha256)

    return JsonObject(map)
}

internal fun appsToJsonArray(apps: List<AppDto>, fields: Set<String>): JsonArray =
    JsonArray(apps.map { appToJson(it, fields) })

internal fun parseFields(call: ApplicationCall, default: Set<String>): Set<String> {
    val raw = call.request.queryParameters["fields"]?.trim().orEmpty()
    if (raw.isBlank()) return default
    return raw.split(",")
        .map { it.trim() }
        .filter { it.isNotEmpty() }
        .toSet()
}

internal fun ApplicationCall.baseUrl(): String {
    val scheme = request.header("X-Forwarded-Proto") ?: "http"
    val host = request.host()
    val port = request.port()
    val defaultPort = (scheme == "http" && port == 80) || (scheme == "https" && port == 443)
    return if (defaultPort) "$scheme://$host" else "$scheme://$host:$port"
}

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
