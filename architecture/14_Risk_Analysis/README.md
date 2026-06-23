# 14 — Risk Analysis

## Summary

Technical, architectural, and project risks specific to the NamAI Bayi Android MVP. Each risk includes probability, impact, mitigation, and the agent responsible.

## Reasoning

Multi-Agent Development introduces unique risks: agents block each other, interfaces drift, and integration surprises multiply. Proactive risk identification keeps the project on track.

## Recommendation

---

### Technical Architecture Risks

| # | Risk | P | I | Mitigation | Owner |
|---|------|---|---|------------|-------|
| R-01 | Hilt compile errors due to circular DI dependencies | M | H | Keep DI flat; Module → binds only; no circular references | Architecture Agent |
| R-02 | Room migration fails after schema change | L | H | Add auto-migration; test schema changes before production | Data Local Agent |
| R-03 | ProGuard/R8 strips AI response models at runtime | M | H | Add @Keep annotation to all DTOs and serialization models | Architecture Agent |
| R-04 | Kotlin version incompatibility between Compose, Hilt, Room | M | M | Use BOM for Compose, consistent Kotlin version in `libs.versions.toml` | Architecture Agent |
| R-05 | Navigation Compose type-safety issues with arguments | M | M | Define all nav args in NavArgs.kt; test argument passing | Navigation Agent |

### Multi-Agent Risks

| # | Risk | P | I | Mitigation | Owner |
|---|------|---|---|--------------|-------|
| R-06 | Screen agents use components before UI Agent finishes them | H | H | UI Agent completes all components in Sprint 2 before screen agents start Sprint 3 | Architecture Agent (scheduling) |
| R-07 | Domain models change after data layer already implemented | M | H | Freeze domain model API after Sprint 1; changes require Architecture Agent approval | Architecture Agent |
| R-08 | Repository interface changes break Use Cases | M | M | Repository interfaces frozen after A-06 completes; Use Case agent works from frozen interfaces | Domain Agent |
| R-09 | Screen agents implement duplicate functionality | L | M | Shared components in `core/ui/component/` prevent this by design | UI Agent |
| R-10 | Agent commits break other agents' code | M | H | Branch per agent; code review by Architecture Agent before merge | Architecture Agent |
| R-11 | Interface drift — domain model field name ≠ entity field name | M | H | Shared mapping specification; mapper tests verify all fields | Testing Agent |

### AI-Specific Risks

| # | Risk | P | I | Mitigation | Owner |
|---|------|---|---|------------|-------|
| R-12 | OpenRouter API key exposed in APK | M | C | API calls go through server-side proxy (future); MVP uses client-side with key in BuildConfig (obfuscated) | Architecture Agent |
| R-13 | AI response does not match JSON schema | M | H | Strict validation in `AIResponseValidator`; fallback UI for parse errors | AI/Prompt Agent |
| R-14 | AI response time exceeds 15 seconds | H | M | Show progress animation; 15s timeout with retry button | Screen: Loading Agent |
| R-15 | AI provider rate limits the app | L | M | Implement client-side rate limiting (max 10 requests/minute) | Data Network Agent |
| R-16 | AI generates culturally insensitive names | L | C | System prompt guardrails; response validation; cultural checklists | AI/Prompt Agent |
| R-17 | Empty/blank input generates low-quality results | M | M | Strategy selector defaults to "AI_SELECTED" when input is minimal | AI/Prompt Agent |

### Data & Persistence Risks

| # | Risk | P | I | Mitigation | Owner |
|---|------|---|---|------------|-------|
| R-18 | Room database corruption on crash | L | H | Add `fallbackToDestructiveMigration()`; backup mechanism in future | Data Local Agent |
| R-19 | DataStore preference read before write creates NPE | L | M | Always initialize DataStore with defaults before first read | Data Local Agent |
| R-20 | Large history reduces app performance | M | L | Limit history to 50 items; paginate if needed | Data Local Agent |
| R-21 | JSON serialization of BabyProfile fails for special characters | L | M | Use Kotlinx Serialization; test with Arabic/Javanese characters | AI/Prompt Agent |

### UI/UX Risks

| # | Risk | P | I | Mitigation | Owner |
|---|------|---|---|------------|-------|
| R-22 | Bottom navigation interferes with keyboard on Generate screen | M | M | `WindowInsets` handling; scroll on keyboard show | Screen: Generate Agent |
| R-23 | Loading animation stutters on low-end devices | M | M | Use Canvas-based animation (not Lottie) for MVP; test on API 26+ | UI Component Agent |
| R-24 | Dark mode text contrast on some screens | M | M | Theme Agent validates all color pairs against WCAG AA | Theme Agent |
| R-25 | Preview data mismatch — agent tests with wrong default values | M | L | All composables accept explicit state; no hidden defaults | UI Component Agent |

### Project Management Risks

| # | Risk | P | I | Mitigation | Owner |
|---|------|---|---|------------|-------|
| R-26 | Agent delivers late — blocks dependent agents | M | H | Parallel agent scheduling; sprint buffer days (2 per sprint) | Architecture Agent |
| R-27 | Requirements ambiguity during development | M | M | Every task references an architecture document section; questions go to Architecture Agent | Architecture Agent |
| R-28 | Testing Agent starts before source code is stable | H | M | Tests written against frozen interfaces; mock implementations available | Testing Agent |
| R-29 | Build time exceeds 10 minutes locally | M | L | Modularize incrementally; CI runs on GitHub Actions with caching | Architecture Agent |
| R-30 | Play Store rejects app for content policy | L | H | Review policy before launch; age rating: 3+; no inappropriate content | Architecture Agent |

---

### Risk Heat Map

```
Severity
  Critical │ R-12 R-16 R-30
           │
     High  │ R-01 R-03 R-06 R-07 R-13 R-18 R-22 R-26 R-27
           │
   Medium  │ R-04 R-05 R-08 R-14 R-17 R-23 R-24 R-28
           │
     Low   │ R-02 R-09 R-15 R-19 R-20 R-21 R-25 R-29
           └─────────────────────────────────────────
              Low     Medium      High      Critical
                               Probability
```

---

### Risk Response Plan

**Critical Risks (immediate action)**:

| Risk | Response | When |
|------|----------|------|
| R-12 (API key exposed) | Obfuscate in BuildConfig; add server proxy to roadmap | Sprint 1 |
| R-16 (Inappropriate names) | System prompt guardrails + validation | Sprint 2 |
| R-30 (Play Store rejection) | Review policies before listing creation | Sprint 2 |

**High Risks (active monitoring)**:

| Risk | Response | Owner |
|------|----------|-------|
| R-01 (DI circular) | Architecture Agent reviews all DI modules weekly | Architecture |
| R-06 (UI Components late) | UI Agent prioritized; Architecture tracks daily | Architecture |
| R-07 (Model changes) | Freeze domain model after Sprint 1 | Domain |
| R-13 (JSON parse fails) | Validate every AI response; flag failures to AI Agent | AI/Prompt |
| R-18 (DB corruption) | Migration testing in Sprint 4 | Data Local |
| R-22 (Keyboard overlap) | Test on small screen emulator in Sprint 3 | Generate Agent |
| R-26 (Agent delay) | Buffer days; Architecture reprioritizes if needed | Architecture |
| R-27 (Ambiguity) | Architecture Agent available for questions daily | Architecture |

---

### Contingency Budget

| Item | Allocation | Notes |
|------|-----------|-------|
| Sprint buffer | 2 days per sprint | Overhead for agent delays |
| AI cost | $50/month for MVP testing | OpenRouter credits |
| Re-testing | 1 day per sprint | Regression after integration |
| Documentation | Included in each task | Architecture Agent validates |

---

### Risk Monitoring Cadence

| When | Activity |
|------|----------|
| Daily standup | Blockers raised; Architecture tracks risk RAG status |
| Sprint planning | Risk matrix reviewed; new risks added |
| After integration | Validate R-06, R-07, R-08 (interface drift) |
| Before release | Full risk review; R-30 (Play Store) confirmed |
