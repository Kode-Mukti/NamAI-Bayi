# 12 — Agent Breakdown

## Summary

Complete specification of every sub-agent, their responsibilities, deliverables, dependencies, and handoff protocol. This is the organizational blueprint for Multi-Agent Development.

## Reasoning

Without explicit agent boundaries, multiple agents create conflicting code. Each agent must know: exactly what to build, what files to touch, what interfaces to consume, and whom to hand off to.

## Recommendation

---

### Agent Inventory

| # | Agent | Lead Time | Files | Dependencies |
|---|-------|-----------|-------|-------------|
| A-01 | Architecture Agent | Sprint 0-1 | `core/common/`, `core/di/`, `NamAIApplication`, `MainActivity`, build files | None |
| A-02 | Theme Agent | Sprint 1 | `core/ui/theme/`, font resources, `strings.xml`, `colors.xml` | A-01 |
| A-03 | UI Component Agent | Sprint 1-2 | `core/ui/component/` | A-02 |
| A-04 | Navigation Agent | Sprint 1 | `core/ui/navigation/` | A-01 |
| A-05 | Domain Model Agent | Sprint 1 | `domain/model/` | A-01 |
| A-06 | Repository Interface Agent | Sprint 1 | `domain/repository/` | A-05 |
| A-07 | Use Case Agent | Sprint 2 | `domain/usecase/` | A-05, A-06 |
| A-08 | Data Network Agent | Sprint 2-3 | `core/network/`, `data/remote/dto/` | A-01 |
| A-09 | Data Local Agent | Sprint 2-3 | `core/database/`, `data/local/entity/`, `data/local/dao/`, `data/local/datastore/` | A-01, A-05 |
| A-10 | Repository Impl Agent | Sprint 3 | `data/repository/` | A-06, A-08, A-09 |
| A-11 | AI/Prompt Agent | Sprint 2-3 | `data/remote/ai/`, `prompts/`, validation | A-05, A-08 |
| A-12 | Screen: Splash Agent | Sprint 3 | `presentation/splash/` | A-04, A-03 |
| A-13 | Screen: Onboarding Agent | Sprint 3 | `presentation/onboarding/` | A-04, A-03 |
| A-14 | Screen: Home Agent | Sprint 3 | `presentation/home/` | A-04, A-03, A-07 |
| A-15 | Screen: Generate Agent | Sprint 3-4 | `presentation/generate/` | A-04, A-03, A-07 |
| A-16 | Screen: Loading Agent | Sprint 4 | `presentation/loading/` | A-04, A-03, A-07 |
| A-17 | Screen: Result Agent | Sprint 4 | `presentation/result/` | A-04, A-03, A-07 |
| A-18 | Screen: History Agent | Sprint 4 | `presentation/history/` | A-04, A-03, A-07 |
| A-19 | Screen: Favorite Agent | Sprint 4 | `presentation/favorite/` | A-04, A-03, A-07 |
| A-20 | Screen: Settings Agent | Sprint 4 | `presentation/settings/` | A-04, A-03, A-07 |
| A-21 | Testing Agent | Sprint 4+ | `test/` | All above |

---

### Agent Detailed Specifications

#### A-01: Architecture Agent

**Responsibility**: Project skeleton, build system, dependency injection, shared utilities.

**Creates**:
- Root `build.gradle.kts`, `settings.gradle.kts`, `gradle/libs.versions.toml`
- `app/build.gradle.kts` with all dependencies
- `app/src/main/java/com/kodemukti/namaibayi/NamAIApplication.kt`
- `app/src/main/java/com/kodemukti/namaibayi/MainActivity.kt`
- `core/common/Constants.kt`
- `core/common/Extensions.kt`
- `core/common/Result.kt` (sealed class)
- `core/common/UiState.kt` (sealed class)
- `core/di/AppModule.kt`
- `core/di/DataModule.kt`
- `core/di/DomainModule.kt`
- `core/di/AIModule.kt`

**Handoff**: All agents depend on A-01 being complete first.

**Acceptance**: Project compiles, Hilt builds, empty app launches.

---

#### A-02: Theme Agent

**Responsibility**: Visual design tokens.

**Creates**:
- `core/ui/theme/Color.kt`
- `core/ui/theme/Type.kt`
- `core/ui/theme/Shape.kt`
- `core/ui/theme/Elevation.kt`
- `core/ui/theme/Theme.kt`
- `res/font/plus_jakarta_sans_*`
- `res/values/strings.xml` (all static strings)
- `res/values/themes.xml`
- `res/values-night/themes.xml`

**Consumes**: A-01

**Handoff**: A-03 (UI Component Agent)

---

#### A-03: UI Component Agent

**Responsibility**: All reusable composable components.

**Creates** (26 components):
- Buttons: Primary, Secondary, Ghost, Icon, Text
- Cards: NameCard, ResultCard, HistoryCard, FavoriteCard, StatCard
- Input: AppTextField, AppDropdown, GenderSelector, LetterPicker, LevelSlider
- Feedback: LoadingAnimation, ProgressIndicator, ShimmerLoader, EmptyState, ErrorState, SnackbarHost
- Display: ScoreBar, StrategyBadge, Chip, SectionHeader, DetailRow, InfoTooltip
- Layout: TopBar, BottomNavigationBar, OnboardingPage, AppScaffold, ExpandableSection

**Consumes**: A-02

**Handoff**: A-12 through A-20 (all screen agents)

---

#### A-04: Navigation Agent

**Responsibility**: Route definitions and navigation graph.

**Creates**:
- `core/ui/navigation/Screen.kt`
- `core/ui/navigation/NavArgs.kt`
- `core/ui/navigation/NavGraph.kt`

**Consumes**: A-01

**Handoff**: A-12 through A-20 (screen agents register their composables)

---

#### A-05: Domain Model Agent

**Responsibility**: All domain data classes.

**Creates**:
- `domain/model/BabyProfile.kt` (with Gender, Religion, Culture enums)
- `domain/model/BabyResult.kt`
- `domain/model/NameRecommendation.kt` (with UniquenessLabel, PopularityLevel, Readability)
- `domain/model/NamingStrategy.kt`
- `domain/model/FavoriteName.kt`
- `domain/model/HistoryItem.kt`
- `domain/model/Settings.kt` (with AIProviderType, AppLanguage)
- `domain/model/AIPromptRequest.kt`
- `domain/model/AIResponse.kt` (with TokenCount)
- `domain/model/AppError.kt`

**Consumes**: A-01

**Handoff**: A-06, A-07, A-10, A-11

---

#### A-06: Repository Interface Agent

**Responsibility**: Repository contracts in domain layer.

**Creates**:
- `domain/repository/GenerateRepository.kt`
- `domain/repository/HistoryRepository.kt`
- `domain/repository/FavoriteRepository.kt`
- `domain/repository/SettingsRepository.kt`

**Consumes**: A-05

**Handoff**: A-10 (Repository Impl), A-07 (Use Cases)

---

#### A-07: Use Case Agent

**Responsibility**: Business logic unit operations.

**Creates**:
- `domain/usecase/GenerateBabyName.kt`
- `domain/usecase/GetHistory.kt`
- `domain/usecase/DeleteHistory.kt`
- `domain/usecase/ClearHistory.kt`
- `domain/usecase/GetFavorites.kt`
- `domain/usecase/ToggleFavorite.kt`
- `domain/usecase/IsFavorite.kt`
- `domain/usecase/GetSettings.kt`
- `domain/usecase/UpdateSettings.kt`
- `domain/usecase/ExportResult.kt`

**Consumes**: A-05, A-06

**Handoff**: A-14 through A-20 (screen ViewModels)

---

#### A-08: Data Network Agent

**Responsibility**: Network configuration, Retrofit setup, DTOs.

**Creates**:
- `core/network/NetworkConfig.kt`
- `core/network/ApiInterceptor.kt`
- `core/network/OpenRouterApi.kt`
- `data/remote/dto/OpenRouterRequest.kt`
- `data/remote/dto/OpenRouterResponse.kt`
- `data/remote/dto/AIErrorResponse.kt`

**Consumes**: A-01

**Handoff**: A-10, A-11

---

#### A-09: Data Local Agent

**Responsibility**: Room database, DAOs, entities, DataStore.

**Creates**:
- `core/database/NamAIDatabase.kt`
- `core/database/Converters.kt`
- `data/local/entity/BabyResultEntity.kt`
- `data/local/entity/FavoriteEntity.kt`
- `data/local/entity/HistoryEntity.kt`
- `data/local/dao/BabyResultDao.kt`
- `data/local/dao/FavoriteDao.kt`
- `data/local/dao/HistoryDao.kt`
- `data/local/datastore/SettingsDataStore.kt`
- `data/local/datastore/OnboardingDataStore.kt`
- Entity ↔ Domain model mapper functions

**Consumes**: A-01, A-05

**Handoff**: A-10

---

#### A-10: Repository Impl Agent

**Responsibility**: Implement repository interfaces against real data sources.

**Creates**:
- `data/repository/GenerateRepositoryImpl.kt`
- `data/repository/HistoryRepositoryImpl.kt`
- `data/repository/FavoriteRepositoryImpl.kt`
- `data/repository/SettingsRepositoryImpl.kt`

**Consumes**: A-06 (interfaces), A-08 (network), A-09 (local)

**Handoff**: A-14 through A-20 (via DI)

---

#### A-11: AI/Prompt Agent

**Responsibility**: AI provider logic, prompt engineering, strategy selection, response validation.

**Creates**:
- `data/remote/ai/AIProvider.kt` interface
- `data/remote/ai/OpenRouterProvider.kt`
- `data/remote/ai/MockAIProvider.kt`
- `data/remote/ai/StrategySelector.kt`
- `data/remote/ai/UserPromptBuilder.kt`
- `data/remote/ai/FormatEnforcer.kt`
- `data/remote/ai/RetryStrategy.kt`
- `data/remote/ai/AIResponseValidator.kt`
- `system_prompt_v1.txt`
- `user_prompt_template_v1.txt`

**Consumes**: A-05 (models), A-08 (DTOs, API)

**Handoff**: A-10 (GenerateRepositoryImpl consumes AIProvider)

---

#### A-12 through A-20: Screen Agents

Each screen agent follows an identical pattern:

**Responsibility**: One screen (Composable + ViewModel + State)

**Creates**:
- `presentation/{screen}/{ScreenName}Screen.kt`
- `presentation/{screen}/{ScreenName}ViewModel.kt`
- `presentation/{screen}/{ScreenName}State.kt`

**Consumes**:
- A-04 (Navigation — route registration in NavGraph)
- A-03 (UI components)
- A-07 (Use Cases)

**Example — A-15 Generate Agent**:
- `presentation/generate/GenerateScreen.kt` (uses AppTextField, AppDropdown, GenderSelector, LevelSlider, PrimaryButton, ExpandableSection)
- `presentation/generate/GenerateViewModel.kt` (injects GenerateBabyName use case)
- `presentation/generate/GenerateState.kt` (GenerateUiState, GenerateEvent, GenerateEffect)

---

#### A-21: Testing Agent

**Responsibility**: Unit tests, UI tests, integration tests.

**Creates**: Test files in `test/` mirroring source structure.

**Consumes**: All completed modules.

---

### Agent DAG (Dependency Graph)

```
                    A-01 (Architecture)
                   /    |    |    \
                  /     |    |     \
            A-02(Theme) | A-04(Nav) A-05(Domain)
                 |      |    |        |
            A-03(UI)    |    |        +--------+
                 \      |    |        |        |
                  \     |    |       A-06     A-08(Net)
                   \    |    |        |        |
                    \   |    |       A-07    A-09(Local)
                     \  |    |        |        |
                A-12→20 (Screens)     A-10(Repo Impl)
                      \ |    |        |
                       A-21 (Testing) A-11(Prompt)
```

### Communication Protocol

| Interaction | Method |
|-------------|--------|
| Agent A depends on Agent B's output | Agent B provides interface/class files |
| Agent A needs to register in NavGraph | Screen Agent provides composable function reference to Navigation Agent |
| Agent A needs DI binding | Architecture Agent creates @Provides/@Binds |
| Agent A discovers bug in B's code | File bug ticket, do not modify B's code |
| Contract ambiguity | Refer to this architecture document |

### Golden Rules

1. **One agent, one package** — never edit outside your assigned path
2. **Never modify another agent's files** — file a ticket
3. **Interfaces before implementations** — domain interfaces first, data implementations later
4. **Tests are mandatory** — every use case and ViewModel must have tests
5. **Preview parameter defaults** — every composable must work in preview with default state
6. **No magic numbers** — all dimensions, colors, strings reference design tokens
