package com.fiveBoys.rustore.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppDto(
    val id: String,
    val name: String,
    val description: String,
    val category: String,
    @SerialName("iconUrl") val iconUrl: String = "",
    @SerialName("age") val age: String? = null
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
