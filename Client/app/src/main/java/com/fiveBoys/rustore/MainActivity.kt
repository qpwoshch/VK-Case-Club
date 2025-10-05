package com.fiveBoys.rustore

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.fiveBoys.rustore.data.OnboardingStore
import com.fiveBoys.rustore.network.provideApi
import com.fiveBoys.rustore.repo.AppsRepository
import com.fiveBoys.rustore.ui.*
import com.fiveBoys.rustore.ui.theme.RuStoreTheme
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val api = provideApi()
        val repo = AppsRepository(api)
        val onboardingStore = OnboardingStore(this)

        setContent {
            RuStoreTheme {
                val nav = rememberNavController()

                val listVm = viewModel<AppListViewModel>(
                    factory = vmFactory { AppListViewModel(repo) }
                )
                val catVm = viewModel<CategoriesViewModel>(
                    factory = vmFactory { CategoriesViewModel(repo) }
                )

                NavHost(navController = nav, startDestination = Routes.SPLASH) {
                    composable(Routes.SPLASH) {
                        SplashRoute(
                            onboardingStore = onboardingStore,
                            onFirstLaunch = { nav.navigate(Routes.ONBOARDING) { popUpTo(0) } },
                            onSkip = { nav.navigate(Routes.FEED) { popUpTo(0) } }
                        )
                    }
                    composable(Routes.ONBOARDING) {
                        OnboardingScreen(
                            onContinue = {
                                nav.navigate(Routes.FEED) { popUpTo(0) }
                            },
                            markShown = {
                                GlobalScope.launch {
                                    onboardingStore.setShown()
                                }
                            }
                        )
                    }
                    composable(
                        route = "${Routes.FEED}?category={category}",
                        arguments = listOf(navArgument("category") {
                            type = NavType.StringType
                            nullable = true
                            defaultValue = null
                        })
                    ) { backStackEntry ->
                        val category = backStackEntry.arguments?.getString("category")
                        // Обновляем ViewModel при смене категории
                        LaunchedEffect(category) {
                            listVm.refresh(category)
                        }
                        AppListScreen(
                            nav = nav,
                            viewModel = listVm,
                            category = category,
                            onOpenCategories = { nav.navigate(Routes.CATEGORIES) },
                            onOpenDetails = { id -> nav.navigate("${Routes.DETAILS}/$id") }
                        )
                    }
                    composable(Routes.CATEGORIES) {
                        CategoriesScreen(
                            nav = nav,
                            viewModel = catVm,
                            onPick = { cat ->
                                nav.navigate("${Routes.FEED}?category=$cat") {
                                    popUpTo(Routes.FEED) { inclusive = true }
                                }
                            }
                        )
                    }
                    composable(
                        route = "${Routes.DETAILS}/{id}",
                        arguments = listOf(navArgument("id") { type = NavType.StringType })
                    ) { entry ->
                        val id = entry.arguments?.getString("id") ?: return@composable

                        val detailsVm = viewModel<AppDetailsViewModel>(
                            factory = vmFactory { AppDetailsViewModel(repo, id) }
                        )

                        AppDetailsRoute(
                            navController = nav,
                            viewModel = detailsVm
                        )
                    }
                }
            }
        }
    }
}