# 11 — Task Breakdown

## Summary

Complete task breakdown for Multi-Agent Development. Each task is granular enough for a single agent to complete independently in 1-3 days.

## Reasoning

Tasks must be:
1. **Independent** — no cross-agent blocking
2. **Testable** — each task has clear acceptance criteria
3. **Small** — 1-3 days per task
4. **Atomic** — one responsibility per task

---

### Build Config & Foundation

| ID | Task | Agent | Dependencies | Est. Days |
|----|------|-------|-------------|-----------|
| T-001 | Create Android project with version catalog | Architecture | — | 1 |
| T-002 | Configure build.gradle.kts with all dependencies | Architecture | T-001 | 1 |
| T-003 | Set up Hilt DI modules (AppModule, DataModule, DomainModule, AIModule) | Architecture | T-001, T-002 | 1 |
| T-004 | Create NamAIApplication.kt | Architecture | T-003 | 0.5 |
| T-005 | Create MainActivity.kt (single activity host) | Architecture | T-004 | 0.5 |
| T-006 | Create Constants.kt, Extensions.kt, Result.kt, UiState.kt | Architecture | T-001 | 1 |

### Theme & Design System

| ID | Task | Agent | Dependencies | Est. Days |
|----|------|-------|-------------|-----------|
| T-007 | Define Color.kt (light + dark palette) | Theme | T-001 | 1 |
| T-008 | Define Type.kt (typography scale) | Theme | T-001 | 0.5 |
| T-009 | Define Shape.kt (corner radii) | Theme | T-001 | 0.5 |
| T-010 | Define Elevation.kt | Theme | T-001 | 0.5 |
| T-011 | Create Theme.kt (Material3 theme with dark mode) | Theme | T-007, T-008, T-009, T-010 | 1 |
| T-012 | Set up font resources (Plus Jakarta Sans) | Theme | T-001 | 0.5 |

### Navigation

| ID | Task | Agent | Dependencies | Est. Days |
|----|------|-------|-------------|-----------|
| T-013 | Create Screen.kt (sealed class with all routes) | Navigation | T-001 | 0.5 |
| T-014 | Create NavArgs.kt | Navigation | T-013 | 0.5 |
| T-015 | Create NavGraph.kt with all composable destinations | Navigation | T-013, T-014, T-011 | 1 |

### Domain Layer

| ID | Task | Agent | Dependencies | Est. Days |
|----|------|-------|-------------|-----------|
| T-016 | Create domain/model/BabyProfile.kt | Domain | T-001 | 0.5 |
| T-017 | Create domain/model/BabyResult.kt | Domain | T-016 | 0.5 |
| T-018 | Create domain/model/NameRecommendation.kt | Domain | T-016 | 0.5 |
| T-019 | Create domain/model/NamingStrategy.kt | Domain | T-001 | 0.5 |
| T-020 | Create domain/model/FavoriteName.kt | Domain | T-001 | 0.5 |
| T-021 | Create domain/model/HistoryItem.kt | Domain | T-001 | 0.5 |
| T-022 | Create domain/model/Settings.kt | Domain | T-001 | 0.5 |
| T-023 | Create domain/model/AIPromptRequest.kt, AIResponse.kt | Domain | T-001 | 0.5 |
| T-024 | Create GenerateRepository interface | Domain | T-016, T-023 | 0.5 |
| T-025 | Create HistoryRepository interface | Domain | T-021 | 0.5 |
| T-026 | Create FavoriteRepository interface | Domain | T-020 | 0.5 |
| T-027 | Create SettingsRepository interface | Domain | T-022 | 0.5 |
| T-028 | Create GenerateBabyName use case | Domain | T-024, T-025, T-027 | 1 |
| T-029 | Create GetHistory use case | Domain | T-025 | 0.5 |
| T-030 | Create DeleteHistory use case | Domain | T-025 | 0.5 |
| T-031 | Create ClearHistory use case | Domain | T-025 | 0.5 |
| T-032 | Create GetFavorites use case | Domain | T-026 | 0.5 |
| T-033 | Create ToggleFavorite use case | Domain | T-026 | 0.5 |
| T-034 | Create IsFavorite use case | Domain | T-026 | 0.5 |
| T-035 | Create GetSettings use case | Domain | T-027 | 0.5 |
| T-036 | Create UpdateSettings use case | Domain | T-027 | 0.5 |
| T-037 | Create ExportResult use case | Domain | T-018 | 0.5 |

### Data Layer — Network

| ID | Task | Agent | Dependencies | Est. Days |
|----|------|-------|-------------|-----------|
| T-038 | Create core/database/NamAIDatabase.kt | Data | T-001 | 0.5 |
| T-039 | Create core/database/Converters.kt | Data | T-038 | 0.5 |
| T-040 | Create core/network/NetworkConfig.kt | Data | T-001 | 0.5 |
| T-041 | Create core/network/ApiInterceptor.kt (OpenRouter auth) | Data | T-040 | 0.5 |
| T-042 | Create core/network/OpenRouterApi.kt (Retrofit interface) | Data | T-041 | 1 |
| T-043 | Create data/remote/dto/OpenRouterRequest.kt | Data | T-042 | 0.5 |
| T-044 | Create data/remote/dto/OpenRouterResponse.kt | Data | T-042 | 0.5 |
| T-045 | Create data/remote/ai/AIProvider.kt interface | AI/Prompt | T-001 | 0.5 |
| T-046 | Create data/remote/ai/OpenRouterProvider.kt | AI/Prompt | T-045, T-042, T-043, T-044 | 2 |
| T-047 | Create data/remote/ai/MockAIProvider.kt | AI/Prompt | T-045 | 1 |

### Data Layer — Local Storage

| ID | Task | Agent | Dependencies | Est. Days |
|----|------|-------|-------------|-----------|
| T-048 | Create domain model → Room entity mappers | Data | T-016, T-017, T-020, T-021 | 1 |
| T-049 | Create Room entities (BabyResultEntity, FavoriteEntity, HistoryEntity) | Data | T-038, T-048 | 1 |
| T-050 | Create Room DAOs (BabyResultDao, FavoriteDao, HistoryDao) | Data | T-049 | 1 |
| T-051 | Create data/local/datastore/SettingsDataStore.kt | Data | T-022 | 1 |
| T-052 | Create data/local/datastore/OnboardingDataStore.kt | Data | T-001 | 0.5 |

### Data Layer — Repository Implementation

| ID | Task | Agent | Dependencies | Est. Days |
|----|------|-------|-------------|-----------|
| T-053 | Create GenerateRepositoryImpl | Data | T-024, T-046, T-050, T-051 | 2 |
| T-054 | Create HistoryRepositoryImpl | Data | T-025, T-050 | 1 |
| T-055 | Create FavoriteRepositoryImpl | Data | T-026, T-050 | 1 |
| T-056 | Create SettingsRepositoryImpl | Data | T-027, T-051 | 1 |

### Prompt Engineering

| ID | Task | Agent | Dependencies | Est. Days |
|----|------|-------|-------------|-----------|
| T-057 | Create system prompt v1 text | AI/Prompt | T-001 | 1 |
| T-058 | Create user prompt builder | AI/Prompt | T-016, T-019 | 1 |
| T-059 | Create strategy selector logic | AI/Prompt | T-016, T-019 | 1 |
| T-060 | Create format enforcer (JSON extraction) | AI/Prompt | T-044 | 0.5 |
| T-061 | Create AI response validator | AI/Prompt | T-044 | 1 |
| T-062 | Create retry strategy | AI/Prompt | T-001 | 0.5 |

### Presentation — Shared Components

| ID | Task | Agent | Dependencies | Est. Days |
|----|------|-------|-------------|-----------|
| T-063 | Create PrimaryButton, SecondaryButton, GhostButton | UI | T-011 | 1 |
| T-064 | Create AppTextField, AppDropdown | UI | T-011 | 1 |
| T-065 | Create TopBar, BottomNavigationBar, AppScaffold | UI | T-011, T-013 | 1 |
| T-066 | Create NameCard, ResultCard, ScoreBar, StrategyBadge | UI | T-011, T-018 | 1.5 |
| T-067 | Create LoadingAnimation, ProgressIndicator | UI | T-011 | 1 |
| T-068 | Create EmptyState, ErrorState, SnackbarHost | UI | T-011 | 1 |
| T-069 | Create GenderSelector, LevelSlider, LetterPicker | UI | T-011 | 1 |
| T-070 | Create ExpandableSection, SectionHeader, DetailRow | UI | T-011 | 1 |
| T-071 | Create Chip, OnboardingPage, InfoTooltip | UI | T-011 | 1 |

### Presentation — Screens

| ID | Task | Agent | Dependencies | Est. Days |
|----|------|-------|-------------|-----------|
| T-072 | Create SplashScreen + SplashViewModel + SplashState | Screen:Splash | T-015, T-035, T-063, T-065 | 1 |
| T-073 | Create OnboardingScreen + OnboardingViewModel + OnboardingState | Screen:Onboarding | T-015, T-036, T-071 | 1.5 |
| T-074 | Create HomeScreen + HomeViewModel + HomeState | Screen:Home | T-015, T-029, T-065, T-066 | 1.5 |
| T-075 | Create GenerateScreen + GenerateViewModel + GenerateState | Screen:Generate | T-015, T-028, T-064, T-069, T-070 | 2 |
| T-076 | Create LoadingScreen + LoadingViewModel + LoadingState | Screen:Loading | T-015, T-028, T-067 | 1.5 |
| T-077 | Create ResultScreen + ResultViewModel + ResultState | Screen:Result | T-015, T-028, T-033, T-037, T-066 | 2 |
| T-078 | Create HistoryScreen + HistoryViewModel + HistoryState | Screen:History | T-015, T-029, T-030, T-031, T-068 | 1.5 |
| T-079 | Create FavoriteScreen + FavoriteViewModel + FavoriteState | Screen:Favorite | T-015, T-032, T-033, T-068 | 1.5 |
| T-080 | Create SettingsScreen + SettingsViewModel + SettingsState | Screen:Settings | T-015, T-035, T-036, T-068 | 1.5 |

### Testing

| ID | Task | Agent | Dependencies | Est. Days |
|----|------|-------|-------------|-----------|
| T-081 | Write unit tests for GenerateBabyNameUseCase | Testing | T-028, T-053 | 1 |
| T-082 | Write unit tests for ToggleFavorite | Testing | T-033, T-055 | 0.5 |
| T-083 | Write unit tests for GetHistory | Testing | T-029, T-054 | 0.5 |
| T-084 | Write unit tests for AI response validator | Testing | T-061 | 1 |
| T-085 | Write unit tests for strategy selector | Testing | T-059 | 0.5 |
| T-086 | Write unit tests for GenerateViewModel | Testing | T-075 | 1 |
| T-087 | Write unit tests for ResultViewModel | Testing | T-077 | 1 |
| T-088 | Write UI tests for Generate screen | Testing | T-075 | 1 |
| T-089 | Write UI tests for Result screen | Testing | T-077 | 1 |

### Integration & Polish

| ID | Task | Agent | Dependencies | Est. Days |
|----|------|-------|-------------|-----------|
| T-090 | Integrate all screens into NavGraph | Architecture | T-072 — T-080 | 1 |
| T-091 | End-to-end flow test (Generate → Loading → Result) | Testing | T-090 | 1 |
| T-092 | Dark mode validation across all screens | Theme | T-090 | 1 |
| T-093 | Accessibility pass (content descriptions, contrast) | UI | T-090 | 1 |
| T-094 | Performance profiling and optimization | Architecture | T-090 | 1 |
| T-095 | Crash reporting setup (Firebase Crashlytics) | Architecture | T-001 | 0.5 |
| T-096 | Final build, signing, and APK generation | Architecture | T-090 | 0.5 |

---

### Task Summary

| Category | Count | Est. Days |
|----------|-------|-----------|
| Build Config & Foundation | 6 | 5 |
| Theme & Design System | 6 | 4 |
| Navigation | 3 | 2 |
| Domain Layer | 22 | 11 |
| Data — Network | 7 | 6 |
| Data — Local Storage | 5 | 4 |
| Data — Repository Impl | 4 | 5 |
| Prompt Engineering | 6 | 4 |
| UI Components | 9 | 9 |
| Presentation — Screens | 9 | 13 |
| Testing | 9 | 7 |
| Integration & Polish | 7 | 5 |
| **Total** | **96** | **75** |
