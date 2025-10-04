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
    const val BASE_URL = "http://192.168.0.115:8080"
    const val APPS_PATH = "/apps/start"  // Изменили путь
    const val CATEGORIES_PATH = "/categories"
}

interface ApiService {
    @GET(ApiConfig.APPS_PATH)
    suspend fun getApps(): AppsResponse  // Убрали пагинацию, так как сервер отдает все сразу

//    @GET(ApiConfig.CATEGORIES_PATH)
//    suspend fun getCategories(): List<CategoryDto>
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