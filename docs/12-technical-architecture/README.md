# 12 — Technical Architecture

## Summary

Modular, provider-agnostic technical architecture supporting Android and Web platforms with replaceable AI backend. Designed for scalability, maintainability, and future migration.

## Reasoning

The architecture must support three critical requirements: (1) AI provider must be swappable without code changes, (2) both platforms must share consistent business logic, (3) system must scale from zero to millions of users without rewrites.

## Recommendation

### High-Level Architecture

```
┌─────────────────────────────────┐
│         Client Layer            │
│  ┌──────────┐  ┌────────────┐   │
│  │ Android  │  │ Web (PWA)  │   │
│  │ Compose  │  │ Next.js    │   │
│  └────┬─────┘  └─────┬──────┘   │
└───────┼───────────────┼─────────┘
        │               │
┌───────┴───────────────┴─────────┐
│         API Gateway             │
│  ┌──────────────────────────┐   │
│  │  Next.js API Routes      │   │
│  │  (or future backend)     │   │
│  └──────────┬───────────────┘   │
└─────────────┼───────────────────┘
              │
┌─────────────┴───────────────────┐
│     AI Abstraction Layer        │
│  ┌──────────────────────────┐   │
│  │  AI Provider Interface   │   │
│  │  ┌──────┐ ┌──────┐      │   │
│  │  │Gemini│ │OpenAI│ ...  │   │
│  │  └──────┘ └──────┘      │   │
│  └──────────┬───────────────┘   │
└─────────────┼───────────────────┘
              │
┌─────────────┴───────────────────┐
│        Data Layer               │
│  ┌──────────┐  ┌────────────┐   │
│  │  Client  │  │  Analytics │   │
│  │ Storage  │  │  (Amplitude│   │
│  │ (Room/   │  │   /Firebase│   │
│  │  LS)     │  │   )       │   │
│  └──────────┘  └────────────┘   │
└─────────────────────────────────┘
```

### AI Provider Abstraction

```
// TypeScript Interface (shared between Web and Android)
interface AIProvider {
  generateNames(preferences: UserPreferences): Promise<AIResult>;
  getNameDetail(name: NameOption): Promise<NameDetail>;
  healthCheck(): Promise<boolean>;
}
```

### Android Architecture

```
┌────────────────────────────────────┐
│          UI Layer                  │
│  ┌──────────────────────────────┐  │
│  │  Composable Screens          │  │
│  │  - HomeScreen               │  │
│  │  - InputScreen              │  │
│  │  - ResultScreen             │  │
│  │  - WishlistScreen           │  │
│  └──────────┬───────────────────┘  │
└─────────────┼──────────────────────┘
┌─────────────┼──────────────────────┐
│      ViewModel Layer               │
│  ┌──────────┴───────────────────┐  │
│  │  ViewModels                 │  │
│  │  - HomeViewModel            │  │
│  │  - InputViewModel           │  │
│  │  - ResultViewModel          │  │
│  │  - WishlistViewModel        │  │
│  └──────────┬───────────────────┘  │
└─────────────┼──────────────────────┘
┌─────────────┼──────────────────────┐
│      Domain Layer                  │
│  ┌──────────┴───────────────────┐  │
│  │  Repository Interface        │  │
│  │  - AIRepository             │  │
│  │  - WishlistRepository       │  │
│  └──────────┬───────────────────┘  │
└─────────────┼──────────────────────┘
┌─────────────┼──────────────────────┐
│      Data Layer                    │
│  ┌──────────┴───────────────────┐  │
│  │  - AIProviderImpl (Retrofit) │  │
│  │  - WishlistDao (Room)        │  │
│  │  - RemoteConfig              │  │
│  └──────────────────────────────┘  │
└────────────────────────────────────┘
```

### Web Architecture

```
┌────────────────────────────────────┐
│       Next.js App                  │
│  ┌──────────────────────────────┐  │
│  │  Pages / Routes              │  │
│  │  - / → Home                 │  │
│  │  - /konsultasi → Input      │  │
│  │  - /hasil → Result           │  │
│  │  - /favorit → Wishlist       │  │
│  └──────────┬───────────────────┘  │
│  ┌──────────┴───────────────────┐  │
│  │  Components                 │  │
│  │  - HomeHero                 │  │
│  │  - InputForm                │  │
│  │  - NameCard                 │  │
│  │  - NameDetail               │  │
│  │  - WishlistGrid             │  │
│  └──────────┬───────────────────┘  │
│  ┌──────────┴───────────────────┐  │
│  │  State (Zustand)            │  │
│  │  - userPreferences          │  │
│  │  - aiResult                 │  │
│  │  - wishlist                 │  │
│  └──────────┬───────────────────┘  │
│  ┌──────────┴───────────────────┐  │
│  │  API Layer                  │  │
│  │  - /api/generate (serverless)│  │
│  │  - AI Provider config       │  │
│  └──────────────────────────────┘  │
└────────────────────────────────────┘
```

### Data Flow

```
User Input → ViewModel/State → API Gateway → AI Provider Interface → Gemini/OpenAI
                                                                          ↓
User ← ViewModel/State ← Parse JSON ← API Gateway ← Structured Response
```

### Technology Stack

| Layer | Android | Web |
|-------|---------|-----|
| Language | Kotlin | TypeScript |
| UI | Jetpack Compose | React + Tailwind CSS |
| Architecture | MVVM + Clean Architecture | Feature-based |
| State | StateFlow + ViewModel | Zustand |
| DI | Hilt | — |
| Network | Retrofit + OkHttp | fetch (Next.js API) |
| Local Storage | Room | localStorage + IndexedDB |
| AI Provider | Gemini SDK / OpenAI SDK | Gemini SDK / OpenAI SDK |
| Analytics | Firebase + Amplitude | Amplitude |
| CI/CD | GitHub Actions | GitHub Actions + Vercel |

### Key Design Decisions

1. **AI Provider Interface**: Every AI provider implements the same interface. Switching requires changing only config.

2. **No Backend for MVP**: MVP uses client-side AI calls (with API key management). Future backend can be added without client changes.

3. **Shared Types**: Define shared types in a monorepo package for consistency.

4. **Client Storage First**: Wishlist stored locally. Cloud sync is a future feature.

5. **Progressive Enhancement**: Web app works as PWA from day 1. Android is native for best performance.

## Implementation

- Phase 1: Set up monorepo structure
- Phase 2: Build AI Provider Interface
- Phase 3: Implement Android app
- Phase 4: Implement Web app
- Phase 5: Integration testing

## Future Improvement

- Add backend service (Node.js/Go) for rate limiting and caching
- Implement gRPC for internal services
- Add CDN for static assets
- Implement caching layer for AI responses
- Add monitoring and observability stack
