package com.fiveBoys.rustore.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppsResponse(
    val apps: List<AppDto>
)

@Serializable
data class AppDto(
    val id: String,
    val name: String,
    val category: String,
    @SerialName("ratingAge") val age: String? = null,
    @SerialName("shortDesc") val description: String,
    @SerialName("iconUrl") val iconUrl: String = ""
)

@Serializable
data class PagedResponse<T>(
    val items: List<T>,
    val page: Int,
    val pageSize: Int,
    val total: Int,
    val hasNext: Boolean
)

@Serializable
data class CategoryDto(
    val name: String,
    val count: Int
)