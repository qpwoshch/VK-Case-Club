package com.example.backend

import kotlinx.serialization.Serializable

@Serializable
data class AppDto(
    val id: String,
    val name: String,
    val developer: String,
    val category: String,       
    val ratingAge: String,   
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

@Serializable
data class PagedAppsDto(
    val items: List<AppDto>,
    val total: Int,
    val limit: Int,
    val offset: Int
)

@Serializable
data class ErrorDto(val error: String, val message: String? = null)

fun error(code: String, msg: String? = null) = ErrorDto(code, msg)

@Serializable
data class DebugPublicDto(
    val publicPath: String,
    val exists: Boolean,
    val icons: List<String>,
    val screenshots: List<String>,
    val apks: List<String>
)
