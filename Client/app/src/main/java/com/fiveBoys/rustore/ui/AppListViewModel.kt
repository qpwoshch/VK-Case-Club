package com.fiveBoys.rustore.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.fiveBoys.rustore.network.AppDto
import com.fiveBoys.rustore.repo.AppsRepository

class AppListViewModel(private val repo: AppsRepository) : ViewModel() {
    private var currentCategory: String? = null
    private val _apps = MutableStateFlow<List<AppDto>>(emptyList())
    val apps: StateFlow<List<AppDto>> = _apps

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private var allApps: List<AppDto> = emptyList()
    private var currentPage = 0
    private val pageSize = 10

    init {
        loadInitialData()
    }

    fun refresh(category: String? = null) {
        currentCategory = category
        loadInitialData()
    }

    private fun loadInitialData() {
        if (_loading.value) return
        _loading.value = true

        viewModelScope.launch {
            runCatching {
                if (currentCategory != null) {
                    repo.loadAppsByCategory(currentCategory!!)
                } else {
                    repo.loadAllApps()
                }
            }
                .onSuccess { apps ->
                    allApps = apps
                    currentPage = 1
                    _apps.value = allApps.take(pageSize)
                }
                .onFailure {
                    it.printStackTrace()
                }
            _loading.value = false
        }
    }

    fun loadMore() {
        if (_loading.value) return

        val nextPage = currentPage + 1
        val startIndex = currentPage * pageSize
        val endIndex = minOf(startIndex + pageSize, allApps.size)

        if (startIndex >= allApps.size) return

        _loading.value = true
        viewModelScope.launch {
            val newItems = allApps.subList(startIndex, endIndex)
            _apps.value = _apps.value + newItems
            currentPage = nextPage
            _loading.value = false
        }
    }
}