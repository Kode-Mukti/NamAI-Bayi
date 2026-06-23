package com.kodemukti.namaibayi.core.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.AutoAwesome
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(
    val route: String,
    val title: String = "",
    val icon: ImageVector? = null,
) {
    data object Home : Screen("home", "Beranda", Icons.Default.Home)
    data object Generate : Screen("generate", "Cari Nama", Icons.Outlined.AutoAwesome)
    data object Result : Screen("result/{babyProfileId}", "Hasil") {
        fun createRoute(babyProfileId: String) = "result/$babyProfileId"
    }
    data object Detail : Screen("detail/{nameRecommendationId}", "Detail") {
        fun createRoute(nameRecommendationId: String) = "detail/$nameRecommendationId"
    }
    data object Favorites : Screen("favorites", "Favorit", Icons.Default.Favorite)
    data object History : Screen("history", "Riwayat", Icons.Default.History)
    data object Settings : Screen("settings", "Pengaturan", Icons.Default.Settings)
    data object About : Screen("about", "Tentang")

    companion object {
        val bottomNavItems = listOf(Home, Generate, Favorites, History, Settings)
    }
}
