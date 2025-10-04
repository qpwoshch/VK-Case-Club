package com.fiveBoys.rustore.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.fiveBoys.rustore.network.CategoryDto
import com.fiveBoys.rustore.repo.AppsRepository

class CategoriesViewModel(private val repo: AppsRepository): ViewModel() {
    private val _cats = MutableStateFlow<List<CategoryDto>>(emptyList())
    val cats: StateFlow<List<CategoryDto>> = _cats

//    fun load() {
//        if (_cats.value.isNotEmpty()) return
//        viewModelScope.launch {
//            runCatching { repo.categories() }
//                .onSuccess { _cats.value = it }
//        }
//    }
}
