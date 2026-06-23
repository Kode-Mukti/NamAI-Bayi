# 19 — Android Architecture

## Summary

Android application built with Kotlin, Jetpack Compose, and Clean Architecture. Single-activity architecture with modular structure for scalability and testability.

## Reasoning

Android architecture must support: (1) offline-first with local wishlist, (2) clean separation between UI and AI logic, (3) easy provider switching, (4) testability. MVVM with Clean Architecture is the established best practice for production Android apps.

## Recommendation

### Architecture Layers

```
┌─────────────────────────────────────────┐
│            UI Layer (Compose)            │
│  ┌────────┐ ┌────────┐ ┌─────────────┐  │
│  │Screens │ │Compose │ │  Navigation  │  │
│  │(Screen)│ │Widgets │ │  (NavHost)   │  │
│  └───┬────┘ └───┬────┘ └──────┬──────┘  │
│      │          │              │         │
│  ┌───┴──────────┴──────────────┴──────┐  │
│  │        ViewModels                  │  │
│  │  (StateFlow + ViewModelScope)      │  │
│  └───────────────┬────────────────────┘  │
└──────────────────┼───────────────────────┘
                   │
┌──────────────────┼───────────────────────┐
│       Domain Layer (Use Cases)           │
│  ┌───────────────┴────────────────────┐  │
│  │  GetNameRecommendationUseCase      │  │
│  │  SaveToWishlistUseCase             │  │
│  │  RemoveFromWishlistUseCase         │  │
│  │  GetWishlistUseCase                │  │
│  └───────────────┬────────────────────┘  │
└──────────────────┼───────────────────────┘
                   │
┌──────────────────┼───────────────────────┐
│          Data Layer                      │
│  ┌───────────────┴────────────────────┐  │
│  │  Repository Implementations        │  │
│  │  ┌──────────────┐ ┌─────────────┐  │  │
│  │  │AIProviderRepo│ │WishlistRepo │  │  │
│  │  └──────┬───────┘ └──────┬──────┘  │  │
│  └─────────┼────────────────┼──────────┘  │
└────────────┼────────────────┼──────────────┘
             │                │
    ┌────────┴────────┐  ┌───┴────────┐
    │  AI Provider    │  │   Room DB  │
    │  ┌──────────┐   │  │ (Wishlist) │
    │  │ Gemini   │   │  └────────────┘
    │  │ OpenAI   │   │
    │  │ (future) │   │
    │  └──────────┘   │
    └─────────────────┘
```

### Package Structure

```
com.kodemukti.namaibayi/
├── app/
│   ├── MainActivity.kt              # Single activity
│   ├── NamAIApplication.kt          # Application class
│   └── di/
│       ├── AppModule.kt             # Hilt DI module
│       ├── AIModule.kt              # AI provider DI
│       └── DatabaseModule.kt        # Room DI
│
├── ui/
│   ├── navigation/
│   │   ├── NavGraph.kt              # Navigation routes
│   │   ├── Screen.kt                # Screen sealed class
│   │   └── NavArgs.kt               # Navigation arguments
│   │
│   ├── theme/
│   │   ├── Theme.kt                 # Material3 theme
│   │   ├── Color.kt                 # Color definitions
│   │   ├── Type.kt                  # Typography
│   │   └── Shape.kt                 # Shapes
│   │
│   ├── home/
│   │   ├── HomeScreen.kt            # Home composable
│   │   └── HomeViewModel.kt         # Home state holder
│   │
│   ├── input/
│   │   ├── InputScreen.kt           # Input form composable
│   │   ├── InputViewModel.kt        # Input state holder
│   │   └── components/
│   │       ├── InputField.kt        # Reusable input field
│   │       ├── DropdownField.kt     # Custom dropdown
│   │       └── ProgressStepper.kt   # Step indicator
│   │
│   ├── result/
│   │   ├── ResultScreen.kt          # Result list composable
│   │   ├── ResultViewModel.kt       # Result state holder
│   │   ├── components/
│   │   │   ├── NameCard.kt          # Name card component
│   │   │   ├── NameDetailSheet.kt   # Bottom sheet detail
│   │   │   ├── StrategyBadge.kt     # Strategy chip
│   │   │   └── LoadingAnimation.kt  # Loading state
│   │   └── share/
│   │       ├── ShareToWhatsApp.kt   # WA share intent
│   │       └── ShareToInstagram.kt  # IG story share
│   │
│   └── wishlist/
│       ├── WishlistScreen.kt        # Wishlist composable
│       └── WishlistViewModel.kt     # Wishlist state holder
│
├── domain/
│   ├── model/
│   │   ├── UserPreferences.kt       # Input domain model
│   │   ├── AIResponse.kt            # AI result domain model
│   │   ├── NameRecommendation.kt    # Single recommendation
│   │   └── WishlistItem.kt          # Saved name model
│   │
│   ├── repository/
│   │   ├── AIRepository.kt          # AI provider interface
│   │   └── WishlistRepository.kt    # Wishlist interface
│   │
│   └── usecase/
│       ├── GetRecommendationUseCase.kt
│       ├── SaveToWishlistUseCase.kt
│       ├── RemoveFromWishlistUseCase.kt
│       └── GetWishlistUseCase.kt
│
└── data/
    ├── remote/
    │   ├── ai/
    │   │   ├── AIProvider.kt         # Provider interface
    │   │   ├── GeminiProvider.kt     # Gemini implementation
    │   │   ├── OpenAIProvider.kt     # OpenAI implementation
    │   │   └── ProviderConfig.kt     # Config/selection logic
    │   └── api/
    │       └── GeminiApi.kt          # Retrofit interface
    │
    ├── local/
    │   ├── db/
    │   │   ├── NamAIDatabase.kt      # Room database
    │   │   ├── WishlistDao.kt        # DAO interface
    │   │   └── entity/
    │   │       └── WishlistEntity.kt # Room entity
    │   └── datastore/
    │       └── PreferencesDataStore.kt  # Settings storage
    │
    └── repository/
        ├── AIRepositoryImpl.kt       # AI repo implementation
        └── WishlistRepositoryImpl.kt  # Wishlist impl
```

### Key Dependencies

```kotlin
// build.gradle.kts (app)
dependencies {
    // Compose BOM
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")

    // Hilt DI
    implementation("com.google.dagger:hilt-android:2.50")
    kapt("com.google.dagger:hilt-compiler:2.50")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")

    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    // Network
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // AI SDKs
    implementation("com.google.ai.client.generativeai:generativeai:0.4.0")  // Gemini

    // Testing
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")
    testImplementation("io.mockk:mockk:1.13.9")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
}
```

### Navigation Graph

```kotlin
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Input : Screen("input")
    object Result : Screen("result/{sessionId}") {
        fun createRoute(sessionId: String) = "result/$sessionId"
    }
    object Wishlist : Screen("wishlist")
}

// NavGraph.kt
@Composable
fun NamAINavGraph(navController: NavHostController) {
    NavHost(navController, startDestination = Screen.Home.route) {
        composable(Screen.Home.route) {
            HomeScreen(onStartConsultation = {
                navController.navigate(Screen.Input.route)
            })
        }
        composable(Screen.Input.route) {
            InputScreen(onResult = { sessionId ->
                navController.navigate(Screen.Result.createRoute(sessionId))
            })
        }
        composable(
            route = Screen.Result.route,
            arguments = listOf(navArgument("sessionId") { type = NavType.StringType })
        ) {
            ResultScreen()
        }
        composable(Screen.Wishlist.route) {
            WishlistScreen()
        }
    }
}
```

### State Management

```kotlin
// ResultViewModel.kt
@HiltViewModel
class ResultViewModel @Inject constructor(
    private val getRecommendationUseCase: GetRecommendationUseCase,
    private val saveToWishlistUseCase: SaveToWishlistUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val sessionId: String = savedStateHandle["sessionId"]!!

    private val _uiState = MutableStateFlow<ResultUiState>(ResultUiState.Loading)
    val uiState: StateFlow<ResultUiState> = _uiState.asStateFlow()

    init {
        loadRecommendation()
    }

    private fun loadRecommendation() {
        viewModelScope.launch {
            _uiState.value = ResultUiState.Loading
            getRecommendationUseCase(sessionId)
                .onSuccess { result ->
                    _uiState.value = ResultUiState.Success(result)
                }
                .onFailure { error ->
                    _uiState.value = ResultUiState.Error(error.message ?: "Terjadi kesalahan")
                }
        }
    }

    fun saveName(name: NameRecommendation) {
        viewModelScope.launch {
            saveToWishlistUseCase(name)
        }
    }
}

sealed class ResultUiState {
    object Loading : ResultUiState()
    data class Success(val response: AIResponse) : ResultUiState()
    data class Error(val message: String) : ResultUiState()
}
```

### AI Provider Configuration

```kotlin
// ProviderConfig.kt
enum class AIProviderType {
    GEMINI, OPENAI, OPENROUTER
}

@Module
@InstallIn(SingletonComponent::class)
object AIModule {

    @Provides
    @Singleton
    fun provideAIProvider(
        @ApplicationContext context: Context,
    ): AIProvider {
        val config = PreferencesDataStore(context)
        return when (config.getAIProvider()) {
            AIProviderType.GEMINI -> GeminiProvider(context)
            AIProviderType.OPENAI -> OpenAIProvider(context)
            AIProviderType.OPENROUTER -> OpenRouterProvider(context)
        }
    }
}
```

### Testing Strategy

| Layer | Test Type | Framework |
|-------|-----------|-----------|
| ViewModel | Unit | JUnit + MockK + Turbine |
| UseCase | Unit | JUnit + MockK |
| Repository | Unit + Integration | JUnit + MockK + Room |
| Repository (AI) | Integration | With mock server |
| Compose UI | Snapshot | Paparazzi |
| Compose UI | Screenshot | Roborazzi |
| Navigation | Unit | JUnit |

## Implementation

- Start with domain layer (models, interfaces)
- Implement data layer (Room + AI provider)
- Build ViewModel layer
- Create Compose UI screens
- Add DI with Hilt
- Write tests concurrently

## Future Improvement

- Kotlin Multiplatform Mobile (KMM) for iOS
- Compose Multiplatform for shared UI
- Dynamic feature modules for Premium
- Baseline Profiles for startup optimization
- Kotlin serialization for JSON parsing
