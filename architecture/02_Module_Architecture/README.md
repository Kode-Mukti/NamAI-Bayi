# 02 — Module Architecture

## Summary

Complete breakdown of every architectural module. Each module is a bounded context with explicit responsibilities, public contract, and dependencies on other modules.

## Reasoning

Multi-Agent Development requires strict module boundaries. Agents must know exactly what their module provides and what it consumes. Violation of module boundaries is a design error.

## Recommendation

### Module Dependency Graph

```
core (no dependencies)
  ↑
domain (depends on: core)
  ↑
data (depends on: domain, core)
  ↑
presentation (depends on: domain, core)
```

**Note**: This is logical only (single Gradle module). Physical separation would add complexity without benefit for MVP.

---

### Module: core

**Path**: `com.kodemukti.namaibayi.core`

**Purpose**: Foundation layer. Agnostic of features. No business logic.

**Sub-modules**:

| Sub-module | Contents | Owned By |
|------------|----------|----------|
| `core/common` | `Constants`, `Extensions`, `Result.kt`, `UiState.kt` | Architecture Agent |
| `core/di` | All Hilt modules (`AppModule`, `DataModule`, `DomainModule`, `AIModule`) | Architecture Agent |
| `core/network` | `OpenRouterApi` (Retrofit), `NetworkConfig`, `ApiInterceptor` | Data Agent |
| `core/database` | `NamAIDatabase` (Room), `Converters`, `DatabaseConfig` | Data Agent |
| `core/ui/theme` | `Theme.kt`, `Color.kt`, `Type.kt`, `Shape.kt`, `Elevation.kt` | Theme Agent |
| `core/ui/component` | All reusable Composables | UI Agent |
| `core/ui/navigation` | `NavGraph.kt`, `Screen.kt`, `NavArgs.kt` | Navigation Agent |
| `core/util` | `ShareUtil`, `DateUtil`, `ValidationUtil`, `JsonUtil` | Architecture Agent |

**Public Contract**:
```kotlin
// What other modules can use from core:
- Theme (Theme.kt, Color.kt, Type.kt, Shape.kt)
- Components (PrimaryButton, NameCard, etc.)
- Navigation (Screen sealed class, NavGraph)
- Result<T> sealed class
- UiState<T> sealed class
- Extension functions
- Constants
```

**Dependencies**: None (pure Kotlin/Android SDK)

---

### Module: domain

**Path**: `com.kodemukti.namaibayi.domain`

**Purpose**: Business logic and enterprise rules. Pure Kotlin, no Android dependencies. Fully testable without instrumented tests.

**Sub-modules**:

| Sub-module | Contents | Owned By |
|------------|----------|----------|
| `domain/model` | All data models (see Data Model doc) | Domain Agent |
| `domain/repository` | Repository interfaces (contracts) | Domain Agent |
| `domain/usecase` | All use cases | Domain Agent |

**Public Contract**:
```kotlin
// What other modules can use from domain:
- All model classes
- Repository interfaces
- UseCase classes
- NamingStrategy enum
```

**Dependencies**: `core` (for `Result<T>`)

---

### Module: data

**Path**: `com.kodemukti.namaibayi.data`

**Purpose**: Implementations of repository interfaces. Data sources (remote AI, local Room, DataStore). All technology choices live here.

**Sub-modules**:

| Sub-module | Contents | Owned By |
|------------|----------|----------|
| `data/remote/dto` | Network DTOs (`OpenRouterRequest`, `OpenRouterResponse`) | AI/Prompt Agent |
| `data/remote/ai` | `AIProvider` interface, `OpenRouterProvider`, `MockAIProvider` | AI/Prompt Agent |
| `data/local/entity` | Room entities (`BabyResultEntity`, `FavoriteEntity`, `HistoryEntity`) | Data Agent |
| `data/local/dao` | Room DAOs | Data Agent |
| `data/local/datastore` | DataStore for settings + onboarding flag | Data Agent |
| `data/repository` | Repository implementations | Data Agent |

**Public Contract**:
```kotlin
// What other modules can use from data:
// (Nothing directly — presentation talks to domain only)
// Data module exists so DI can provide implementations to domain interfaces.
```

**Dependencies**: `domain`, `core`

---

### Module: presentation

**Path**: `com.kodemukti.namaibayi.presentation`

**Purpose**: UI layer. Each feature is a self-contained sub-package with Screen + ViewModel + State.

**Sub-modules**:

| Sub-module | Screen | Owned By |
|------------|--------|----------|
| `presentation/splash` | SplashScreen | Screen: Splash Agent |
| `presentation/onboarding` | OnboardingScreen | Screen: Onboarding Agent |
| `presentation/home` | HomeScreen | Screen: Home Agent |
| `presentation/generate` | GenerateScreen | Screen: Generate Agent |
| `presentation/loading` | LoadingScreen | Screen: Loading Agent |
| `presentation/result` | ResultScreen | Screen: Result Agent |
| `presentation/favorite` | FavoriteScreen | Screen: Favorite Agent |
| `presentation/history` | HistoryScreen | Screen: History Agent |
| `presentation/settings` | SettingsScreen | Screen: Settings Agent |

**Per Feature Structure**:
```
FeatureName/
├── FeatureScreen.kt       # Composable
├── FeatureViewModel.kt    # ViewModel (Hilt-injected)
└── FeatureState.kt        # UI State + Events
```

**Public Contract**: None (presentation is the top-most layer)
**Dependencies**: `domain`, `core`

---

### Module Boundary Rules

| Rule | Description |
|------|-------------|
| **Strict Layering** | presentation → domain → core (data sits beside domain) |
| **No Skip Layers** | presentation never imports data directly |
| **Domain is Pure** | domain imports nothing Android-specific |
| **Interface in Domain** | Repository interfaces belong in domain, implementations in data |
| **ViewModel in Presentation** | ViewModel in `presentation/*/`, injected via Hilt |
| **Core is Read-Only** | Core components are created by their owning agents; other agents only consume |
| **DTOs Stay in Data** | Network response models never leak to presentation |

---

### Agent Module Ownership

```
┌─────────────────────────────────────────────────────────┐
│                Architecture Agent                        │
│  Creates: core/common, core/di, core/util                │
│  Maintains: module structure, build config               │
├─────────────────────────────────────────────────────────┤
│  ┌─────────────────┐  ┌──────────────────────────────┐  │
│  │   Theme Agent   │  │      UI Agent                │  │
│  │  core/ui/theme  │  │  core/ui/component           │  │
│  └─────────────────┘  └──────────────────────────────┘  │
│  ┌─────────────────┐  ┌──────────────────────────────┐  │
│  │ Navigation Agent│  │      Data Agent              │  │
│  │ core/ui/nav     │  │  data/local/*                │  │
│  └─────────────────┘  │  core/network                │  │
│  ┌─────────────────┐  │  core/database               │  │
│  │  Domain Agent   │  └──────────────────────────────┘  │
│  │  domain/        │  ┌──────────────────────────────┐  │
│  └─────────────────┘  │      AI/Prompt Agent          │  │
│  ┌─────────────────┐  │  data/remote/ai               │  │
│  │  Screen Agents  │  │  data/remote/dto              │  │
│  │  presentation/* │  └──────────────────────────────┘  │
│  └─────────────────┘                                    │
└─────────────────────────────────────────────────────────┘
```
