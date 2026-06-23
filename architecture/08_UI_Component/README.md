# 08 — UI Component

## Summary

Complete inventory of reusable UI components. Owned by the UI Agent. Every other feature agent imports from `core/ui/component/`.

## Reasoning

Without a shared component library, each screen agent builds their own buttons and cards. This creates visual inconsistency and duplicate work. The UI Agent pre-builds all components; screen agents only compose them.

## Recommendation

---

### Component Inventory

#### Buttons

| Component | Variants | Props | Used In |
|-----------|----------|-------|---------|
| `PrimaryButton` | Default, Loading, Disabled | `text, onClick, isLoading, modifier` | All screens |
| `SecondaryButton` | Default, Disabled | `text, onClick, modifier` | Result, Settings |
| `GhostButton` | Default | `text, onClick, modifier` | Onboarding |
| `IconButton` | — | `icon, onClick, contentDescription, modifier` | TopBar, cards |
| `TextButton` | — | `text, onClick, modifier` | Settings |

#### Cards & Containers

| Component | Variants | Props | Used In |
|-----------|----------|-------|---------|
| `NameCard` | Default, Selected, Saved | `name, meaning, score, isSaved, onTap, onSave` | Result, History |
| `ResultCard` | Default | `name, meaning, philosophy, origin, scores, onTap, onShare, onSave` | Result detail |
| `HistoryCard` | Default | `topName, strategy, date, nameCount, onTap, onDelete` | History |
| `FavoriteCard` | Default | `name, meaning, origin, score, onTap, onRemove` | Favorite |
| `StatCard` | Default | `label, value, icon` | Home (future) |

#### Input Components

| Component | Variants | Props | Used In |
|-----------|----------|-------|---------|
| `AppTextField` | Default, Error, Filled | `value, onValueChange, label, error, hint, maxLength` | Generate |
| `AppDropdown` | Default, Error | `selectedValue, options, onSelected, label, error` | Generate |
| `GenderSelector` | — | `selected, onSelected` | Generate |
| `LetterPicker` | — | `value, onValueChange` | Generate (advanced) |
| `LevelSlider` | — | `value, onValueChange, min, max, label` | Generate (advanced) |

#### Feedback Components

| Component | Variants | Props | Used In |
|-----------|----------|-------|---------|
| `LoadingAnimation` | Default | `statusMessage, progress` | Loading |
| `ProgressIndicator` | Determinate, Indeterminate | `progress, modifier` | Loading |
| `ShimmerLoader` | Card, List | `modifier` | History, Favorite |
| `EmptyState` | — | `icon, title, description, actionText, onAction` | History, Favorite |
| `ErrorState` | Retry, Dismiss | `message, onRetry, onDismiss` | Result, Loading |
| `SnackbarHost` | Success, Error | `message, type` | All |

#### Display Components

| Component | Variants | Props | Used In |
|-----------|----------|-------|---------|
| `ScoreBar` | — | `score, maxScore, label` | Result detail |
| `StrategyBadge` | Per strategy | `strategy: NamingStrategy` | Result |
| `Chip` | Filled, Outlined | `text, onTap, isSelected` | Generate (tags) |
| `SectionHeader` | — | `title, actionText, onAction` | All list screens |
| `DetailRow` | — | `label, value` | Result detail |
| `InfoTooltip` | — | `title, description` | Generate |

#### Layout Components

| Component | Variants | Props | Used In |
|-----------|----------|-------|---------|
| `TopBar` | WithBack, WithTitle, WithActions | `title, onBack, actions` | All |
| `BottomNavigationBar` | — | `selectedTab, onTabSelected` | Home/History/Fav/Settings |
| `OnboardingPage` | — | `image, title, description, isLastPage` | Onboarding |
| `AppScaffold` | WithBottomNav, Without | `content` | All |
| `ExpandableSection` | — | `title, isExpanded, onToggle, content` | Generate (advanced) |

---

### Component Specifications

#### PrimaryButton

```
┌──────────────────────────────────────┐
│            PrimaryButton             │
├──────────────────────────────────────┤
│ Default:                             │
│   Background: Primary (#C4956A)      │
│   Text: White                        │
│   Height: 52dp                       │
│   Corner: 26dp (fully rounded)       │
│   Padding: H 32dp                    │
│   Font: LabelLarge, Semibold         │
│                                      │
│ Loading:                             │
│   CircularProgressIndicator center   │
│   Text replaced by spinner           │
│                                      │
│ Disabled:                            │
│   Background: SurfaceVariant         │
│   Text: OnSurfaceVariant            │
│   Not clickable                      │
└──────────────────────────────────────┘
```

#### NameCard

```
┌──────────────────────────────────────┐
│            NameCard                  │
├──────────────────────────────────────┤
│ ┌────┬─────────────────────────┬──┐  │
│ │ #1 │ ARUMI                   │ ♥│  │
│ │    │ Mutiara laut yang indah │  │  │
│ │    │ Skor: 92/100            │  │  │
│ └────┴─────────────────────────┴──┘  │
│                                      │
│ Tap → ResultCard (expanded)          │
│ Heart → Toggle favorite              │
└──────────────────────────────────────┘

Dimensions:
  Height: 80dp (collapsed)
  Corner: 12dp
  Elevation: 2dp
  Background: Surface
  Tap target: 48dp min
```

#### ScoreBar

```
  Keunikan
  ████████░░░░░░░ 85/100
  ╰──────────────╯
  Max width: 200dp
  Height: 8dp
  Track: SurfaceVariant
  Progress: Primary (0-33), Warning (34-66), Success (67-100)
  Corner: 4dp
```

#### LoadingAnimation

```
┌──────────────────────────────────────┐
│         LoadingAnimation             │
├──────────────────────────────────────┤
│                                      │
│          ┌──────────────┐            │
│          │  ✨  🧸  ✨   │            │
│          │  Animating   │            │
│          └──────────────┘            │
│                                      │
│  AI Sedang Merangkai                 │
│  Nama Terbaik...                     │
│                                      │
│  ████████░░░░░░░░ 40%                │
│                                      │
│  Menganalisis preferensi...          │
│                                      │
└──────────────────────────────────────┘

Animation: Lottie or Canvas-based
Duration: 15s max before timeout
```

---

### State Visualization Matrix

```
┌────────────┬──────────┬──────────┬──────────┬──────────┐
│ Component  │ Loading  │ Empty    │ Error    │ Success  │
├────────────┼──────────┼──────────┼──────────┼──────────┤
│ NameCard   │ Shimmer  │ N/A      │ N/A      │ Content  │
│ History    │ Shimmer  │ EmptySt. │ ErrorSt. │ List     │
│ Favorite   │ Shimmer  │ EmptySt. │ ErrorSt. │ Grid     │
│ Result     │ Shimmer  │ N/A      │ ErrorSt. │ Cards    │
│ Generate   │ disabled │ N/A      │ FieldErr │ Enabled  │
│ Loading    │ spinner  │ N/A      │ Retry    │ Navigate │
└────────────┴──────────┴──────────┴──────────┴──────────┘
```

---

### Component Agent Handoff

| Agent | Builds | Delivers To |
|-------|--------|-------------|
| UI Agent | All 26 components in `core/ui/component/` | All screen agents |
| UI Agent | Theme files in `core/ui/theme/` | All agents |
| Screen agents | Individual screens using components | Navigation Agent |

### Component Creation Order

```
Sprint 1:
  1. PrimaryButton, SecondaryButton, GhostButton, IconButton
  2. TopBar, BottomNavigationBar, AppScaffold
  3. AppTextField, AppDropdown

Sprint 2:
  4. NameCard, ScoreBar, StrategyBadge, Chip
  5. GenderSelector, LetterPicker, LevelSlider
  6. ExpandableSection, SectionHeader, DetailRow

Sprint 3:
  7. LoadingAnimation, ProgressIndicator
  8. ErrorState, EmptyState
  9. ResultCard, HistoryCard, FavoriteCard

Sprint 4:
  10. ShimmerLoader, SnackbarHost
  11. OnboardingPage, InfoTooltip
  12. Polish and accessibility pass
```
