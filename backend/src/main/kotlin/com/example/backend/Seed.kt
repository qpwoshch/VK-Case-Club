package com.example.backend

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
        iconPath = "icons/1/icon.png",
        screenshotPaths = listOf("screenshots/1/screen1.png", "screenshots/1/screen2.png", "screenshots/1/screen3.png"),
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
        iconPath = "icons/2/icon.png",
        screenshotPaths = listOf("screenshots/2/screen1.png", "screenshots/2/screen2.png", "screenshots/2/screen3.png"),
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
        iconPath = "icons/3/icon.png",
        screenshotPaths = listOf("screenshots/3/screen1.png", "screenshots/3/screen2.png", "screenshots/3/screen3.png"),
        apkPath = "apks/demo.apk"
    )
)

val AllowedCategories = setOf("Финансы", "Инструменты", "Игры", "Государственные", "Транспорт")
val AllowedAgeRatings = setOf("0+", "6+", "8+", "12+", "16+", "18+")

fun validateSeeds() {
    val badCats = appsSeed.filter { it.category !in AllowedCategories }
    val badAges = appsSeed.filter { it.ratingAge !in AllowedAgeRatings }
    require(badCats.isEmpty()) { "Seed category is out of allowed set: $badCats" }
    require(badAges.isEmpty()) { "Seed age rating is out of allowed set: $badAges" }
}
