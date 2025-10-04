package com.example.backend

import java.io.File

// Seed-модель с относительными путями
internal data class SeedApp(
    val id: String,
    val name: String,
    val developer: String,
    val category: String,
    val ratingAge: String,
    val shortDesc: String,
    val fullDesc: String,
    val iconPath: String,
    val screenshotPaths: List<String>,
    val apkPath: String
)

internal val appsSeed = listOf(
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

// Сборка DTO с вычислением apkSize/apkSha256
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
