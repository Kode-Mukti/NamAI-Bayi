# 04 — Navigation

## Summary

Complete navigation architecture using Jetpack Navigation Compose. Defines every route, argument, transition, and the navigation graph structure.

## Reasoning

Navigation is owned by the Navigation Agent. Feature agents only need to know their screen's route and how to navigate to/from it. Centralizing navigation prevents duplicate route definitions.

## Recommendation

### Screen Routes

```kotlin
// core/ui/navigation/Screen.kt
sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Onboarding : Screen("onboarding")
    object Home : Screen("home")
    object Generate : Screen("generate")
    object Loading : Screen("loading/{resultId}") {
        fun createRoute(resultId: String) = "loading/$resultId"
    }
    object Result : Screen("result/{resultId}") {
        fun createRoute(resultId: String) = "result/$resultId"
    }
    object History : Screen("history")
    object Favorite : Screen("favorite")
    object Settings : Screen("settings")
    object About : Screen("about")
}
```

### Navigation Graph

```
┌──────────┐
│  Splash  │
└────┬─────┘
     │ condition: isOnboardingCompleted
     ├───────────────┐
     ▼               ▼
┌──────────┐  ┌──────────────┐
│  Home    │  │  Onboarding  │
└────┬─────┘  └──────┬───────┘
     │                │
     │                │ (after completion)
     │                ▼
     │           ┌──────────┐
     │           │  Home    │
     │           └────┬─────┘
     │                 │
     ▼                 ▼
┌──────────┐     ┌──────────┐
│ Generate │     │ History  │
└────┬─────┘     └──────────┘
     │                 ▲
     ▼                 │
┌──────────┐     ┌──────────┐
│ Loading  │     │ Favorite │
└────┬─────┘     └──────────┘
     │                 ▲
     ▼                 │
┌──────────┐     ┌──────────┐
│  Result  │     │ Settings │
└────┬─────┘     └──────────┘
     │                 ▲
     ├─────────────────┤
     │                 │
     ▼                 │
┌──────────┐           │
│  Home    │           │
│(restart) │           │
└──────────┘           │
                       │
                  ┌──────────┐
                  │  About   │
                  └──────────┘
```

### Bottom Navigation (Home, History, Favorite, Settings)

```
┌────────────────────────────────────────┐
│                                        │
│          Content Area                  │
│                                        │
├──────┬──────┬──────┬──────┬────────────┤
│ Home │Generate│History│Fav  │ Settings  │
└──────┴──────┴──────┴──────┴────────────┘
```

Note: Generate is a FAB action (not bottom tab), but tabs are: Home, History, Favorite, Settings.

### Navigation Arguments

```kotlin
// core/ui/navigation/NavArgs.kt
object NavArgs {
    const val RESULT_ID = "resultId"
}
```

### Navigation Actions per Screen

| Screen | Navigates To | Condition | Animation |
|--------|-------------|-----------|-----------|
| Splash | Onboarding | `onboardingNotCompleted` | Fade in |
| Splash | Home | `onboardingCompleted` | Fade in |
| Onboarding | Home | All pages viewed + tap "Mulai" | Slide left |
| Home | Generate | Tap "Mulai Konsultasi" | Slide up |
| Home | History | Tap History tab | None (bottom nav) |
| Home | Favorite | Tap Favorite tab | None (bottom nav) |
| Home | Settings | Tap Settings tab | None (bottom nav) |
| Generate | Loading | Submit form (generate ID) | Slide up |
| Loading | Result | AI response received | Fade through |
| Loading | Generate | AI error + user retries | Slide down |
| Result | Home | Tap "Konsultasi Lagi" | Pop to Home |
| Result | Favorite | Tap save heart | None (in-place state) |
| Result | Share (external) | Tap WA/IG icon | System sheet |
| History | Result | Tap history item | Slide right |
| Favorite | Result (optional) | Tap favorite item | Slide right |
| Settings | About | Tap "Tentang" | Slide right |
| Settings | Home | Change language | Restart activity |

### NavGraph Implementation Contract

```kotlin
// core/ui/navigation/NavGraph.kt
@Composable
fun NamAINavGraph(
    navController: NavHostController,
    startDestination: String   // "home" or "onboarding"
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Each screen agent registers their composable here
        composable(Screen.Splash.route) { SplashScreen(navController) }
        composable(Screen.Onboarding.route) { OnboardingScreen(navController) }
        composable(Screen.Home.route) { HomeScreen(navController) }
        composable(Screen.Generate.route) { GenerateScreen(navController) }
        composable(
            route = Screen.Loading.route,
            arguments = listOf(navArgument(NavArgs.RESULT_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            val resultId = backStackEntry.arguments?.getString(NavArgs.RESULT_ID) ?: return@composable
            LoadingScreen(navController, resultId)
        }
        composable(
            route = Screen.Result.route,
            arguments = listOf(navArgument(NavArgs.RESULT_ID) { type = NavType.StringType })
        ) { backStackEntry ->
            val resultId = backStackEntry.arguments?.getString(NavArgs.RESULT_ID) ?: return@composable
            ResultScreen(navController, resultId)
        }
        composable(Screen.History.route) { HistoryScreen(navController) }
        composable(Screen.Favorite.route) { FavoriteScreen(navController) }
        composable(Screen.Settings.route) { SettingsScreen(navController) }
        composable(Screen.About.route) { AboutScreen(navController) }
    }
}
```

### Deep Link Support (Future)

```kotlin
// Deep links for public share
composable(
    route = "share/{resultId}",
    arguments = listOf(navArgument("resultId") { type = NavType.StringType }),
    deepLinks = listOf(navDeepLink { uriPattern = "namaibayi://share/{resultId}" })
) { ... }
```

### Transition Animations

| Transition | Enter | Exit | Pop Enter | Pop Exit |
|-----------|-------|------|-----------|----------|
| Bottom nav | `fadeIn` | `fadeOut` | `fadeIn` | `fadeOut` |
| Push (forward) | `slideInHorizontally` | `slideOutHorizontally` | `slideInHorizontally` | `slideOutHorizontally` |
| Modal (Generate) | `slideInVertically` | `slideOutVertically` | `slideInVertically` | `slideOutVertically` |
| Full (Loading) | `fadeThrough` | `fadeThrough` | `fadeThrough` | `fadeThrough` |

### Agent Handoff

| Agent | Creates | Receives |
|-------|---------|----------|
| Navigation Agent | `Screen.kt`, `NavArgs.kt`, `NavGraph.kt` | — |
| Theme Agent | Theme colors (no nav dependency) | — |
| Screen: Splash | `SplashScreen(navController)` | `Screen.Splash.route` |
| Screen: Onboarding | `OnboardingScreen(navController)` | `Screen.Onboarding.route` |
| Screen: Home | `HomeScreen(navController)` | `Screen.Home.route` |
| Screen: Generate | `GenerateScreen(navController)` | `Screen.Generate.route` |
| Screen: Loading | `LoadingScreen(navController, resultId)` | `Screen.Loading.route` |
| Screen: Result | `ResultScreen(navController, resultId)` | `Screen.Result.route` |
| Screen: History | `HistoryScreen(navController)` | `Screen.History.route` |
| Screen: Favorite | `FavoriteScreen(navController)` | `Screen.Favorite.route` |
| Screen: Settings | `SettingsScreen(navController)` | `Screen.Settings.route` |
