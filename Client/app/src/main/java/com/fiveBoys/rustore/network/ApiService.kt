package com.fiveBoys.rustore.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.create
import okhttp3.MediaType.Companion.toMediaType

object ApiConfig {
    // Эмулятор -> локальный ПК
    const val BASE_URL = "http://10.0.2.2:8080"
    // Если у вас сервер ждёт на /get_aps — просто поменяй эту константу:
    const val APPS_PATH = "/apps"        // "/get_aps"
    const val CATEGORIES_PATH = "/categories"
}

interface ApiService {
    @GET(ApiConfig.APPS_PATH)
    suspend fun getApps(
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
        @Query("category") category: String? = null
    ): PagedResponse<AppDto>

    @GET(ApiConfig.CATEGORIES_PATH)
    suspend fun getCategories(): List<CategoryDto>
}

fun provideApi(): ApiService {
    val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    val client = OkHttpClient.Builder().addInterceptor(logging).build()
    val contentType = "application/json".toMediaType()
    val json = Json { ignoreUnknownKeys = true }
    return Retrofit.Builder()
        .baseUrl(ApiConfig.BASE_URL)
        .addConverterFactory(json.asConverterFactory(contentType))
        .client(client)
        .build()
        .create()
}
