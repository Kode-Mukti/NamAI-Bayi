# 08 — User Stories

## Summary

41 user stories covering MVP, viral, and future features. Organized by epic with acceptance criteria for development.

## Reasoning

User stories translate product requirements into actionable development tasks. Each story follows the standard "As a... I want... So that..." format with clear acceptance criteria for testing.

## Recommendation

### Epic 1: Onboarding & Home

| ID | Story | Acceptance Criteria | Priority |
|----|-------|-------------------|----------|
| US-001 | As a new user, I want to see a beautiful home screen so that I understand what the app does | Tagline, description, CTA button visible; "Mulai Konsultasi" button prominent | P0 |
| US-002 | As a user, I want to see the Kode Mukti branding so that I recognize the parent brand | Logo, brand colors, footer with "by Kode Mukti" | P0 |
| US-003 | As a returning user, I want to see my last session so that I can continue where I left off | Last session preview, tap to continue | P1 |

### Epic 2: Input & Preferences

| ID | Story | Acceptance Criteria | Priority |
|----|-------|-------------------|----------|
| US-004 | As a user, I want to enter my preferences so that the AI can find the best name | Form with all optional fields; progress indicator | P0 |
| US-005 | As a user, I want every field to be optional so that I can start quickly | All fields optional; submit button always enabled | P0 |
| US-006 | As a user, I want to see field descriptions so that I understand what to enter | Tooltip/hint below each field | P0 |
| US-007 | As a user, I want to select religion and culture so that the AI suggests appropriate names | Dropdown with agama and budaya options | P0 |
| US-008 | As a user, I want to enter father and mother names so that AI can consider combinations | Text input for nama ayah, nama ibu | P0 |
| US-009 | As a user, I want to specify unique level so that the AI knows how rare the name should be | Slider from 1-5 for unique level | P1 |
| US-010 | As a user, I want to specify traditional level so that the AI knows my preference | Slider from 1-5 for traditional level | P1 |
| US-011 | As a user, I want to enter first/last letter preferences so that the name starts/ends with specific letters | Letter picker for huruf awal, huruf akhir | P1 |
| US-012 | As a user, I want to add favorite names so that the AI understands my taste | Text input, add multiple names | P1 |
| US-013 | As a user, I want to add disliked names so that the AI avoids them | Text input, add multiple names | P1 |

### Epic 3: AI Result

| ID | Story | Acceptance Criteria | Priority |
|----|-------|-------------------|----------|
| US-014 | As a user, I want to see loading animation while AI generates so that I know it's working | Beautiful loading animation with estimated time | P0 |
| US-015 | As a user, I want to see top 5 name recommendations so that I can choose | 5 name cards with key info visible | P0 |
| US-016 | As a user, I want to see explanation for each name so that I understand the recommendation | Expandable detail: meaning, philosophy, pronunciation, origin, cultural relevance, uniqueness, popularity, international readability, full name, sibling compatibility, why AI recommends | P0 |
| US-017 | As a user, I want to see a uniqueness score so that I know how rare the name is | Score 1-100 with label (Sangat Unik, Unik, Cukup Unik, Populer, Sangat Populer) | P0 |
| US-018 | As a user, I want to see popularity estimate so that I know how common the name is | Estimate with label (Sangat Populer, Populer, Cukup Populer, Jarang, Sangat Jarang) | P0 |
| US-019 | As a user, I want to see international readability so that I know if foreigners can pronounce it | Label: Mudah, Sedang, Sulit | P1 |
| US-020 | As a user, I want to see the naming strategy used so that I understand the AI's approach | Strategy badge (e.g., "Strategi: Islami Modern") | P0 |

### Epic 4: Wishlist & Save

| ID | Story | Acceptance Criteria | Priority |
|----|-------|-------------------|----------|
| US-021 | As a user, I want to save names to wishlist so that I can compare later | Heart icon on cards; saved names in wishlist tab | P0 |
| US-022 | As a user, I want to view all saved names so that I can compare | Wishlist screen with all saved names, tap for detail | P0 |
| US-023 | As a user, I want to remove names from wishlist so that I can curate | Swipe or tap to remove with confirmation | P0 |
| US-024 | As a user, I want to mark a name as "chosen" so that I remember my final decision | "Pilih" button; badge on chosen name | P1 |

### Epic 5: Sharing & Viral

| ID | Story | Acceptance Criteria | Priority |
|----|-------|-------------------|----------|
| US-025 | As a user, I want to share a name on WhatsApp so that I can ask my spouse's opinion | Share to WA with formatted card | P0 |
| US-026 | As a user, I want to share on Instagram Story so that I can get friends' opinions | Instagram Story integration with template | P0 |
| US-027 | As a user, I want to share a public link so that family can see my shortlist | Generate unique public link | P1 |
| US-028 | As a user, I want to create a QR code so that I can share offline | QR code for each name or wishlist | P2 |
| US-029 | As a user, I want to battle two names so that I can decide between them | Side-by-side comparison with vote | P1 |
| US-030 | As a user, I want to vote on names so that I can involve family | Vote button; real-time results | P1 |
| US-031 | As a user, I want to get a daily name suggestion so that I discover new names | Push notification with daily name | P1 |

### Epic 6: Premium (Future)

| ID | Story | Acceptance Criteria | Priority |
|----|-------|-------------------|----------|
| US-032 | As a user, I want to upgrade to Premium so that I get deeper AI analysis | Upgrade flow; payment integration | Future |
| US-033 | As a user, I want unlimited AI consultations so that I can explore as many names as I want | Remove daily limit for premium | Future |
| US-034 | As a user, I want AI chat so that I can ask follow-up questions | Conversational AI interface | Future |

### Epic 7: Technical

| ID | Story | Acceptance Criteria | Priority |
|----|-------|-------------------|----------|
| US-035 | As a developer, I want to switch AI providers without code changes so that I can optimize cost | Config-based provider selection | P0 |
| US-036 | As a developer, I want structured JSON output so that I can parse results reliably | Strict JSON schema validation | P0 |
| US-037 | As a developer, I want error handling for AI failures so that users see friendly error messages | Timeout, retry, fallback handling | P0 |
| US-038 | As a user, I want my data to be private so that I feel safe using the app | No data stored without consent; clear privacy policy | P0 |
| US-039 | As a user, I want the app to work offline so that I can use it without internet | Cached results; offline mode | P2 |
| US-040 | As a user, I want to see the app in Bahasa Indonesia so that I understand everything | Full Bahasa Indonesia UI | P0 |
| US-041 | As a user, I want accessibility support so that I can use the app with screen reader | TalkBack/ screen reader support; proper contrast | P1 |

## Implementation

- Prioritize P0 stories for MVP
- P1 stories for first post-launch iteration
- P2 and Future for roadmap planning
- Each story estimated in story points (1, 2, 3, 5, 8)
- Stories validated with acceptance criteria during QA

## Future Improvement

- Convert user feedback into new stories
- Split large stories into smaller ones
- Add performance stories as needed
- Accessibility audit stories
