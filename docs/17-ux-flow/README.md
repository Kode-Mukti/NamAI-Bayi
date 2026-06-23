# 17 — UX Flow

## Summary

Complete user experience flow from first visit to advocacy. Three screens for MVP with focus on emotional design and minimal friction.

## Reasoning

Baby name selection is an emotional and stressful process. The UX must feel calming, supportive, and intelligent — like talking to a warm, knowledgeable consultant. Every interaction should reduce anxiety and increase confidence.

## Recommendation

### MVP Screen Flow

```
                    ┌─────────────┐
                    │   HOME      │
                    │             │
                    │ • Logo      │
                    │ • Tagline   │
                    │ • Desc      │
                    │ • [Mulai]   │
                    └──────┬──────┘
                           │ tap
                           ▼
                    ┌─────────────┐
                    │   INPUT     │
                    │             │
                    │ • Progress  │
                    │ • Fields    │
                    │ • [Cari]    │
                    └──────┬──────┘
                           │ submit
                           ▼
                    ┌─────────────┐
                    │   LOADING   │
                    │             │
                    │ • Animation │
                    │ • Estimated │
                    │   time      │
                    └──────┬──────┘
                           │ done
                           ▼
                    ┌─────────────┐
                    │   RESULT    │
                    │             │
                    │ • Strategy  │
                    │ • 5 Cards   │
                    │ • Save ♥    │
                    │ • Share     │
                    │ • [Ulangi]  │
                    └──────┬──────┘
                           │ tap card
                           ▼
                    ┌─────────────┐
                    │   DETAIL    │
                    │  (overlay)  │
                    │             │
                    │ • Full info │
                    │ • Save ♥    │
                    │ • Share     │
                    │ • Back      │
                    └─────────────┘
```

### Home Screen UX

```
┌─────────────────────────┐
│                         │
│      [KODE MUKTI]       │  ← Logo (small, top)
│                         │
│                         │
│    ┌───────────────┐    │
│    │  (Icon/Ilust) │    │  ← Beautiful illustration of
│    │               │    │     family/baby
│    └───────────────┘    │
│                         │
│   NamAI Bayi            │  ← Product name (large, warm font)
│                         │
│  Konsultan Nama Bayi    │
│  AI untuk Orang Tua     │  ← Tagline
│  Indonesia              │
│                         │
│  Temukan nama yang      │
│  sempurna untuk buah    │  ← Description (2-3 lines)
│  hati Anda dengan       │
│  kecerdasan AI          │
│                         │
│  ┌─────────────────┐    │
│  │ Mulai Konsultasi │    │  ← CTA button (large, prominent)
│  └─────────────────┘    │
│                         │
│  by Kode Mukti          │  ← Footer brand
│                         │
└─────────────────────────┘
```

### Input Screen UX

```
┌─────────────────────────┐
│ ← Kembali       Langkah │  ← Top bar with back + progress
│                 1 dari 3│
│                         │
│ Ceritakan Tentang       │  ← Warm header
│ Buah Hati Anda          │
│                         │
│ ┌─────────────────────┐ │
│ │ Nama Ayah (opsional)│ │  ← All fields optional
│ │                     │ │
│ └─────────────────────┘ │
│                         │
│ ┌─────────────────────┐ │
│ │ Nama Ibu (opsional)  │ │
│ │                     │ │
│ └─────────────────────┘ │
│                         │
│ ○ Laki-laki  ● Perempuan│  ← Gender selection
│                         │
│ ┌─────────────────────┐ │
│ │ Agama               ▼│ │  ← Dropdown
│ └─────────────────────┘ │
│                         │
│ ┌─────────────────────┐ │
│ │ Budaya              ▼│ │
│ └─────────────────────┘ │
│                         │
│ ┌─────────────────────┐ │
│ │ Provinsi            ▼│ │
│ └─────────────────────┘ │
│                         │
│ ┌─────────────────────┐ │
│ │ Makna yang          │ │
│ │ Diinginkan          │ │
│ │ (contoh: kuat,      │ │
│ │  bijaksana, cantik) │ │
│ └─────────────────────┘ │
│                         │
│ ┌─────────────────────┐ │
│ │ Kepribadian         │ │
│ │ (contoh: pemberani, │ │
│ │  penyayang)          │ │
│ └─────────────────────┘ │
│                         │
│ [Lebih Banyak Opsi ▾]  │  ← Collapsible advanced options
│                         │
│  ┌───────────────────┐  │
│  │  Cari Nama        │  │  ← Submit button
│  └───────────────────┘  │
│                         │
└─────────────────────────┘
```

### Loading Screen UX

```
┌─────────────────────────┐
│                         │
│                         │
│       ┌─────────┐       │
│       │  ☁️ 🌟   │       │  ← Animated illustration
│       │  ✨ 💫   │       │     (stars, clouds, magical)
│       └─────────┘       │
│                         │
│   AI Sedang Merangkai   │  ← Animated text
│   Nama Terbaik...       │     (changes every 2 seconds)
│                         │
│   Menganalisis          │  ← Status messages
│   preferensi...         │
│   ████████░░░░ 65%      │  ← Progress bar (estimated)
│                         │
│   Hanya beberapa detik  │
│   lagi                  │
│                         │
└─────────────────────────┘

Status messages (cycling):
- "Menganalisis preferensi..."
- "Memilih strategi penamaan..."
- "Merangkai kandidat nama..."
- "Mengevaluasi kualitas..."
- "Memilih rekomendasi terbaik..."
```

### Result Screen UX

```
┌─────────────────────────┐
│ ← Kembali         Simpan│  ← Top bar
│                         │
│ Rekomendasi Nama        │  ← Header
│                         │
│ Strategi: Islami Modern │  ← Strategy badge
│                         │
│ ┌─────────────────────┐ │
│ │ 1. Arumi            │ │  ← Name card (expandable)
│ │ Mutiara laut yang   │ │
│ │ indah               │ │
│ │                     │ │
│ │ ♥  Skor: 92/100     │ │
│ └─────────────────────┘ │
│                         │
│ ┌─────────────────────┐ │
│ │ 2. Pramudya         │ │
│ │ Kesatria yang       │ │
│ │ bijaksana           │ │
│ │                     │ │
│ │ ♥  Skor: 88/100     │ │
│ └─────────────────────┘ │
│                         │
│ ┌─────────────────────┐ │
│ │ 3. ...              │ │
│ └─────────────────────┘ │
│                         │
│ ┌─────────────────────┐ │
│ │ 4. ...              │ │
│ └─────────────────────┘ │
│                         │
│ ┌─────────────────────┐ │
│ │ 5. ...              │ │
│ └─────────────────────┘ │
│                         │
│ ┌─────────────────────┐ │
│ │  Konsultasi Lagi    │ │
│ └─────────────────────┘ │
│                         │
└─────────────────────────┘
```

### Name Detail (Overlay/Sheet)

```
┌─────────────────────────┐
│                         │
│ ✕                    ♥  │  ← Close + Save
│                         │
│        ARUMI            │  ← Name (large, beautiful font)
│                         │
│   Mutiara Laut yang     │  ← Meaning
│   Indah                 │
│                         │
│ ──── Informasi ────     │
│                         │
│ Filosofi                │
│ Nama ini melambangkan   │
│ keindahan dan ketenangan│
│ seperti mutiara di ...  │
│                         │
│ Pengucapan: A-roo-mee   │
│ Panggilan: Arum, Rumi   │
│ Asal: Sansekerta        │
│                         │
│ Relevansi Budaya        │
│ Cocok untuk keluarga    │
│ Jawa-Muslim modern ...  │
│                         │
│ Skor Keunikan: 85/100   │
│ (Sangat Unik)           │
│                         │
│ Estimasi Popularitas:   │
│ Jarang                  │
│                         │
│ Keterbacaan             │
│ Internasional: Mudah    │
│                         │
│ Nama Lengkap:           │
│ Arumi Pramudya Putri    │
│                         │
│ Kompatibilitas Saudara: │
│ Cocok dengan Arjuno,    │
│ Dewani                  │
│                         │
│ ──── ────               │
│                         │
│ Mengapa AI             │
│ Merekomendasikan:       │
│ Berdasarkan preferensi  │
│ Anda ...                │
│                         │
│ ┌─────┐ ┌─────┐        │
│ │ WA  │ │ IG  │        │  ← Share buttons
│ └─────┘ └─────┘        │
│                         │
└─────────────────────────┘
```

### Interaction Patterns

| Action | Animation | Haptic (Android) | Sound |
|--------|-----------|------------------|-------|
| Tap CTA | Button scale 0.95 → 1.0 | Light click | None |
| Submit form | Progress wave | Medium thud | None |
| Loading dots | Pulsing | None | None |
| Cards appear | Staggered slide up | Light per card | None |
| Save name | Heart fills, scale bounce | Light click | None |
| Error | Shake animation | Error buzz | None |
| Share | Sheet slides up | Medium click | None |

### Microcopy Principles

- Warm, personal tone (like a friend who is an expert)
- Use "Anda" not "Kamu" (formal, respectful)
- Positive framing only ("Temukan" not "Cari...")
- Encouraging messages ("Pilihan yang bagus!")
- Celebration when saving ("Berhasil disimpan! 🌟" — but no emoji in MVP)
- Clear error messages with solutions

## Implementation

- Build Home → Input → Result flow first
- Implement progressively enhanced loading
- Add micro-interactions last (Polish sprint)
- Test with real users for emotional response

## Future Improvement

- Add onboarding walkthrough for first-time users
- Personalized home screen based on history
- Couple Mode split-screen flow
- Dark mode support
- RTL support for Arabic names context
