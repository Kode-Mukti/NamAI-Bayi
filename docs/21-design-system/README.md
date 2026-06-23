# 21 — Design System

## Summary

Unified design system for NamAI Bayi across Android and Web platforms. Defines colors, typography, spacing, components, and interaction patterns for consistent brand experience.

## Reasoning

A single design system ensures brand consistency across platforms, reduces design debt, and speeds up development. Both Android (Material3) and Web (Tailwind) implement the same visual language.

## Reasoning

A single design system ensures brand consistency across platforms, reduces design debt, and speeds up development. Both Android (Material3) and Web (Tailwind) implement the same visual language.

## Recommendation

### Brand DNA

| Attribute | Value |
|-----------|-------|
| Personality | Warm, Wise, Trustworthy, Modern Indonesian |
| Tone of Voice | Friendly expert, reassuring, culturally aware |
| Colors | Earthy warm tones with gold accents |
| Typography | Clean, readable, warm sans-serif |
| Shapes | Soft rounded corners |
| Icons | Minimal line icons with personality |

### Color Palette

```
Primary Warmth
┌──────────────────────────────────────────────┐
│ #C4956A   #D4A574   #E8C49A   #F5E1C8   #FFFBF5 │
│ 700       600       500       200        50    │
└──────────────────────────────────────────────┘

Deep Earth
┌──────────────────────────────────────────────┐
│ #5C4033   #7A5A4A   #9C7A6A   #BFA89A   #E8DDD5 │
│ 900       800       700       500        100   │
└──────────────────────────────────────────────┘

Accent Gold
┌──────────────────────────────────────────────┐
│ #B8860B   #D4A017   #EECF6A   #F5E6A3   #FDF5E6 │
│ 800       600       400       200        50    │
└──────────────────────────────────────────────┘

Supporting Colors
┌──────────────────────────────────────────────┐
│ Success: #4CAF50   Error: #E74C3C            │
│ Info: #3498DB    Warning: #F39C12            │
└──────────────────────────────────────────────┘

Neutral
┌──────────────────────────────────────────────┐
│ #1A1A1A   #333333   #666666   #999999         │
│ #CCCCCC   #E6E6E6   #F5F5F5   #FFFFFF         │
└──────────────────────────────────────────────┘
```

### Typography

| Style | Size | Weight | Line Height | Usage |
|-------|------|--------|-------------|-------|
| H1 | 32sp | Bold | 1.2 | Screen title |
| H2 | 24sp | Semibold | 1.3 | Section header |
| H3 | 20sp | Semibold | 1.3 | Card title |
| Body L | 16sp | Regular | 1.5 | Primary content |
| Body M | 14sp | Regular | 1.5 | Secondary content |
| Body S | 12sp | Regular | 1.4 | Caption, hints |
| Label L | 16sp | Semibold | 1.3 | Button, form label |
| Label M | 14sp | Medium | 1.3 | Small button |
| Label S | 12sp | Medium | 1.3 | Badge, chip |

**Font Family (Web):** `Plus Jakarta Sans` (Google Fonts)
**Font Family (Android):** `Plus Jakarta Sans` (downloadable font)

### Spacing System (8dp grid)

| Token | Value | Usage |
|-------|-------|-------|
| space-xs | 4dp | Icon spacing |
| space-sm | 8dp | Element gap |
| space-md | 12dp | Card gap |
| space-lg | 16dp | Screen padding |
| space-xl | 24dp | Section spacing |
| space-2xl | 32dp | Screen top/bottom |
| space-3xl | 48dp | Hero section |

### Component Library

#### Button

```
┌──────────────────────┐
│  Primary (filled)    │
│  BG: #C4956A         │
│  Text: White         │
│  Height: 48dp        │
│  Radius: 24dp        │
│  Padding: 16dp 32dp  │
└──────────────────────┘

┌──────────────────────┐
│  Secondary (outline) │
│  Border: #C4956A     │
│  Text: #C4956A       │
│  Height: 48dp        │
│  Radius: 24dp        │
└──────────────────────┘

┌──────────────────────┐
│  Ghost (text only)   │
│  Text: #666666       │
│  Height: auto        │
└──────────────────────┘

States: Default → Pressed (#B07D50) → Disabled (#CCCCCC)
```

#### Card

```
┌──────────────────────────────────┐
│  BG: White                       │
│  Radius: 12dp                    │
│  Elevation: 2dp (Android)        │
│  Shadow: 0 2px 8px rgba(0,0,0,0.08) (Web) │
│  Padding: 16dp                   │
│  Border: 1px #F0E8E0 (optional) │
└──────────────────────────────────┘
```

#### Text Input

```
┌──────────────────────────────────┐
│  BG: #F9F6F0                     │
│  Border: 1px #E0D5C8             │
│  Focus Border: #C4956A           │
│  Radius: 10dp                    │
│  Height: 48dp                    │
│  Padding: 12dp 16dp              │
│  Label: 14sp #666666             │
│  Error: #E74C3C border           │
└──────────────────────────────────┘
```

#### Dropdown

```
┌──────────────────────────────────┐
│  Same as text input              │
│  + Chevron icon right-aligned    │
│  Options: white background       │
│  Selected: #C4956A highlight     │
└──────────────────────────────────┘
```

#### Chip / Badge

```
┌──────────────────────┐
│  Chip                │
│  BG: #F5E1C8         │
│  Text: #7A5A4A       │
│  Radius: 16dp        │
│  Height: 28dp        │
│  Padding: 8dp 12dp   │
│  Font: 12sp medium   │
└──────────────────────┘
```

#### Score Bar

```
  Keunikan
  ────────────────────────
  ████████░░░░░░░░░ 85/100
  Sangat Unik
```

### Icon Set

| Icon | Usage |
|------|-------|
| Heart | Save to wishlist |
| Heart-filled | Saved |
| Share | Share action |
| WhatsApp | Share to WA |
| Instagram | Share to IG |
| Trophy | Battle action |
| Star | Premium feature |
| Sparkle | AI suggestion |
| Chevron | Expand/collapse |
| Arrow left | Back navigation |
| Check | Chosen/completed |
| Plus | Add item |

### Dark Mode

| Token | Light | Dark |
|-------|-------|------|
| BG Primary | #FFFBF5 | #1A1512 |
| BG Card | #FFFFFF | #2C2420 |
| Text Primary | #1A1A1A | #F0E8E0 |
| Text Secondary | #666666 | #BFA89A |
| Border | #E0D5C8 | #3D322C |
| Primary | #C4956A | #D4A574 |

### Animation Tokens

| Element | Duration | Easing |
|---------|----------|--------|
| Button press | 100ms | ease-out |
| Card appear | 300ms | ease-out |
| Page transition | 350ms | ease-in-out |
| Heart fill | 200ms | spring |
| Skeleton pulse | 1.5s | linear infinite |
| Loading dots | 1s | ease-in-out infinite |

### Accessibility

| Requirement | Standard |
|-------------|----------|
| Contrast Ratio | AA min (4.5:1) |
| Touch Target | Min 48dp |
| Font Scale | Supports up to 200% |
| Focus Indicator | Visible 2px ring |
| Screen Reader | Content descriptions |
| Motion | Respect prefers-reduced-motion |

## Implementation

- Create Figma component library from this spec
- Implement web design tokens in Tailwind config
- Implement Android theme with Material3
- Build component showcase page / activity

## Future Improvement

- Motion design system (Lottie animations)
- Illustration style guide
- Accessibility audit tools
- Component usage analytics
- Theming API for white-label future
