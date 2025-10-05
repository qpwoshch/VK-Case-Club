package com.fiveBoys.rustore.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.first
import com.fiveBoys.rustore.data.OnboardingStore

@Composable
fun SplashRoute(
    onboardingStore: OnboardingStore,
    onFirstLaunch: () -> Unit,
    onSkip: () -> Unit
) {
    LaunchedEffect(Unit) {
        val shown = onboardingStore.isShown.first()
        if (shown) onSkip() else onFirstLaunch()
    }
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}
