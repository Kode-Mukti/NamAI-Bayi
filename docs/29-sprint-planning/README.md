# 29 — Sprint Planning

## Summary

Sprint-by-sprint plan for MVP development. 8 sprints of 2 weeks each (16 weeks total) from project start to public launch.

## Reasoning

Agile sprints with 2-week cadence balance speed with quality. Each sprint has a clear theme, deliverables, and success criteria. Sprints are ordered by dependency — foundational work first, features later.

## Recommendation

### Sprint Overview

```
Sprint 1 │ Foundation    │ Project setup, architecture, CI/CD
Sprint 2 │ Core UI       │ Home + Input screens (both platforms)
Sprint 3 │ AI Engine     │ Provider layer, prompt engineering, API
Sprint 4 │ Result Screen │ Result display, name detail, wishlist
Sprint 5 │ Viral & Save  │ Share, wishlist persistence, polish
Sprint 6 │ Web Launch    │ SEO, PWA, web-specific features
Sprint 7 │ Beta & Fix    │ Beta release, bug fixes, UAT
Sprint 8 │ Launch        │ Play Store, production release
```

### Sprint 1: Foundation (Week 1-2)

**Theme**: Set up everything needed to start building

**Goals**:
- Set up monorepo structure (Android + Web)
- Configure CI/CD (GitHub Actions)
- Set up AI Provider Interface
- Create design system tokens
- Set up analytics (Amplitude + Firebase)
- Initialize databases (Room + schema)

**Deliverables**:
| Item | Platform | Points |
|------|----------|--------|
| Monorepo structure | Both | 3 |
| Next.js project scaffold | Web | 2 |
| Android project scaffold | Android | 2 |
| CI/CD pipeline | Both | 3 |
| AI Provider interface (TypeScript + Kotlin) | Both | 5 |
| Design token implementation | Both | 3 |
| Firebase + Amplitude setup | Both | 2 |
| Room database setup | Android | 2 |
| Zod schema for validation | Web | 2 |
| **Total** | | **24 points** |

**Risks**: Tooling compatibility, Android SDK setup time

### Sprint 2: Core UI (Week 3-4)

**Theme**: Build the first two screens users interact with

**Goals**:
- Home screen with branding and CTA
- Input form with all fields (optional)
- Form validation and submission
- Navigation between screens
- Loading state design

**Deliverables**:
| Item | Platform | Points |
|------|----------|--------|
| Home screen | Both | 3 |
| Input form layout | Both | 5 |
| Form state management | Both | 3 |
| All input components (text, dropdown, slider) | Both | 5 |
| Navigation graph | Both | 2 |
| Animated loading screen | Both | 3 |
| Responsive layout (Web) | Web | 2 |
| **Total** | | **23 points** |

### Sprint 3: AI Engine (Week 5-6)

**Theme**: Connect input to AI and get structured results

**Goals**:
- Implement Gemini provider
- Implement OpenAI provider (fallback)
- Build /api/generate endpoint
- Build prompt library (v1)
- Strategy selection logic
- Response validation and parsing
- Loading → Result transition

**Deliverables**:
| Item | Platform | Points |
|------|----------|--------|
| Gemini provider implementation | Both | 5 |
| /api/generate endpoint | Web | 3 |
| Prompt library (system + user) | Both | 5 |
| Strategy selection logic | Both | 3 |
| Response validation | Both | 3 |
| Error handling + retry | Both | 3 |
| Provider configuration | Both | 2 |
| **Total** | | **24 points** |

### Sprint 4: Result Screen (Week 7-8)

**Theme**: Display AI recommendations beautifully

**Goals**:
- Result screen with 5 name cards
- Expandable name detail
- Uniqueness score visualization
- Strategy badge
- Save to wishlist (heart)
- Wishlist screen (basic)

**Deliverables**:
| Item | Platform | Points |
|------|----------|--------|
| Result screen layout | Both | 5 |
| Name card component | Both | 3 |
| Name detail bottom sheet | Both | 5 |
| Strategy badge component | Both | 2 |
| Score bar visualization | Both | 2 |
| Save to wishlist (heart) | Both | 2 |
| Wishlist screen | Both | 3 |
| State management for result | Both | 3 |
| **Total** | | **25 points** |

### Sprint 5: Viral & Save (Week 9-10)

**Theme**: Make sharing and saving delightful

**Goals**:
- Share to WhatsApp (formatted card)
- Share to Instagram Story
- Wishlist persistence (Room/LocalStorage)
- Remove from wishlist
- Session persistence
- Share card design

**Deliverables**:
| Item | Platform | Points |
|------|----------|--------|
| Share to WhatsApp | Both | 3 |
| Share to Instagram Story | Both | 3 |
| Share card/link generation | Both | 3 |
| Wishlist persistence | Both | 3 |
| Session persistence | Both | 2 |
| Micro-interactions (animations) | Both | 3 |
| Polish + edge cases | Both | 3 |
| **Total** | | **20 points** |

### Sprint 6: Web Launch (Week 11-12)

**Theme**: Polish web version for production

**Goals**:
- SEO metadata for all pages
- PWA manifest + service worker
- Open Graph for sharing
- Performance optimization
- Lighthouse score > 90
- Sitemap generation

**Deliverables**:
| Item | Platform | Points |
|------|----------|--------|
| SEO metadata | Web | 3 |
| PWA setup | Web | 3 |
| Performance optimization | Web | 5 |
| Sitemap | Web | 2 |
| Structured data (JSON-LD) | Web | 2 |
| Accessibility audit | Both | 3 |
| Dark mode | Both | 3 |
| **Total** | | **21 points** |

### Sprint 7: Beta & Fix (Week 13-14)

**Theme**: Stabilize, test, and prepare for launch

**Goals**:
- Full test suite pass
- UAT with 20 users
- Bug fixes from beta
- AI quality testing
- Performance profiling
- Crash reporting

**Deliverables**:
| Item | Points |
|------|--------|
| Beta APK + Web deploy | 3 |
| UAT session (20 users) | 5 |
| Bug fixes | 8 |
| AI quality improvements | 5 |
| Crash reporting setup | 2 |
| Performance fixes | 3 |
| **Total** | | **26 points** |

### Sprint 8: Launch (Week 15-16)

**Theme**: Ship to production

**Goals**:
- Play Store submission
- Production web deploy
- Launch marketing materials
- Monitoring dashboards
- Post-launch support

**Deliverables**:
| Item | Points |
|------|--------|
| Play Store listing | 3 |
| Production deploy (Web) | 2 |
| Monitoring setup | 3 |
| Launch checklist completion | 3 |
| Marketing assets | 3 |
| Post-launch bug tracking | 2 |
| **Total** | | **16 points** |

### Velocity Tracking

| Sprint | Planned | Completed | Velocity |
|--------|---------|-----------|----------|
| 1 | 24 | — | — |
| 2 | 23 | — | — |
| 3 | 24 | — | — |
| 4 | 25 | — | — |
| 5 | 20 | — | — |
| 6 | 21 | — | — |
| 7 | 26 | — | — |
| 8 | 16 | — | — |
| **Total** | **179** | **—** | **—** |

### Definition of Done

- Code reviewed and merged
- Unit tests pass (90%+ coverage for new code)
- AI quality tests pass (4.0+ composite)
- Manual testing on 3 devices
- No critical bugs
- Analytics events implemented
- Accessibility checked
- Performance within budget

### Ceremonies

| Ceremony | Schedule | Duration |
|----------|----------|----------|
| Sprint Planning | Sprint start | 2 hours |
| Daily Standup | Daily | 15 min |
| Sprint Review | Sprint end | 1 hour |
| Sprint Retro | Sprint end | 1 hour |
| Backlog Grooming | Mid-sprint | 30 min |

## Implementation

- Use Linear or GitHub Projects for sprint tracking
- Create tickets per deliverable
- Estimate in story points (1, 2, 3, 5, 8, 13)
- Track velocity after Sprint 1
- Adjust scope based on actual velocity

## Future Improvement

- Introduce bug bash days
- Automate sprint reporting
- Add capacity planning for P1 features
- Split into 1-week sprints for faster feedback
- Introduce feature flags for gradual rollout
