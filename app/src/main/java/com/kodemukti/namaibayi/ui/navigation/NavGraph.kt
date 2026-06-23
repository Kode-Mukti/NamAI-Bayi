package com.kodemukti.namaibayi.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.kodemukti.namaibayi.core.navigation.NavArgs
import com.kodemukti.namaibayi.core.navigation.Screen
import com.kodemukti.namaibayi.ui.screens.about.AboutScreen
import com.kodemukti.namaibayi.ui.screens.detail.DetailScreen
import com.kodemukti.namaibayi.ui.screens.favorites.FavoritesScreen
import com.kodemukti.namaibayi.ui.screens.generate.GenerateScreen
import com.kodemukti.namaibayi.ui.screens.history.HistoryScreen
import com.kodemukti.namaibayi.ui.screens.home.HomeScreen
import com.kodemukti.namaibayi.ui.screens.result.ResultScreen
import com.kodemukti.namaibayi.ui.screens.settings.SettingsScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier,
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToGenerate = {
                    navController.navigate(Screen.Generate.route)
                },
                onNavigateToResult = { id ->
                    navController.navigate(Screen.Result.createRoute(id))
                },
            )
        }
        composable(Screen.Generate.route) {
            GenerateScreen(
                onNavigateToResult = { id ->
                    navController.navigate(Screen.Result.createRoute(id)) {
                        popUpTo(Screen.Home.route) { inclusive = false }
                    }
                },
            )
        }
        composable(
            route = Screen.Result.route,
            arguments = listOf(navArgument(NavArgs.BABY_PROFILE_ID) { type = NavType.StringType }),
        ) { backStackEntry ->
            val babyProfileId = backStackEntry.arguments?.getString(NavArgs.BABY_PROFILE_ID) ?: ""
            ResultScreen(
                babyProfileId = babyProfileId,
                onNavigateToDetail = { id ->
                    navController.navigate(Screen.Detail.createRoute(id))
                },
            )
        }
        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument(NavArgs.NAME_RECOMMENDATION_ID) { type = NavType.StringType }),
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString(NavArgs.NAME_RECOMMENDATION_ID) ?: ""
            DetailScreen(
                nameRecommendationId = id,
                onBack = { navController.popBackStack() },
            )
        }
        composable(Screen.Favorites.route) {
            FavoritesScreen(
                onNavigateToDetail = { id ->
                    navController.navigate(Screen.Detail.createRoute(id))
                },
            )
        }
        composable(Screen.History.route) {
            HistoryScreen(
                onNavigateToResult = { id ->
                    navController.navigate(Screen.Result.createRoute(id))
                },
            )
        }
        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateToAbout = {
                    navController.navigate(Screen.About.route)
                },
            )
        }
        composable(Screen.About.route) {
            AboutScreen(
                onBack = { navController.popBackStack() },
            )
        }
    }
}
