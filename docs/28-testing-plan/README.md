# 28 — Testing Plan

## Summary

Comprehensive testing strategy covering AI output quality, unit tests, integration tests, UI tests, user acceptance, and performance. Quality is everyone's responsibility.

## Reasoning

NamAI Bayi's core value is AI-generated name recommendations. If the AI produces low-quality results, the product fails. Testing must cover both traditional software quality and AI output quality — two very different testing domains.

## Recommendation

### Testing Pyramid

```
            ╱  E2E (Cypress/Compose UI)  ╲          ← Few
           ╱   Integration (API + AI)      ╲         ← Medium
          ╱    Unit (ViewModel, UseCase)      ╲       ← Many
         ╱     AI Quality (Prompt Tests)        ╲     ← Critical
        ╱      Manual / UAT                       ╲   ← Essential
```

### AI Quality Testing (Highest Priority)

#### Test Categories

| Category | Tests | Frequency |
|----------|-------|-----------|
| Cultural Appropriateness | 50+ test cases across all religions and cultures | Every prompt change |
| Name Quality | 100+ test cases for name validity, meaning, pronunciation | Weekly |
| Strategy Selection | 50+ test cases covering all 13 strategies | Every prompt change |
| Edge Cases | Empty input, extreme values, conflicting preferences | Every release |
| Bias Detection | Gender, religion, ethnic bias checks | Monthly |

#### AI Test Cases

```
// Test Suite: Cultural Appropriateness
Test 1.1: User Muslim → Should NOT get Christian-only names
Test 1.2: User Hindu → Should NOT get Islamic-only names
Test 1.3: User Sundanese → Should include Sundanese options
Test 1.4: User Bali-Hindu → Should consider caste conventions
Test 1.5: User Chinese-Indonesian → Should consider international names

// Test Suite: Name Quality
Test 2.1: Names should have valid meaning in Bahasa
Test 2.2: Names should be pronounceable in Indonesian context
Test 2.3: Names should not have negative connotations
Test 2.4: Full name suggestions should flow well
Test 2.5: Sibling compatibility should be coherent

// Test Suite: Strategy Selection
Test 3.1: Islam + Traditional → Strategy: Islami Tradisional
Test 3.2: Jawa + No religion → Strategy: Jawa
Test 3.3: High unique + No other input → Strategy: AI Generated
Test 3.4: Father + Mother name → Strategy: Kombinasi Orang Tua
Test 3.5: Korea-inspired → Strategy: Korea-Inspired

// Test Suite: Edge Cases
Test 4.1: Empty input → AI should ask for more info gracefully
Test 4.2: Conflicting preferences → AI should resolve gracefully
Test 4.3: Very long meaning text → Should still produce quality names
Test 4.4: Symbols in input → Should sanitize
Test 4.5: Extremely rare culture → Should handle gracefully
```

#### AI Quality Scoring Rubric

| Criterion | Weight | Scoring (1-5) | Minimum Acceptable |
|-----------|--------|---------------|-------------------|
| Cultural appropriateness | 30% | 1-5 | 4.0 |
| Name validity | 20% | 1-5 | 4.5 |
| Explanation quality | 15% | 1-5 | 3.5 |
| Uniqueness differentiation | 15% | 1-5 | 3.5 |
| Format compliance | 10% | 1-5 | 5.0 |
| Response time | 10% | 1-5 | 4.0 |
| **Composite** | **100%** | **1-5** | **4.0** |

### Unit Testing

| Layer | Framework | Coverage Target |
|-------|-----------|----------------|
| ViewModel (Android) | JUnit + MockK + Turbine | 90% |
| State management (Web) | Jest + Vitest | 90% |
| Use Cases | JUnit / Jest | 95% |
| Repository | JUnit / Jest + Mock | 90% |
| Validation (Zod/schema) | Jest | 100% |
| Strategy selection logic | Jest | 100% |
| Utility functions | Jest | 95% |

### Integration Testing

| Scope | Tool | Tests |
|-------|------|-------|
| AI Provider → API Route | Jest + MSW | Provider switching, error handling, timeout |
| Room Database → Repository | Room testing | Save, read, delete wishlist |
| API Route → AI Response | Jest + mock provider | Response validation, error codes |
| State → Persistence | Jest / JUnit | Zustand persist, Room migration |

### UI Testing

| Platform | Tool | Scope |
|----------|------|-------|
| Android Compose | Compose UI Test | All screens, navigation, form validation |
| Web Components | Testing Library | Component rendering, user interactions |
| Cross-platform | Manual | Visual consistency check |

#### UI Test Scenarios

```
Test UI-1: Home → Tap "Mulai" → Navigate to Input
Test UI-2: Input → Fill all fields → Submit → Navigate to Loading
Test UI-3: Input → Submit empty → Navigate to Loading (all optional)
Test UI-4: Loading → AI success → Navigate to Result
Test UI-5: Loading → AI error → Show error with retry
Test UI-6: Result → Tap name card → Show detail sheet
Test UI-7: Result → Save name → Heart fills, count updates
Test UI-8: Result → Share WA → Opens WA intent
Test UI-9: Wishlist → View saved → Show saved names
Test UI-10: Wishlist → Remove → Name disappears
Test UI-11: Result → "Konsultasi Lagi" → Back to Input with previous values
```

### End-to-End Testing

| Tool | Scenarios |
|------|-----------|
| Playwright (Web) | Full user journey: Home → Input → AI → Result → Share → Wishlist |
| Kaspresso (Android) | Same full journey on Android |
| Manual | Real device testing on 5+ Android devices |

### Performance Testing

| Test | Tool | Target |
|------|------|--------|
| AI response time | Custom timing | < 8 seconds |
| App startup time | Firebase Performance | < 2 seconds |
| Screen transition | Manual profiling | < 300ms |
| Bundle size | Webpack Bundle Analyzer | < 150KB JS |
| Memory usage | Android Profiler | < 100MB |
| Network calls | Charles Proxy | < 3 per flow |
| Offline behavior | Network throttling | Graceful degradation |

### User Acceptance Testing (UAT)

#### Test Group: 20 Indonesian Parents

| Profile | Users | Test Focus |
|---------|-------|------------|
| Muslim, city-dwelling | 8 | Islamic names, UI comprehension |
| Hindu/Bali/Non-Muslim | 4 | Cultural accuracy, religious fit |
| Traditional parents | 4 | Javanese/Sundanese names, ease of use |
| Tech-savvy parents | 4 | AI quality, comparison with ChatGPT |

#### UAT Questionnaire (after testing)

```
1. How satisfied are you with the name recommendations? (1-5)
2. Did the AI understand your cultural/religious preferences? (1-5)
3. Was the explanation helpful in making a decision? (1-5)
4. How likely are you to share this app with friends? (1-5)
5. Would you pay for unlimited consultations? (Yes/No/Maybe)
6. What name did you like most? Why?
7. What would you improve?
8. Would you use this app again?
```

### Testing Execution Plan

| Phase | Activities | Timeline |
|-------|------------|----------|
| Sprint Testing | Unit + Integration, AI quality tests per sprint | Every sprint |
| Code Freeze | Full test suite, performance, security | 1 week before beta |
| Beta | UAT with 20 users, internal dogfooding | 2 weeks |
| Release Candidate | Regression, edge cases, device matrix | 1 week |
| Post-Launch | Monitoring, bug fixing, AI quality monitoring | Ongoing |

### Bug Taxonomy

| Severity | Definition | Response Time |
|----------|-----------|-------------|
| Critical | App crashes, AI always fails, data loss | < 4 hours |
| High | Feature broken, wrong AI output, payment fails | < 24 hours |
| Medium | UI glitch, minor wrong label, slow but usable | < 1 week |
| Low | Typo, minor alignment, minor visual issue | Next sprint |

## Implementation

- Set up testing frameworks in Sprint 1
- AI quality tests are highest priority
- Automate tests in CI/CD pipeline
- Document test cases as they are created
- Run full suite before every release

## Future Improvement

- Visual regression testing (Chromatic/Percy)
- AI output monitoring in production
- Automated bias detection pipeline
- Performance budget in CI
- Crowd-sourced UAT with incentives
