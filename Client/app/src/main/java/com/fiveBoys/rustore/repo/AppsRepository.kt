package com.fiveBoys.rustore.repo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.fiveBoys.rustore.network.ApiService
import com.fiveBoys.rustore.network.AppDto
import com.fiveBoys.rustore.network.CategoryDto

class AppsRepository(private val api: ApiService) {
    private val pageSize = 20

    suspend fun loadPage(page: Int, category: String?): PageResult =
        withContext(Dispatchers.IO) {
            val r = api.getApps(page = page, pageSize = pageSize, category = category)
            PageResult(
                items = r.items,
                nextPage = if (r.hasNext) r.page + 1 else null
            )
        }

    suspend fun categories(): List<CategoryDto> = withContext(Dispatchers.IO) { api.getCategories() }

    data class PageResult(val items: List<AppDto>, val nextPage: Int?)
}
