# 18 — Wireframe

## Summary

Low-fidelity wireframes for all MVP screens. Defines layout structure, content hierarchy, and component placement. Serves as the blueprint for UI development.

## Reasoning

Wireframes separate layout from visual design. The team can validate structure and flow before investing in high-fidelity design. Every screen is documented with its purpose and key interactions.

## Recommendation

### Screen 1: Home — Mobile (Android/Web Mobile)

```
┌─────────────────────────────┐
│                             │
│  [KODE MUKTI logo]          │  ← 40dp height, top padding 16dp
│                             │
│                             │
│        ┌───────┐            │
│        │  🧸   │            │  ← Illustration, 200x200dp
│        └───────┘            │     centered
│                             │
│                             │
│  NamAI Bayi                 │  ← H1, 32sp, bold, centered
│                             │
│  Konsultan Nama Bayi        │
│  AI untuk Orang Tua         │  ← H2, 18sp, centered
│  Indonesia                  │
│                             │
│  Temukan nama yang sempurna │
│  untuk buah hati Anda       │  ← Body, 14sp, centered
│  dengan kecerdasan AI.      │     max 3 lines
│                             │
│                             │
│  ┌─────────────────────┐    │
│  │  Mulai Konsultasi   │    │  ← CTA, 48dp height
│  └─────────────────────┘    │     cornerRadius 24dp
│                             │     primary color
│                             │
│                             │
│  by Kode Mukti              │  ← Footer, 12sp, center
│                             │
└─────────────────────────────┘
```

### Screen 2: Input — Mobile

```
┌─────────────────────────────┐
│ ← Kembali          Ln 1/3   │  ← TopAppBar, back arrow
│                             │     + progress step
│                             │
│ Ceritakan Tentang           │  ← Header, 20sp
│ Buah Hati Anda              │
│                             │
│ ┌─────────────────────────┐ │
│ │ Nama Ayah (opsional)    │ │  ← TextField, 48dp height
│ │                         │ │     full width
│ └─────────────────────────┘ │
│                             │
│ ┌─────────────────────────┐ │
│ │ Nama Ibu (opsional)     │ │
│ │                         │ │
│ └─────────────────────────┘ │
│                             │
│ Jenis Kelamin               │  ← Label, 14sp
│ ○ Laki-laki  ● Perempuan    │  ← RadioGroup, horizontal
│                             │
│ ┌─────────────────────────┐ │
│ │ Agama            ▼ Islam│ │  ← Dropdown, 48dp
│ └─────────────────────────┘ │
│                             │
│ ┌─────────────────────────┐ │
│ │ Budaya           ▼ Jawa │ │
│ └─────────────────────────┘ │
│                             │
│ ┌─────────────────────────┐ │
│ │ Provinsi     ▼ Jabar    │ │
│ └─────────────────────────┘ │
│                             │
│ ┌─────────────────────────┐ │
│ │ Makna yang Diinginkan   │ │  ← TextField, multiline
│ │ Contoh: kuat,           │ │     hint text
│ │ bijaksana, cantik       │ │
│ └─────────────────────────┘ │
│                             │
│ ┌─────────────────────────┐ │
│ │ Kepribadian             │ │
│ │ Contoh: pemberani,      │ │
│ │ penyayang               │ │
│ └─────────────────────────┘ │
│                             │
│ ┌─ Lebih Banyak Opsi ────┐ │  ← Expandable section
│ │ Huruf Awal     [A B C] │ │
│ │ Huruf Akhir    [X Y Z] │ │
│ │ Tingkat Keunikan ──●── │ │
│ │ Tingkat Tradisional─●─ │ │
│ │ Nama Favorit    [+ ...]│ │
│ │ Nama Disukai    [+ ...]│ │
│ └─────────────────────────┘ │
│                             │
│  ┌─────────────────────┐    │
│  │  Cari Nama          │    │  ← Submit button
│  └─────────────────────┘    │
│                             │
└─────────────────────────────┘
```

### Screen 3: Loading — Mobile

```
┌─────────────────────────────┐
│                             │
│                             │
│                             │
│                             │
│        ☁️  ✨               │  ← Loading illustration
│        💫  🌟               │     centered
│                             │
│                             │
│   AI Sedang Merangkai       │  ← Status text, 18sp
│   Nama Terbaik...           │     animate with ellipsis
│                             │
│                             │
│   ████████░░░░░░░░ 40%      │  ← Progress bar, 8dp height
│                             │     80% width, centered
│                             │
│   Menganalisis preferensi   │  ← Status message, 14sp
│                             │     changes every 3s
│                             │
│   Hanya beberapa detik      │
│   lagi                      │
│                             │
│                             │
└─────────────────────────────┘
```

### Screen 4: Result — Mobile

```
┌─────────────────────────────┐
│ ← Kembali           ♥ 3    │  ← TopAppBar + wishlist count
│                             │
│ Rekomendasi Nama            │  ← Header, 20sp
│                             │
│ ┌─────────────────────────┐ │
│ │ Strategi: Islami Modern │ │  ← Chip, 12sp
│ └─────────────────────────┘ │
│                             │
│  ┌───────────────────────┐  │
│  │ 1                    │  │  ← Card, elevation 2dp
│  │                      │  │     cornerRadius 12dp
│  │ ARUMI                │  │  ← Name, 24sp, bold
│  │                      │  │
│  │ Mutiara laut yang    │  │  ← Meaning, 14sp
│  │ indah                │  │
│  │                      │  │
│  │ ♥ 92/100  ▸          │  │  ← Heart + score + expand
│  └───────────────────────┘  │
│                             │
│  ┌───────────────────────┐  │
│  │ 2                    │  │
│  │ PRAMUDYA             │  │
│  │ Kesatria yang        │  │
│  │ bijaksana            │  │
│  │ ♥ 88/100  ▸          │  │
│  └───────────────────────┘  │
│                             │
│  ┌───────────────────────┐  │
│  │ 3  /  4  /  5         │  │  ← Same pattern
│  └───────────────────────┘  │
│                             │
│  ┌─────────────────────┐    │
│  │  Konsultasi Lagi    │    │  ← Secondary CTA
│  └─────────────────────┘    │
│                             │
└─────────────────────────────┘
```

### Screen 5: Name Detail (Bottom Sheet) — Mobile

```
┌─────────────────────────────┐
│                             │
│ ┌─────────────────────────┐ │
│ │ ✕                   ♥  │ │  ← Close + Save
│ └─────────────────────────┘ │
│                             │
│         ARUMI               │  ← H1, 32sp, centered
│                             │
│    Mutiara Laut yang Indah  │  ← Meaning, 16sp, center
│                             │
│    ── Informasi ──          │  ← Section header, 14sp
│                             │
│    Filosofi                 │  ← Label, 12sp, semibold
│    Nama ini melambangkan    │  ← Body, 14sp
│    keindahan dan            │
│    ketenangan...            │
│                             │
│    Pengucapan: A-roo-mee    │  ← Key-value pair
│    Panggilan: Arum, Rumi    │
│    Asal: Sansekerta         │
│                             │
│    Skor Keunikan            │
│    ████████░░ 85/100        │  ← Progress bar
│    Sangat Unik              │
│                             │
│    Estimasi Popularitas     │
│    Jarang                   │
│                             │
│    Keterbacaan Internas.    │
│    Mudah                    │
│                             │
│    Nama Lengkap             │
│    Arumi Pramudya Putri     │
│                             │
│    ── ──                    │
│                             │
│    ┌─────┐ ┌─────┐         │
│    │ WA  │ │ IG  │         │  ← Share buttons, 48dp
│    └─────┘ └─────┘         │
│                             │
└─────────────────────────────┘
```

### Screen 6: Wishlist — Mobile

```
┌─────────────────────────────┐
│ ← Kembali        Wishlist  │  ← TopAppBar
│                             │
│  ┌───────────────────────┐  │
│  │ ARUMI                 │  │  ← Saved name card
│  │ Mutiara laut yang     │  │     swipe to remove
│  │ indah                 │  │
│  │                [Pilih]│  │  ← "Chosen" action
│  └───────────────────────┘  │
│                             │
│  ┌───────────────────────┐  │
│  │ ZAHRA                 │  │
│  │ Bunga yang mekar      │  │
│  │ indah                 │  │
│  │                [Pilih]│  │
│  └───────────────────────┘  │
│                             │
│  ┌───────────────────────┐  │
│  │ PRAMUDYA ᎒            │  │  ← ᎒ = chosen badge
│  │ Kesatria yang         │  │
│  │ bijaksana             │  │
│  │             ✓ Terpilih│  │  ← Confirmed chosen
│  └───────────────────────┘  │
│                             │
│                     [+ Baru]│  ← FAB to new consult
│                             │
└─────────────────────────────┘
```

### Layout Grid (Web — Desktop)

```
┌─────────────────────────────────────────────────┐
│  Header (logo + nav)          [by Kode Mukti]    │
├─────────────────────────────────────────────────┤
│                                                   │
│          ┌─────────────────────────┐              │
│          │     Hero Section        │              │  ← 50% width
│          │  (centered, max 600px)  │              │     centered
│          └─────────────────────────┘              │
│                                                   │
├─────────────────────────────────────────────────┤
│  ┌──────────┐  ┌──────────┐  ┌──────────┐        │
│  │  Feature │  │  Feature │  │  Feature │        │  ← 3 columns
│  │    1     │  │    2     │  │    3     │        │     on desktop
│  └──────────┘  └──────────┘  └──────────┘        │
│                                                   │
├─────────────────────────────────────────────────┤
│  Footer                                           │
└─────────────────────────────────────────────────┘
```

### Responsive Breakpoints

| Breakpoint | Width | Layout |
|------------|-------|--------|
| Mobile | < 640px | Single column, full-width |
| Tablet | 640-1024px | Two columns for form |
| Desktop | > 1024px | Centered max-width 800px |

## Implementation

- Build mobile-first layouts
- Use 8dp grid system
- Maintain consistent spacing (16dp padding, 12dp card gap)
- Ensure all touch targets are minimum 48dp
- Test on 5 device sizes minimum

## Future Improvement

- Tablet-optimized layouts (side-by-side name comparison)
- Landscape mode support
- Split-screen for Couple Mode
- Animations and micro-interactions added to wireframe
