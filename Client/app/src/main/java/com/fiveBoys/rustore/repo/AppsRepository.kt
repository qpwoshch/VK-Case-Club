package com.fiveBoys.rustore.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.fiveBoys.rustore.network.ApiService
import com.fiveBoys.rustore.network.AppDto
import com.fiveBoys.rustore.network.CategoryDto

class AppsRepository(private val api: ApiService) {
    private val pageSize = 10  // Размер страницы для ленивой загрузки

    suspend fun loadAllApps(): List<AppDto> = withContext(Dispatchers.IO) {
        val response = api.getApps()
        response.apps
    }

//    suspend fun categories(): List<CategoryDto> = withContext(Dispatchers.IO) {
//        api.getCategories()
//    }
}