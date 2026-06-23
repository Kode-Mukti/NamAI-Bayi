# 01 — Project Structure

## Summary

Complete Android project folder structure for NamAI Bayi using Clean Architecture (presentation / domain / data / core). Every agent will work within their assigned module without crossing boundaries.

## Reasoning

A rigid, predictable folder structure is essential for Multi-Agent Development. Each agent must know exactly where to place files and where to find dependencies. No ambiguity means no merge conflicts.

## Recommendation

```
NamAI-Bayi/
├── app/
│   ├── build.gradle.kts
│   ├── proguard-rules.pro
│   └── src/
│       ├── main/
│       │   ├── AndroidManifest.xml
│       │   ├── java/com/kodemukti/namaibayi/
│       │   │   ├── NamAIApplication.kt          # Application class
│       │   │   ├── MainActivity.kt               # Single Activity host
│       │   │   │
│       │   │   ├── core/
│       │   │   │   ├── common/
│       │   │   │   │   ├── Constants.kt          # App-wide constants
│       │   │   │   │   ├── Extensions.kt         # Kotlin extension functions
│       │   │   │   │   ├── Result.kt             # Sealed Result wrapper
│       │   │   │   │   └── UiState.kt            # Generic UI state wrapper
│       │   │   │   ├── di/
│       │   │   │   │   ├── AppModule.kt          # App-level DI
│       │   │   │   │   ├── DataModule.kt         # Data layer DI
│       │   │   │   │   ├── DomainModule.kt       # Domain layer DI
│       │   │   │   │   └── AIModule.kt           # AI provider DI
│       │   │   │   ├── network/
│       │   │   │   │   ├── OpenRouterApi.kt      # Retrofit interface
│       │   │   │   │   ├── NetworkConfig.kt      # Timeout, base URL
│       │   │   │   │   └── ApiInterceptor.kt     # Auth header interceptor
│       │   │   │   ├── database/
│       │   │   │   │   ├── NamAIDatabase.kt      # Room database
│       │   │   │   │   ├── Converters.kt         # Type converters
│       │   │   │   │   └── DatabaseConfig.kt     # DB name, version
│       │   │   │   ├── ui/
│       │   │   │   │   ├── theme/
│       │   │   │   │   │   ├── Theme.kt          # Material3 theme
│       │   │   │   │   │   ├── Color.kt          # Color tokens
│       │   │   │   │   │   ├── Type.kt           # Typography
│       │   │   │   │   │   ├── Shape.kt          # Corner radii
│       │   │   │   │   │   └── Elevation.kt      # Elevation tokens
│       │   │   │   │   ├── component/
│       │   │   │   │   │   ├── PrimaryButton.kt
│       │   │   │   │   │   ├── SecondaryButton.kt
│       │   │   │   │   │   ├── NameCard.kt
│       │   │   │   │   │   ├── ResultCard.kt
│       │   │   │   │   │   ├── LoadingAnimation.kt
│       │   │   │   │   │   ├── ProgressIndicator.kt
│       │   │   │   │   │   ├── Chip.kt
│       │   │   │   │   │   ├── ScoreBar.kt
│       │   │   │   │   │   ├── SectionHeader.kt
│       │   │   │   │   │   ├── TopBar.kt
│       │   │   │   │   │   ├── BottomBar.kt
│       │   │   │   │   │   ├── EmptyState.kt
│       │   │   │   │   │   └── ErrorState.kt
│       │   │   │   │   └── navigation/
│       │   │   │   │       ├── NavGraph.kt       # Navigation host
│       │   │   │   │       ├── Screen.kt         # Route sealed class
│       │   │   │   │       └── NavArgs.kt        # Nav argument constants
│       │   │   │   └── util/
│       │   │   │       ├── ShareUtil.kt          # Share to WA/IG
│       │   │   │       ├── DateUtil.kt           # Date formatting
│       │   │   │       ├── ValidationUtil.kt     # Input validation
│       │   │   │       └── JsonUtil.kt           # JSON parsing helpers
│       │   │   │
│       │   │   ├── data/
│       │   │   │   ├── remote/
│       │   │   │   │   ├── dto/
│       │   │   │   │   │   ├── OpenRouterRequest.kt
│       │   │   │   │   │   ├── OpenRouterResponse.kt
│       │   │   │   │   │   └── AIErrorResponse.kt
│       │   │   │   │   └── ai/
│       │   │   │   │       ├── AIProvider.kt          # Provider interface
│       │   │   │   │       ├── OpenRouterProvider.kt  # OpenRouter impl
│       │   │   │   │       └── MockAIProvider.kt      # For testing
│       │   │   │   ├── local/
│       │   │   │   │   ├── entity/
│       │   │   │   │   │   ├── BabyResultEntity.kt
│       │   │   │   │   │   ├── FavoriteEntity.kt
│       │   │   │   │   │   └── HistoryEntity.kt
│       │   │   │   │   ├── dao/
│       │   │   │   │   │   ├── BabyResultDao.kt
│       │   │   │   │   │   ├── FavoriteDao.kt
│       │   │   │   │   │   └── HistoryDao.kt
│       │   │   │   │   └── datastore/
│       │   │   │   │       ├── SettingsDataStore.kt
│       │   │   │   │       └── OnboardingDataStore.kt
│       │   │   │   └── repository/
│       │   │   │       ├── GenerateRepositoryImpl.kt
│       │   │   │       ├── HistoryRepositoryImpl.kt
│       │   │   │       ├── FavoriteRepositoryImpl.kt
│       │   │   │       └── SettingsRepositoryImpl.kt
│       │   │   │
│       │   │   ├── domain/
│       │   │   │   ├── model/
│       │   │   │   │   ├── BabyProfile.kt
│       │   │   │   │   ├── BabyResult.kt
│       │   │   │   │   ├── NameRecommendation.kt
│       │   │   │   │   ├── FavoriteName.kt
│       │   │   │   │   ├── HistoryItem.kt
│       │   │   │   │   ├── AIPromptRequest.kt
│       │   │   │   │   ├── AIResponse.kt
│       │   │   │   │   ├── Settings.kt
│       │   │   │   │   └── NamingStrategy.kt
│       │   │   │   ├── repository/
│       │   │   │   │   ├── GenerateRepository.kt    # Interface
│       │   │   │   │   ├── HistoryRepository.kt     # Interface
│       │   │   │   │   ├── FavoriteRepository.kt    # Interface
│       │   │   │   │   └── SettingsRepository.kt    # Interface
│       │   │   │   └── usecase/
│       │   │   │       ├── GenerateBabyName.kt
│       │   │   │       ├── GetHistory.kt
│       │   │   │       ├── DeleteHistory.kt
│       │   │   │       ├── ClearHistory.kt
│       │   │   │       ├── GetFavorites.kt
│       │   │   │       ├── ToggleFavorite.kt
│       │   │   │       ├── IsFavorite.kt
│       │   │   │       ├── GetSettings.kt
│       │   │   │       ├── UpdateSettings.kt
│       │   │   │       └── ExportResult.kt
│       │   │   │
│       │   │   └── presentation/
│       │   │       ├── splash/
│       │   │       │   ├── SplashScreen.kt
│       │   │       │   ├── SplashViewModel.kt
│       │   │       │   └── SplashState.kt
│       │   │       ├── onboarding/
│       │   │       │   ├── OnboardingScreen.kt
│       │   │       │   ├── OnboardingViewModel.kt
│       │   │       │   ├── OnboardingState.kt
│       │   │       │   └── OnboardingPage.kt
│       │   │       ├── home/
│       │   │       │   ├── HomeScreen.kt
│       │   │       │   ├── HomeViewModel.kt
│       │   │       │   └── HomeState.kt
│       │   │       ├── generate/
│       │   │       │   ├── GenerateScreen.kt
│       │   │       │   ├── GenerateViewModel.kt
│       │   │       │   └── GenerateState.kt
│       │   │       ├── loading/
│       │   │       │   ├── LoadingScreen.kt
│       │   │       │   ├── LoadingViewModel.kt
│       │   │       │   └── LoadingState.kt
│       │   │       ├── result/
│       │   │       │   ├── ResultScreen.kt
│       │   │       │   ├── ResultViewModel.kt
│       │   │       │   └── ResultState.kt
│       │   │       ├── history/
│       │   │       │   ├── HistoryScreen.kt
│       │   │       │   ├── HistoryViewModel.kt
│       │   │       │   └── HistoryState.kt
│       │   │       ├── favorite/
│       │   │       │   ├── FavoriteScreen.kt
│       │   │       │   ├── FavoriteViewModel.kt
│       │   │       │   └── FavoriteState.kt
│       │   │       └── settings/
│       │   │           ├── SettingsScreen.kt
│       │   │           ├── SettingsViewModel.kt
│       │   │           └── SettingsState.kt
│       │   │
│       │   └── res/
│       │       ├── drawable/
│       │       │   ├── ic_splash_logo.xml
│       │       │   ├── ic_empty_state.xml
│       │       │   ├── img_onboarding_1.xml
│       │       │   ├── img_onboarding_2.xml
│       │       │   ├── img_onboarding_3.xml
│       │       │   ├── img_loading_animation.xml
│       │       │   └── ic_launcher_foreground.xml
│       │       ├── values/
│       │       │   ├── strings.xml
│       │       │   ├── colors.xml
│       │       │   └── themes.xml
│       │       ├── values-night/
│       │       │   └── themes.xml
│       │       ├── font/
│       │       │   └── plus_jakarta_sans_*.ttf
│       │       └── mipmap-*/
│       │
│       └── test/
│           └── java/com/kodemukti/namaibayi/
│               ├── domain/usecase/
│               │   ├── GenerateBabyNameTest.kt
│               │   ├── ToggleFavoriteTest.kt
│               │   └── GetHistoryTest.kt
│               ├── data/repository/
│               │   ├── GenerateRepositoryImplTest.kt
│               │   └── FavoriteRepositoryImplTest.kt
│               └── presentation/
│                   ├── GenerateViewModelTest.kt
│                   └── ResultViewModelTest.kt
├── build.gradle.kts                            # Project-level
├── settings.gradle.kts                         # Module includes
├── gradle.properties
├── gradle/
│   └── libs.versions.toml                      # Version catalog
└── .github/
    └── workflows/
        └── build.yml                           # CI pipeline
```

## Design Decisions for Multi-Agent

| Decision | Why |
|----------|-----|
| One module (no :core, :domain modules) | Simplifies navigation for agents; each agent works in a package |
| Package by feature + layer | Agents can own `presentation/generate/*` entirely |
| Interfaces in domain, impl in data | Data agent works independently from domain agent |
| `core/ui/component/` shared | UI Agent owns all reusable components; other agents consume them |
| `core/navigation/` shared | Navigation Agent owns all routes; feature agents only register screens |
| Version catalog in `libs.versions.toml` | Single source of truth; no agent guesses dependencies |

## Agent-to-Package Mapping

| Agent | Owns | Consumes From |
|-------|------|---------------|
| UI Agent | `core/ui/theme/`, `core/ui/component/` | — |
| Navigation Agent | `core/ui/navigation/` | `core/ui/theme/` |
| Data Agent | `data/` | `core/network/`, `core/database/` |
| Domain Agent | `domain/` | — |
| AI/Prompt Agent | `data/remote/ai/`, `domain/model/` | `data/remote/dto/` |
| Screen Agents | Individual `presentation/*/` | All above |
| Testing Agent | `test/` | All above |

## Future Agents

| Feature | New Package |
|---------|-------------|
| Couple Mode | `presentation/couple/` |
| Name Battle | `presentation/battle/` |
| Community | `presentation/community/` |
| Premium | `presentation/premium/` |
