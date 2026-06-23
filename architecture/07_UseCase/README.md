# 07 — UseCase

## Summary

Complete list of use cases with their input, output, dependency, and behavior specification. Use cases are the atomic operations that ViewModels call.

## Reasoning

Use cases encapsulate business logic. They are the single responsibility unit in the domain layer. Each use case is independently testable by the Testing Agent. ViewModels never contain business logic — they delegate to use cases.

## Recommendation

---

### GenerateBabyName

**Purpose**: Orchestrate full AI name generation. Validate input, select strategy, build prompt, call AI, parse result, save history.

```kotlin
class GenerateBabyName @Inject constructor(
    private val generateRepository: GenerateRepository,
    private val historyRepository: HistoryRepository,
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(profile: BabyProfile): Result<BabyResult>
}
```

**Flow**:
```
1. Validate BabyProfile (basic field validation)
2. Get settings (model, temperature)
3. Call generateRepository.generate(profile)
4. If success → create HistoryItem, save via historyRepository
5. Return BabyResult
```

**Error Cases**:
- Validation error → return `Result.Error(VALIDATION_ERROR)`
- Network timeout → return `Result.Error(TIMEOUT)`
- AI invalid response → return `Result.Error(AI_INVALID_RESPONSE)`

---

### GetHistory

**Purpose**: Observe history list reactively.

```kotlin
class GetHistory @Inject constructor(
    private val historyRepository: HistoryRepository
) {
    fun invoke(): Flow<List<HistoryItem>> = historyRepository.getHistory()
}
```

---

### DeleteHistory

**Purpose**: Delete a single history item.

```kotlin
class DeleteHistory @Inject constructor(
    private val historyRepository: HistoryRepository
) {
    suspend operator fun invoke(id: String): Result<Unit> = historyRepository.deleteHistory(id)
}
```

---

### ClearHistory

**Purpose**: Delete all history.

```kotlin
class ClearHistory @Inject constructor(
    private val historyRepository: HistoryRepository
) {
    suspend operator fun invoke(): Result<Unit> = historyRepository.clearHistory()
}
```

---

### GetFavorites

**Purpose**: Observe favorites list reactively.

```kotlin
class GetFavorites @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    fun invoke(): Flow<List<FavoriteName>> = favoriteRepository.getFavorites()
}
```

---

### ToggleFavorite

**Purpose**: Add or remove a favorite name. Returns the new state.

```kotlin
class ToggleFavorite @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    suspend operator fun invoke(favorite: FavoriteName): Result<Boolean> =
        favoriteRepository.toggleFavorite(favorite)
}
```

---

### IsFavorite

**Purpose**: Check if a specific name is already favorited.

```kotlin
class IsFavorite @Inject constructor(
    private val favoriteRepository: FavoriteRepository
) {
    suspend operator fun invoke(nameId: String): Boolean =
        favoriteRepository.isFavorite(nameId)
}
```

---

### GetSettings

**Purpose**: Observe settings reactively.

```kotlin
class GetSettings @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    fun invoke(): Flow<Settings> = settingsRepository.getSettings()
}
```

---

### UpdateSettings

**Purpose**: Update specific settings (dark mode, model, temperature).

```kotlin
class UpdateSettings @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    suspend operator fun invoke(
        darkMode: Boolean? = null,
        model: String? = null,
        temperature: Float? = null
    ): Result<Unit> {
        // Apply only non-null values
        darkMode?.let { settingsRepository.setDarkMode(it) }
        model?.let { settingsRepository.setModel(it) }
        temperature?.let { settingsRepository.setTemperature(it) }
        return Result.Success(Unit)
    }
}
```

---

### ExportResult

**Purpose**: Generate shareable text/image from a BabyResult or NameRecommendation.

```kotlin
class ExportResult @Inject constructor() {

    fun toShareText(name: NameRecommendation, strategy: NamingStrategy): String {
        return """
            ✨ Rekomendasi Nama dari NamAI Bayi ✨
            
            Nama: ${name.name}
            Arti: ${name.meaning}
            Filosofi: ${name.philosophy}
            Asal: ${name.origin}
            Skor Keunikan: ${name.uniquenessScore}/100
            Strategi: ${strategy.name}
            
            Dibuat dengan NamAI Bayi — Konsultan Nama Bayi AI
        """.trimIndent()
    }

    fun toInstagramText(name: NameRecommendation): String {
        return """
            Nama ${name.name} ✨
            ${name.meaning}
        """.trimIndent()
    }
}
```

---

### Use Case Dependency Graph

```
                    ┌──────────────────────┐
                    │   GenerateBabyName   │
                    │                      │
                    │  GenerateRepository  │
                    │  HistoryRepository   │
                    │  SettingsRepository  │
                    └──────────┬───────────┘
                               │
            ┌──────────────────┼──────────────────┐
            ▼                  ▼                    ▼
    ┌──────────────┐  ┌──────────────┐  ┌──────────────┐
    │  GetHistory  │  │ DeleteHistory│  │ ClearHistory │
    │              │  │              │  │              │
    │HistoryRepo   │  │HistoryRepo   │  │HistoryRepo   │
    └──────────────┘  └──────────────┘  └──────────────┘

    ┌──────────────┐  ┌──────────────┐  ┌──────────────┐
    │ GetFavorites │  │ToggleFavorite│  │  IsFavorite  │
    │              │  │              │  │              │
    │FavoriteRepo  │  │FavoriteRepo  │  │FavoriteRepo  │
    └──────────────┘  └──────────────┘  └──────────────┘

    ┌──────────────┐  ┌──────────────┐  ┌──────────────┐
    │  GetSettings │  │UpdateSettings│  │ ExportResult │
    │              │  │              │  │              │
    │SettingsRepo  │  │SettingsRepo  │  │  (pure fn)   │
    └──────────────┘  └──────────────┘  └──────────────┘
```

---

### ViewModel → UseCase Mapping

| ViewModel | UseCase(s) |
|-----------|------------|
| SplashViewModel | GetSettings (to check onboarding) |
| OnboardingViewModel | UpdateSettings (to mark done) |
| HomeViewModel | GetHistory (for recent) |
| GenerateViewModel | GenerateBabyName |
| LoadingViewModel | GenerateBabyName (poll/await) |
| ResultViewModel | ToggleFavorite, ExportResult |
| HistoryViewModel | GetHistory, DeleteHistory, ClearHistory |
| FavoriteViewModel | GetFavorites, ToggleFavorite |
| SettingsViewModel | GetSettings, UpdateSettings |
