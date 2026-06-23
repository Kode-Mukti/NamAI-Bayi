# 13 — Development Roadmap

## Summary

Four-sprint development plan for the NamAI Bayi MVP. Each sprint is 2 weeks (10 working days). Sprints are organized by dependency chain to minimize blocking.

## Reasoning

Multi-Agent Development requires strict sprint ordering. Foundation agents (Architecture, Theme) sprint first. Screen agents sprint later when all dependencies are ready.

## Recommendation

---

### Sprint Overview

```
Sprint 1 │ Foundation  │ Architecture, Theme, Domain, Navigation
Sprint 2 │ Data & AI   │ Network, Database, Prompts, Repository
Sprint 3 │ Screens I   │ Splash, Onboarding, Home, Generate
Sprint 4 │ Screens II  │ Loading, Result, History, Favorite, Settings, Testing
```

---

### Sprint 1: Foundation (Week 1-2)

**Theme**: Everything that everything else depends on.

| Day | Tasks | Agent(s) |
|-----|-------|----------|
| 1-2 | T-001, T-002, T-003, T-004, T-005, T-006 | Architecture |
| 3-4 | T-007, T-008, T-009, T-010, T-011, T-012 | Theme |
| 3-4 | T-013, T-014, T-015 | Navigation |
| 5-10 | T-016 through T-027 (all domain models + repository interfaces) | Domain |
| 5-10 | T-038, T-039 (Database setup) | Data Local |

**Sprint Goal**: App compiles, empty screens navigate, domain models compile.

**Definition of Done**:
- [x]  Project compiles and launches
- [x]  Hilt DI modules compile
- [x]  Theme renders (light + dark)
- [x]  Navigation graph registers all routes
- [x]  All domain models compile
- [x]  All repository interfaces compile
- [x]  Room database compiles

---

### Sprint 2: Data & AI (Week 3-4)

**Theme**: All data sources, AI integration, and business logic.

| Day | Tasks | Agent(s) |
|-----|-------|----------|
| 1-3 | T-040, T-041, T-042, T-043, T-044 | Data Network |
| 1-3 | T-048, T-049, T-050, T-051, T-052 | Data Local |
| 4-6 | T-045, T-046, T-047 | AI/Prompt |
| 4-6 | T-057, T-058, T-059, T-060, T-061, T-062 | AI/Prompt |
| 7-10 | T-053, T-054, T-055, T-056 | Repository Impl |
| 7-10 | T-028 through T-037 (all use cases) | Use Case |
| 1-10 | T-063 through T-071 (all UI components) | UI Component |

**Sprint Goal**: AI can generate names, data persists locally, all use cases work.

**Definition of Done**:
- [x]  OpenRouter API can generate name response
- [x]  AI response parses and validates correctly
- [x]  Strategy selector picks correct strategy
- [x]  Room database saves/reads/clears history and favorites
- [x]  DataStore persists settings
- [x]  All repository implementations compile
- [x]  All use cases compile
- [x]  All 26 UI components render in preview

---

### Sprint 3: Screens I (Week 5-6)

**Theme**: First half of screens — the user journey from open to submit.

| Day | Tasks | Agent(s) |
|-----|-------|----------|
| 1-2 | T-072 Splash | Splash Agent |
| 1-3 | T-073 Onboarding | Onboarding Agent |
| 1-3 | T-074 Home | Home Agent |
| 4-8 | T-075 Generate | Generate Agent |
| 9 | T-090 Integration (partial — splash → generate) | Architecture |
| 10 | T-091 E2E test (partial flow) | Testing |

**Sprint Goal**: User can open app, see onboarding, reach home, fill form, and submit.

**Definition of Done**:
- [ ]  Splash → Onboarding → Home flow works
- [ ]  Generate form renders all fields with state management
- [ ]  Form validation works (client-side)
- [ ]  Submit creates BabyProfile and calls GenerateBabyName use case
- [ ]  Loading screen shows (if result not yet ready — wired in Sprint 4)

---

### Sprint 4: Screens II + Polish (Week 7-8)

**Theme**: Complete the remaining screens, testing, and launch preparation.

| Day | Tasks | Agent(s) |
|-----|-------|----------|
| 1-3 | T-076 Loading | Loading Agent |
| 1-4 | T-077 Result | Result Agent |
| 1-3 | T-078 History | History Agent |
| 1-3 | T-079 Favorite | Favorite Agent |
| 1-3 | T-080 Settings | Settings Agent |
| 4 | T-090 Full integration | Architecture |
| 5-6 | T-081 through T-089 Unit + UI tests | Testing |
| 7 | T-092 Dark mode validation | Theme |
| 7 | T-093 Accessibility pass | UI |
| 8 | T-094 Performance profiling | Architecture |
| 8 | T-095 Crash reporting | Architecture |
| 9 | T-096 Final build | Architecture |
| 10 | Release candidate + smoke tests | All |

**Sprint Goal**: Complete app with all features, tested and ready for beta.

**Definition of Done**:
- [ ]  Full Generate → Loading → Result → Share flow works
- [ ]  History saves and displays past consultations
- [ ]  Favorites add/remove/toggle works
- [ ]  Settings persist and apply
- [ ]  Dark mode works on all screens
- [ ]  All unit tests pass (80%+ coverage)
- [ ]  UI tests for Generate + Result pass
- [ ]  No critical bugs
- [ ]  APK is signed and ready for Play Store

---

### Sprint Dependency Tracking

```
Sprint 1:
  Architecture ──────┬───────── Theme
                     │          UI Components (starts Sprint 2)
                     ├───────── Navigation
                     └───────── Domain Models

Sprint 2:
  Architecture ──────┬───────── Data Network
                     ├───────── Data Local
                     ├───────── AI/Prompt
                     │          Repository Impl (depends on Net + Local + Prompt)
                     │          Use Cases (depends on Repository interfaces)
                     └───────── UI Components (completes)

Sprint 3:
  Navigation ────────┬───────── Splash Screen
                     ├───────── Onboarding Screen
                     ├───────── Home Screen
  UI Components ─────┤
  Use Cases ─────────┤
                     └───────── Generate Screen

Sprint 4:
  Navigation ────────┬───────── Loading Screen
                     ├───────── Result Screen
                     ├───────── History Screen
                     ├───────── Favorite Screen
                     ├───────── Settings Screen
  All ───────────────┴───────── Testing
```

---

### Post-MVP Roadmap

| Sprint | Feature | Agent(s) |
|--------|---------|----------|
| Sprint 5 | Name Battle, Public Share Links | New screen agents |
| Sprint 6 | Daily Name Push, Session Persistence | Enhancement |
| Sprint 7 | Premium Tier, Payment Integration | New monetization agent |
| Sprint 8 | Couple Mode, Family Voting | New screen agents |
