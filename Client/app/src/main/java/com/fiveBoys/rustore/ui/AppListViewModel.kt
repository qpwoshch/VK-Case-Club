package com.fiveBoys.rustore.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.fiveBoys.rustore.network.AppDto
import com.fiveBoys.rustore.repo.AppsRepository

class AppListViewModel(private val repo: AppsRepository) : ViewModel() {

    private val _apps = MutableStateFlow<List<AppDto>>(emptyList())
    val apps: StateFlow<List<AppDto>> = _apps

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private var nextPage: Int? = 1
    private var currentCategory: String? = null

    fun refresh(category: String?) {
        currentCategory = category
        nextPage = 1
        _apps.value = emptyList()
        loadMore()
    }

    fun loadMore() {
        val page = nextPage ?: return
        if (_loading.value) return
        _loading.value = true
        viewModelScope.launch {
            runCatching { repo.loadPage(page, currentCategory) }
                .onSuccess {
                    _apps.value = _apps.value + it.items
                    nextPage = it.nextPage
                }
                .onFailure {
                    // для MVP просто глушим; можно отдать ошибку в UI
                }
            _loading.value = false
        }
    }
}
