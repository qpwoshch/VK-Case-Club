package com.fiveBoys.rustore.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.settingsDataStore by preferencesDataStore(name = "settings")

class OnboardingStore(private val context: Context) {
    private val KEY_SHOWN = booleanPreferencesKey("onboarding_shown")

    val isShown: Flow<Boolean> = context.settingsDataStore.data.map { it[KEY_SHOWN] ?: false }

    suspend fun setShown() {
        context.settingsDataStore.edit { it[KEY_SHOWN] = true }
    }
}
