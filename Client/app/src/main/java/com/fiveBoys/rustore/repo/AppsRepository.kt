package com.fiveBoys.rustore.repo

import com.fiveBoys.rustore.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.fiveBoys.rustore.network.ApiService
import com.fiveBoys.rustore.network.AppDto
import com.fiveBoys.rustore.network.CategoryDto

class AppsRepository(private val api: ApiService) {
    suspend fun loadAllApps(): List<AppDto> = withContext(Dispatchers.IO) {
        api.getApps().apps
    }

    suspend fun loadCategories(): List<CategoryDto> = withContext(Dispatchers.IO) {
        api.getCategories()
    }

    suspend fun loadAppsByCategory(category: String): List<AppDto> = withContext(Dispatchers.IO) {
        api.getAppsByCategory(category).apps
    }

    suspend fun getAppById(id: String): App = withContext(Dispatchers.IO) {
        val opener = com.fiveBoys.rustore.AppOpener()
        opener.open(id)
    }

}