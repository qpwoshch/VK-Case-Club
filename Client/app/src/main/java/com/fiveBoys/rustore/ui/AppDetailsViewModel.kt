package com.fiveBoys.rustore.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fiveBoys.rustore.App
import com.fiveBoys.rustore.repo.AppsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AppDetailsViewModel(
    private val repo: AppsRepository,
    private val appId: String
) : ViewModel() {

    sealed interface Ui {
        data object Loading : Ui
        data class Ready(val app: App) : Ui
        data class Error(val message: String) : Ui
    }

    private val _ui = MutableStateFlow<Ui>(Ui.Loading)
    val ui: StateFlow<Ui> = _ui

    init {
        viewModelScope.launch {
            try {
                val app = repo.getAppById(appId)
                _ui.value = Ui.Ready(app)
            } catch (t: Throwable) {
                _ui.value = Ui.Error(t.message ?: "Не удалось загрузить приложение")
            }
        }
    }
}
