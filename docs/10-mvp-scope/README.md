# 10 — MVP Scope

## Summary

MVP (Minimum Viable Product) consists of 20 essential features across 3 screens: Home, Input, and AI Result. The MVP is designed for 4-6 weeks of development, targeting 10,000 MAU at launch.

## Reasoning

The MVP must validate three critical hypotheses:
1. Parents want AI-powered name consultation (not just a generator)
2. Users will engage with explainable AI recommendations
3. Viral sharing drives organic growth

Everything else is deferred.

## Recommendation

### MVP Screens

#### Screen 1: Home
- Kode Mukti logo and branding
- Tagline: "Konsultan Nama Bayi AI untuk Orang Tua Indonesia"
- Brief description (2-3 sentences)
- CTA button: "Mulai Konsultasi"
- Footer: "by Kode Mukti"

#### Screen 2: Input
- All fields optional
- Clear progress indicator
- Field descriptions/hints
- Submit button: "Cari Nama"
- Fields:
  - Nama Ayah (text)
  - Nama Ibu (text)
  - Jenis Kelamin (radio: Laki-laki / Perempuan)
  - Agama (dropdown: Islam, Kristen, Katolik, Hindu, Buddha, Konghucu, Lainnya)
  - Budaya (dropdown: Jawa, Sunda, Bali, Batak, Minang, Betawi, Melayu, Bugis, Tionghoa, Lainnya)
  - Provinsi (dropdown)
  - Makna (text, descriptive)
  - Kepribadian yang Diinginkan (text/multi-select)

#### Screen 3: AI Result
- Loading animation with progress
- Strategy badge (e.g., "Strategi: Islami Modern")
- 5 name cards with:
  - Name (large text)
  - Meaning (1 line)
  - Tap to expand full detail
- Full detail (expandable):
  - Makna (meaning)
  - Filosofi (philosophy)
  - Pengucapan (pronunciation)
  - Nama Panggilan (nickname)
  - Asal (origin)
  - Relevansi Budaya (cultural relevance)
  - Skor Keunikan (uniqueness score 1-100)
  - Estimasi Popularitas (popularity estimate)
  - Keterbacaan Internasional (international readability)
  - Nama Lengkap (full name suggestion)
  - Kompatibilitas Saudara (sibling compatibility)
  - Mengapa AI merekomendasikan (why AI recommends)
- Save to wishlist (heart icon)
- Share to WhatsApp
- Share to Instagram Story
- "Konsultasi Lagi" button

### MVP Technical Scope

| Component | Technology | Notes |
|-----------|------------|-------|
| Android | Kotlin + Jetpack Compose | Single activity, navigation component |
| Web | Next.js (React) | Responsive, PWA support |
| AI Provider | Gemini API | Default provider, configurable |
| State Management | ViewModel (Android), Zustand (Web) | — |
| Storage | Room (Android), LocalStorage (Web) | For wishlist |
| Analytics | Firebase + Amplitude | Core events only |
| Deployment | Play Store (Android), Vercel (Web) | — |

### What is NOT in MVP

| Feature | Reason |
|---------|--------|
| Registration/Login | Friction for first-time users |
| Premium | Validate value first |
| Couple Mode | Complex, needed by fewer users |
| Community | Moderation overhead |
| AI Chat | High AI cost, complex UX |
| Daily Name | Push notification setup |
| QR Code | Low value for MVP |
| Public Link | Requires backend |
| Name Battle | P1 feature |
| Voting | P1 feature |
| Offline Mode | P2 feature |

### Success Criteria for MVP

| Metric | Target |
|--------|--------|
| MAU | 10,000 by month 3 |
| Session Duration | 3+ minutes |
| Share Rate | 30% of sessions |
| Rating | 4.5+ on Play Store |
| AI Response Time | < 8 seconds |
| Input Completion Rate | 60%+ |

## Implementation

- Sprint 1-2: Home + Input (Android + Web)
- Sprint 3-4: AI Integration + Result Screen
- Sprint 5-6: Polish, share features, testing
- Sprint 7: Beta launch, bug fixes
- Sprint 8: Public launch

## Future Improvement

- Analyze MVP user behavior to prioritize P1 features
- A/B test input form length (more vs fewer fields)
- Monitor AI cost per consultation
- User interviews after MVP launch
