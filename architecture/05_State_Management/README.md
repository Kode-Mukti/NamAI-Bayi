# 05 — State Management

## Summary

Complete state management specification for every screen. Defines UI State, UI Events, One-shot Effects, and the ViewModel contract for each feature.

## Reasoning

Consistent state management is critical for Multi-Agent Development. Every screen agent implements the same pattern: `State` (data), `Event` (user action), `Effect` (one-shot side effect). No agent needs to guess how state works — the pattern is documented and enforced.

## Recommendation

### Universal Pattern

```
┌────────────────────────────────┐
│         Composable Screen      │
│                                │
│  collectAsState(uiState)       │
│  onEvent(Event)                │
│  LaunchedEffect(effect)        │
└──────────┬─────────────────────┘
           │
┌──────────┴─────────────────────┐
│         ViewModel              │
│                                │
│  StateFlow<UiState>            │
│  Channel<UiEffect> (one-shot)  │
│  fun onEvent(event: Event)     │
│                                │
│  Internally:                   │
│  - UseCase.invoke()            │
│  - Update _uiState             │
│  - Send _effect.send()         │
└────────────────────────────────┘
```

---

### Screen: Splash

**Purpose**: Show logo, check onboarding status, auto-navigate.

**State**:
```kotlin
data class SplashUiState(
    val isLoading: Boolean = true,
    val isOnboardingCompleted: Boolean = false  // After check
)
```

**Events**: None (auto-navigation)

**Effects**:
```kotlin
sealed class SplashEffect {
    object NavigateToOnboarding : SplashEffect()
    object NavigateToHome : SplashEffect()
}
```

**ViewModel Contract**:
```kotlin
@HiltViewModel
class SplashViewModel @Inject constructor(
    private val getSettings: GetSettings
) : ViewModel() {
    private val _uiState = MutableStateFlow(SplashUiState())
    val uiState: StateFlow<SplashUiState> = _uiState.asStateFlow()

    private val _effect = Channel<SplashEffect>(Channel.BUFFERED)
    val effect: Flow<SplashEffect> = _effect.receiveAsFlow()

    init { checkOnboardingStatus() }
    private fun checkOnboardingStatus() { ... }
}
```

---

### Screen: Onboarding

**Purpose**: 3 pages of introduction before first use.

**State**:
```kotlin
data class OnboardingUiState(
    val currentPage: Int = 0,
    val pageCount: Int = 3,
    val isLastPage: Boolean = false
)
```

**Events**:
```kotlin
sealed class OnboardingEvent {
    object OnNextPage : OnboardingEvent()
    object OnSkip : OnboardingEvent()
    object OnStart : OnboardingEvent()
}
```

**Effects**:
```kotlin
sealed class OnboardingEffect {
    object NavigateToHome : OnboardingEffect()
}
```

**ViewModel Contract**:
```kotlin
@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val updateSettings: UpdateSettings
) : ViewModel() {
    val uiState: StateFlow<OnboardingUiState>
    val effect: Flow<OnboardingEffect>
    fun onEvent(event: OnboardingEvent)
}
```

---

### Screen: Home

**Purpose**: Main landing screen with CTA and branding.

**State**:
```kotlin
data class HomeUiState(
    val recentHistoryItem: HistoryItem? = null,  // Last consultation
    val hasHistory: Boolean = false
)
```

**Events**:
```kotlin
sealed class HomeEvent {
    object OnStartConsultation : HomeEvent()
    object OnViewHistory : HomeEvent()
    object OnViewFavorite : HomeEvent()
    object OnViewSettings : HomeEvent()
}
```

**Effects**: None (navigation handled by navController directly)

**ViewModel Contract**:
```kotlin
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHistory: GetHistory
) : ViewModel() {
    val uiState: StateFlow<HomeUiState>
    fun onEvent(event: HomeEvent)
}
```

---

### Screen: Generate

**Purpose**: Input form for baby profile.

**State**:
```kotlin
data class GenerateUiState(
    val fatherName: String = "",
    val motherName: String = "",
    val gender: Gender = Gender.MALE,
    val religion: Religion = Religion.ISLAM,
    val culture: Culture = Culture.JAVA,
    val province: String = "",
    val desiredMeaning: String = "",
    val desiredPersonality: String = "",
    val firstLetter: String = "",        // Char as string
    val lastLetter: String = "",
    val maxLength: String = "15",        // Text field value
    val uniquenessLevel: Int = 3,
    val traditionalLevel: Int = 3,
    val showAdvancedOptions: Boolean = false,
    val isLoading: Boolean = false,
    val validationErrors: Map<String, String> = emptyMap()  // field -> error
)
```

**Events**:
```kotlin
sealed class GenerateEvent {
    data class OnFatherNameChanged(val value: String) : GenerateEvent()
    data class OnMotherNameChanged(val value: String) : GenerateEvent()
    data class OnGenderChanged(val value: Gender) : GenerateEvent()
    data class OnReligionChanged(val value: Religion) : GenerateEvent()
    data class OnCultureChanged(val value: Culture) : GenerateEvent()
    data class OnProvinceChanged(val value: String) : GenerateEvent()
    data class OnDesiredMeaningChanged(val value: String) : GenerateEvent()
    data class OnDesiredPersonalityChanged(val value: String) : GenerateEvent()
    data class OnFirstLetterChanged(val value: String) : GenerateEvent()
    data class OnLastLetterChanged(val value: String) : GenerateEvent()
    data class OnMaxLengthChanged(val value: String) : GenerateEvent()
    data class OnUniquenessLevelChanged(val value: Int) : GenerateEvent()
    data class OnTraditionalLevelChanged(val value: Int) : GenerateEvent()
    object OnToggleAdvancedOptions : GenerateEvent()
    object OnSubmit : GenerateEvent()
}
```

**Effects**:
```kotlin
sealed class GenerateEffect {
    data class NavigateToLoading(val resultId: String) : GenerateEffect()
    data class ShowError(val message: String) : GenerateEffect()
}
```

**ViewModel Contract**:
```kotlin
@HiltViewModel
class GenerateViewModel @Inject constructor(
    private val generateBabyName: GenerateBabyName
) : ViewModel() {
    val uiState: StateFlow<GenerateUiState>
    val effect: Flow<GenerateEffect>
    fun onEvent(event: GenerateEvent)
}
```

---

### Screen: Loading

**Purpose**: Animated loading while AI generates names.

**State**:
```kotlin
data class LoadingUiState(
    val statusMessage: String = "Menganalisis preferensi...",
    val progress: Float = 0f,             // 0..1
    val isError: Boolean = false,
    val errorMessage: String = "",
    val isTimeout: Boolean = false
)
```

**Events**:
```kotlin
sealed class LoadingEvent {
    object OnRetry : LoadingEvent()
    object OnCancel : LoadingEvent()
}
```

**Effects**:
```kotlin
sealed class LoadingEffect {
    data class NavigateToResult(val resultId: String) : LoadingEffect()
    object ShowTimeout : LoadingEffect()
}
```

**Status Message Cycle**:
```
0-20%   "Menganalisis preferensi..."
20-40%  "Memilih strategi penamaan..."
40-60%  "Merangkai kandidat nama..."
60-80%  "Mengevaluasi kualitas..."
80-100% "Memilih rekomendasi terbaik..."
```

**Timeout**: 15 seconds → show error with retry option.

---

### Screen: Result

**Purpose**: Display 5 name recommendations with details.

**State**:
```kotlin
data class ResultUiState(
    val isLoading: Boolean = true,
    val babyResult: BabyResult? = null,
    val selectedRecommendation: NameRecommendation? = null,  // Expanded detail
    val showDetailSheet: Boolean = false,
    val savedToWishlist: Set<String> = emptySet(),            // Saved name IDs
    val isSharing: Boolean = false,
    val error: String? = null
)
```

**Events**:
```kotlin
sealed class ResultEvent {
    object OnRetry : ResultEvent()
    data class OnNameTapped(val name: NameRecommendation) : ResultEvent()
    object OnDismissDetail : ResultEvent()
    data class OnToggleSave(val name: NameRecommendation) : ResultEvent()
    data class OnShareWhatsApp(val name: NameRecommendation) : ResultEvent()
    data class OnShareInstagram(val name: NameRecommendation) : ResultEvent()
    object OnNewConsultation : ResultEvent()
    object OnBackToHome : ResultEvent()
}
```

**Effects**:
```kotlin
sealed class ResultEffect {
    data class ShareToWhatsApp(val name: NameRecommendation) : ResultEffect()
    data class ShareToInstagram(val name: NameRecommendation) : ResultEffect()
    object NavigateToGenerate : ResultEffect()
    object NavigateToHome : ResultEffect()
    data class ShowSnackbar(val message: String) : ResultEffect()
}
```

---

### Screen: History

**Purpose**: List of past consultations.

**State**:
```kotlin
data class HistoryUiState(
    val items: List<HistoryItem> = emptyList(),
    val isLoading: Boolean = true,
    val isEmpty: Boolean = false
)
```

**Events**:
```kotlin
sealed class HistoryEvent {
    data class OnItemTapped(val historyItem: HistoryItem) : HistoryEvent()
    data class OnDeleteItem(val historyItem: HistoryItem) : HistoryEvent()
    object OnClearAll : HistoryEvent()
    object OnRefresh : HistoryEvent()
}
```

**Effects**:
```kotlin
sealed class HistoryEffect {
    data class NavigateToResult(val resultId: String) : HistoryEffect()
    data class ShowSnackbar(val message: String) : HistoryEffect()
    data class ShowDeleteConfirmation(val item: HistoryItem) : HistoryEffect()
}
```

---

### Screen: Favorite

**Purpose**: List of saved favorite names.

**State**:
```kotlin
data class FavoriteUiState(
    val items: List<FavoriteName> = emptyList(),
    val isLoading: Boolean = true,
    val isEmpty: Boolean = false
)
```

**Events**:
```kotlin
sealed class FavoriteEvent {
    data class OnRemoveFavorite(val favorite: FavoriteName) : FavoriteEvent()
    data class OnItemTapped(val favorite: FavoriteName) : FavoriteEvent()
    object OnRefresh : FavoriteEvent()
}
```

**Effects**:
```kotlin
sealed class FavoriteEffect {
    data class NavigateToResult(val resultId: String) : FavoriteEffect()
    data class ShowSnackbar(val message: String) : FavoriteEffect()
}
```

---

### Screen: Settings

**Purpose**: App preferences.

**State**:
```kotlin
data class SettingsUiState(
    val settings: Settings = Settings(),
    val appVersion: String = "1.0.0"
)
```

**Events**:
```kotlin
sealed class SettingsEvent {
    data class OnDarkModeChanged(val enabled: Boolean) : SettingsEvent()
    data class OnModelChanged(val model: String) : SettingsEvent()
    data class OnTemperatureChanged(val value: Float) : SettingsEvent()
    object OnClearHistory : SettingsEvent()
    object OnAbout : SettingsEvent()
    object OnRateApp : SettingsEvent()
    object OnShareApp : SettingsEvent()
    object OnPrivacyPolicy : SettingsEvent()
}
```

**Effects**:
```kotlin
sealed class SettingsEffect {
    object RestartApp : SettingsEffect()
    data class ShowSnackbar(val message: String) : SettingsEffect()
    data class OpenUrl(val url: String) : SettingsEffect()
}
```

---

### State Consistency Rules

| Rule | Description |
|------|-------------|
| **Immutable State** | UiState is a data class, replaced entirely via copy() |
| **Single StateFlow** | One StateFlow per screen, not multiple |
| **Events for User Actions** | ViewModel exposes `fun onEvent(event: Event)`, not public state setters |
| **Effects for One-Shot** | Navigation, snackbar, share intents go through Effect channel |
| **No State in Screen** | Screen is a stateless Composable — all state in ViewModel |
| **Loading in State** | Every screen that loads data has `isLoading` in its state |
| **Error in State** | Every screen that can fail has `error: String?` in state |
| **Default Values** | UiState constructors have sensible defaults for preview |

### ViewModel Lifecycle Rules

| Rule | Description |
|------|-------------|
| `viewModelScope.launch` | All async work uses viewModelScope |
| `Dispatchers.IO` | Repository calls on IO dispatcher |
| `onCleared()` | Cancel any ongoing AI requests |
| SavedStateHandle | Only for navigation arguments (resultId) |
| No `remember` in ViewModel | ViewModel outlives recomposition |
