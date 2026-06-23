# 06 — Repository

## Summary

Repository contract interfaces defined in the domain layer. Each interface is the boundary between data and domain. Implementations live in the data layer.

## Reasoning

Repository interfaces are the critical handoff point between the Domain Agent and the Data Agent. The Domain Agent defines what methods are needed. The Data Agent implements them against real data sources. No agent crosses this boundary.

## Recommendation

---

### GenerateRepository

**File**: `domain/repository/GenerateRepository.kt`

**Purpose**: Generate baby names via AI provider. This is the core value proposition.

```kotlin
interface GenerateRepository {

    /**
     * Generate baby name recommendations based on profile.
     * Calls AI provider (OpenRouter), parses response, returns domain model.
     *
     * @param profile User's baby profile with preferences
     * @return Result wrapping BabyResult or domain error
     */
    suspend fun generate(profile: BabyProfile): Result<BabyResult>

    /**
     * Parse the raw AI response and validate against schema.
     *
     * @param rawJson The raw JSON string from AI provider
     * @return Parsed BabyResult or parse error
     */
    fun parseResponse(rawJson: String): Result<BabyResult>

    /**
     * Check if the AI provider is healthy/responsive.
     */
    suspend fun healthCheck(): Boolean
}
```

---

### HistoryRepository

**File**: `domain/repository/HistoryRepository.kt`

**Purpose**: CRUD operations for consultation history.

```kotlin
interface HistoryRepository {

    /**
     * Get all history items, newest first.
     */
    fun getHistory(): Flow<List<HistoryItem>>

    /**
     * Get a single history item by ID.
     */
    suspend fun getHistoryById(id: String): Result<HistoryItem>

    /**
     * Save a new history item after consultation.
     */
    suspend fun saveHistory(item: HistoryItem): Result<Unit>

    /**
     * Delete a single history item.
     */
    suspend fun deleteHistory(id: String): Result<Unit>

    /**
     * Delete all history items.
     */
    suspend fun clearHistory(): Result<Unit>

    /**
     * Get the count of history items.
     */
    suspend fun getHistoryCount(): Int
}
```

---

### FavoriteRepository

**File**: `domain/repository/FavoriteRepository.kt`

**Purpose**: Manage saved favorite names.

```kotlin
interface FavoriteRepository {

    /**
     * Get all favorite names, newest first.
     */
    fun getFavorites(): Flow<List<FavoriteName>>

    /**
     * Add a name to favorites.
     */
    suspend fun addFavorite(favorite: FavoriteName): Result<Unit>

    /**
     * Remove a name from favorites.
     */
    suspend fun removeFavorite(id: String): Result<Unit>

    /**
     * Check if a specific name is favorited.
     */
    suspend fun isFavorite(nameId: String): Boolean

    /**
     * Toggle favorite status. Returns new state.
     */
    suspend fun toggleFavorite(favorite: FavoriteName): Result<Boolean>
}
```

---

### SettingsRepository

**File**: `domain/repository/SettingsRepository.kt`

**Purpose**: Persist user preferences and app settings.

```kotlin
interface SettingsRepository {

    /**
     * Observe settings changes reactively.
     */
    fun getSettings(): Flow<Settings>

    /**
     * Get current settings snapshot.
     */
    suspend fun getSettingsSnapshot(): Settings

    /**
     * Update dark mode preference.
     */
    suspend fun setDarkMode(enabled: Boolean): Result<Unit>

    /**
     * Update AI model selection.
     */
    suspend fun setModel(model: String): Result<Unit>

    /**
     * Update AI temperature.
     */
    suspend fun setTemperature(temperature: Float): Result<Unit>

    /**
     * Mark onboarding as completed.
     */
    suspend fun setOnboardingCompleted(): Result<Unit>

    /**
     * Check if onboarding was completed.
     */
    suspend fun isOnboardingCompleted(): Boolean

    /**
     * Reset all settings to defaults.
     */
    suspend fun resetSettings(): Result<Unit>
}
```

---

### Repository Implementation Notes

| Repository | Data Source | Technology |
|------------|-------------|------------|
| `GenerateRepositoryImpl` | OpenRouter API + local parse | Retrofit + Gson/Kotlinx Serialization |
| `HistoryRepositoryImpl` | Room Database | Room DAO + Entity |
| `FavoriteRepositoryImpl` | Room Database | Room DAO + Entity |
| `SettingsRepositoryImpl` | DataStore Preferences | Jetpack DataStore |

### DI Binding Contract

```kotlin
// core/di/DataModule.kt
@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds abstract fun bindGenerateRepository(impl: GenerateRepositoryImpl): GenerateRepository
    @Binds abstract fun bindHistoryRepository(impl: HistoryRepositoryImpl): HistoryRepository
    @Binds abstract fun bindFavoriteRepository(impl: FavoriteRepositoryImpl): FavoriteRepository
    @Binds abstract fun bindSettingsRepository(impl: SettingsRepositoryImpl): SettingsRepository
}
```

### Error Handling Contract

```kotlin
// core/common/Result.kt
sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val error: AppError) : Result<Nothing>()
}

data class AppError(
    val code: ErrorCode,
    val message: String,
    val cause: Throwable? = null
)

enum class ErrorCode {
    // Network
    NETWORK_ERROR,
    TIMEOUT,
    RATE_LIMITED,
    UNAUTHORIZED,
    // AI
    AI_INVALID_RESPONSE,
    AI_CONTENT_FILTERED,
    AI_PROVIDER_ERROR,
    // Database
    DB_READ_ERROR,
    DB_WRITE_ERROR,
    // Validation
    VALIDATION_ERROR,
    // Unknown
    UNKNOWN
}
```

### Repository Testing Contract

| Repository | Unit Test Goals | Mock Target |
|------------|-----------------|-------------|
| GenerateRepositoryImpl | Correct prompt building, response parsing, error handling | OpenRouterApi |
| HistoryRepositoryImpl | Save/read/delete operations, Flow emission | HistoryDao |
| FavoriteRepositoryImpl | Add/remove/isFavorite, Flow updates | FavoriteDao |
| SettingsRepositoryImpl | Read/write, Flow observation, defaults | DataStore |
