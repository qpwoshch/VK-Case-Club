package com.example.backend

import kotlinx.serialization.Serializable

@Serializable
data class AppDto(
    val id: String,
    val name: String,
    val developer: String,
    val category: String,       // "Финансы", "Инструменты", "Игры", "Государственные", "Транспорт"
    val ratingAge: String,      // "0+", "6+", "12+", "16+", "18+"
    val shortDesc: String,
    val fullDesc: String,
    val iconUrl: String,
    val screenshots: List<String>,
    val apkUrl: String,
    val apkSize: Long? = null,
    val apkSha256: String? = null
)

@Serializable
data class CategoryDto(
    val name: String,
    val count: Int
)
