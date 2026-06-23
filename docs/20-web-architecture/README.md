# 20 — Web Architecture

## Summary

Web application built with Next.js 14 (App Router), TypeScript, Tailwind CSS, and Zustand. Serverless-first with API routes proxying AI calls. PWA support for mobile web users.

## Reasoning

Web architecture must support: (1) SEO for organic discovery, (2) PWA for app-like experience on mobile browsers, (3) serverless AI API calls (no backend server needed for MVP), (4) fast page loads for good Core Web Vitals.

## Recommendation

### Architecture Layers

```
┌─────────────────────────────────────────┐
│         Next.js App Router              │
│                                          │
│  ┌───────────────────────────────────┐  │
│  │         Pages (SSR/SSG)           │  │
│  │  /           → Home (SSG)         │  │
│  │  /konsultasi → Input (client)     │  │
│  │  /hasil      → Result (client)    │  │
│  │  /favorit    → Wishlist (client)   │  │
│  │  /nama/[id]  → Public Link (SSG)  │  │
│  └───────────────┬───────────────────┘  │
│                  │                       │
│  ┌───────────────┴───────────────────┐  │
│  │        Client Components          │  │
│  │  - HomeHero (Server)              │  │
│  │  - InputForm (Client)             │  │
│  │  - NameCard (Client)              │  │
│  │  - NameDetailSheet (Client)       │  │
│  │  - WishlistGrid (Client)          │  │
│  │  - LoadingSpinner (Client)        │  │
│  │  - ShareButtons (Client)          │  │
│  └───────────────┬───────────────────┘  │
│                  │                       │
│  ┌───────────────┴───────────────────┐  │
│  │        State Management           │  │
│  │  Zustand stores:                  │  │
│  │  - usePreferencesStore            │  │
│  │  - useResultStore                 │  │
│  │  - useWishlistStore               │  │
│  │  - useUIStore                     │  │
│  └───────────────┬───────────────────┘  │
└──────────────────┼──────────────────────┘
                   │
┌──────────────────┼──────────────────────┐
│         API Layer                       │
│  ┌───────────────┴───────────────────┐  │
│  │  Next.js API Routes               │  │
│  │  /api/generate → POST             │  │
│  │    → calls AI provider            │  │
│  │  /api/health  → GET               │  │
│  │  /api/share/[id] → GET (public)   │  │
│  └───────────────┬───────────────────┘  │
└──────────────────┼──────────────────────┘
                   │
┌──────────────────┼──────────────────────┐
│      AI Provider Layer (Server-side)    │
│  ┌───────────────┴───────────────────┐  │
│  │  providers/                       │  │
│  │  - interface.ts (AIProvider)      │  │
│  │  - gemini.ts                      │  │
│  │  - openai.ts                      │  │
│  │  - config.ts                      │  │
│  │                                   │  │
│  │  prompts/                         │  │
│  │  - system.ts                      │  │
│  │  - user.ts                        │  │
│  │  - templates.ts                   │  │
│  └───────────────────────────────────┘  │
└─────────────────────────────────────────┘
```

### Project Structure

```
web/
├── src/
│   ├── app/
│   │   ├── layout.tsx                # Root layout + metadata
│   │   ├── page.tsx                  # Home page (SSG)
│   │   ├── konsultasi/
│   │   │   └── page.tsx              # Input page (Client)
│   │   ├── hasil/
│   │   │   └── page.tsx              # Result page (Client)
│   │   ├── favorit/
│   │   │   └── page.tsx              # Wishlist (Client)
│   │   ├── nama/
│   │   │   └── [id]/
│   │   │       └── page.tsx          # Public name link
│   │   └── api/
│   │       ├── generate/
│   │       │   └── route.ts          # AI generation API
│   │       ├── share/
│   │       │   └── [id]/
│   │       │       └── route.ts      # Public share API
│   │       └── health/
│   │           └── route.ts          # Health check
│   │
│   ├── components/
│   │   ├── ui/
│   │   │   ├── Button.tsx
│   │   │   ├── Card.tsx
│   │   │   ├── Input.tsx
│   │   │   ├── Select.tsx
│   │   │   ├── Slider.tsx
│   │   │   ├── Progress.tsx
│   │   │   ├── Badge.tsx
│   │   │   ├── Sheet.tsx
│   │   │   └── Skeleton.tsx
│   │   ├── home/
│   │   │   ├── HomeHero.tsx
│   │   │   └── HomeFeature.tsx
│   │   ├── input/
│   │   │   ├── InputForm.tsx
│   │   │   ├── BasicFields.tsx
│   │   │   ├── AdvancedFields.tsx
│   │   │   └── ProgressStepper.tsx
│   │   ├── result/
│   │   │   ├── NameCard.tsx
│   │   │   ├── NameDetailSheet.tsx
│   │   │   ├── StrategyBadge.tsx
│   │   │   ├── LoadingState.tsx
│   │   │   └── ScoreBar.tsx
│   │   ├── wishlist/
│   │   │   ├── WishlistGrid.tsx
│   │   │   └── WishlistItem.tsx
│   │   └── share/
│   │       ├── ShareToWhatsApp.tsx
│   │       ├── ShareToInstagram.tsx
│   │       └── ShareLink.tsx
│   │
│   ├── stores/
│   │   ├── preferences-store.ts      # Input preferences
│   │   ├── result-store.ts           # AI result state
│   │   ├── wishlist-store.ts         # Wishlist (persisted)
│   │   └── ui-store.ts               # Loading/error state
│   │
│   ├── lib/
│   │   ├── ai/
│   │   │   ├── interface.ts          # AIProvider interface
│   │   │   ├── gemini.ts             # Gemini implementation
│   │   │   ├── openai.ts             # OpenAI implementation
│   │   │   ├── config.ts             # Provider config
│   │   │   └── strategy.ts           # Strategy selection
│   │   ├── prompts/
│   │   │   ├── system.ts             # System prompts
│   │   │   ├── user.ts               # User prompt builder
│   │   │   └── templates.ts          # Template functions
│   │   ├── types/
│   │   │   ├── ai.ts                 # AI types
│   │   │   ├── user.ts               # User data types
│   │   │   └── api.ts                # API types
│   │   ├── validation/
│   │   │   ├── schema.ts             # Zod schemas
│   │   │   └── response.ts           # Response validation
│   │   ├── share/
│   │   │   ├── whatsapp.ts           # WA share utilities
│   │   │   └── instagram.ts          # IG share utilities
│   │   └── utils/
│   │       ├── cn.ts                 # clsx + tailwind-merge
│   │       └── formatters.ts         # Display formatters
│   │
│   └── styles/
│       └── globals.css               # Tailwind imports + base
│
├── public/
│   ├── manifest.json                 # PWA manifest
│   ├── sw.js                         # Service worker
│   ├── icons/
│   │   ├── icon-192x192.png
│   │   └── icon-512x512.png
│   └── og-image.png                  # Open Graph image
│
├── next.config.ts
├── tailwind.config.ts
├── tsconfig.json
├── package.json
└── .env.local                        # API keys (gitignored)
```

### Key Dependencies

```json
{
  "dependencies": {
    "next": "^14.2.0",
    "react": "^18.3.0",
    "react-dom": "^18.3.0",
    "zustand": "^4.5.0",
    "zustand-persist": "^1.0.0",
    "@google/generative-ai": "^0.12.0",
    "openai": "^4.47.0",
    "zod": "^3.22.0",
    "tailwind-merge": "^2.3.0",
    "clsx": "^2.1.0",
    "lucide-react": "^0.378.0",
    "framer-motion": "^11.0.0"
  },
  "devDependencies": {
    "typescript": "^5.4.0",
    "tailwindcss": "^3.4.0",
    "@tailwindcss/forms": "^0.5.0",
    "autoprefixer": "^10.4.0",
    "postcss": "^8.4.0",
    "eslint": "^8.57.0",
    "eslint-config-next": "^14.2.0",
    "jest": "^29.7.0",
    "@testing-library/react": "^14.2.0",
    "msw": "^2.2.0"
  }
}
```

### API Route: Generate Names

```typescript
// src/app/api/generate/route.ts
import { NextRequest, NextResponse } from 'next/server';
import { getAIProvider } from '@/lib/ai/config';
import { buildPrompt } from '@/lib/prompts/user';
import { selectStrategy } from '@/lib/ai/strategies';
import { validateAIResponse } from '@/lib/validation/response';
import { UserPreferences } from '@/lib/types/user';

export async function POST(request: NextRequest) {
  try {
    const preferences: UserPreferences = await request.json();

    // 1. Select strategy
    const strategy = selectStrategy(preferences);

    // 2. Build prompt with strategy
    const prompt = buildPrompt(preferences, strategy);

    // 3. Get provider and generate
    const provider = getAIProvider();
    const rawResponse = await provider.generate(prompt);

    // 4. Validate response
    const validation = validateAIResponse(rawResponse);
    if (!validation.valid) {
      console.error('Invalid AI response:', validation.errors);
      return NextResponse.json(
        { error: true, code: 'AI_INVALID_RESPONSE', message: 'Respons AI tidak valid. Silakan coba lagi.' },
        { status: 502 }
      );
    }

    // 5. Return validated response
    return NextResponse.json(validation.data);

  } catch (error) {
    console.error('Generation error:', error);
    return NextResponse.json(
      {
        error: true,
        code: 'AI_TIMEOUT',
        message: 'AI sedang sibuk. Silakan coba lagi.',
        retryable: true,
      },
      { status: 503 }
    );
  }
}
```

### AI Provider Interface

```typescript
// src/lib/ai/interface.ts
export interface AIProvider {
  name: string;
  generate(prompt: string): Promise<AIResponse>;
  healthCheck(): Promise<boolean>;
}

export interface AIResponse {
  strategy: string;
  strategyDescription: string;
  recommendations: NameRecommendation[];
}

// src/lib/ai/gemini.ts
export class GeminiProvider implements AIProvider {
  name = 'Gemini';
  private model: GenerativeModel;

  constructor() {
    const genAI = new GoogleGenerativeAI(process.env.GEMINI_API_KEY!);
    this.model = genAI.getGenerativeModel({
      model: 'gemini-1.5-flash',
      generationConfig: {
        temperature: 0.8,
        maxOutputTokens: 4096,
        responseMimeType: 'application/json',
      },
    });
  }

  async generate(prompt: string): Promise<AIResponse> {
    const result = await this.model.generateContent(prompt);
    const text = result.response.text();
    return JSON.parse(text);
  }

  async healthCheck(): Promise<boolean> {
    try {
      await this.model.generateContent('test');
      return true;
    } catch {
      return false;
    }
  }
}
```

### State Management (Zustand)

```typescript
// src/stores/result-store.ts
import { create } from 'zustand';
import { persist } from 'zustand/middleware';
import type { AIResponse } from '@/lib/types/ai';

interface ResultState {
  currentResult: AIResponse | null;
  isLoading: boolean;
  error: string | null;
  generate: (preferences: UserPreferences) => Promise<void>;
  clear: () => void;
}

export const useResultStore = create<ResultState>()(
  persist(
    (set) => ({
      currentResult: null,
      isLoading: false,
      error: null,

      generate: async (preferences) => {
        set({ isLoading: true, error: null });
        try {
          const response = await fetch('/api/generate', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(preferences),
          });

          if (!response.ok) {
            const error = await response.json();
            throw new Error(error.message);
          }

          const data: AIResponse = await response.json();
          set({ currentResult: data, isLoading: false });
        } catch (error) {
          set({
            error: error instanceof Error ? error.message : 'Terjadi kesalahan',
            isLoading: false,
          });
        }
      },

      clear: () => set({ currentResult: null, error: null }),
    }),
    {
      name: 'namai-result',
      partialize: (state) => ({ currentResult: state.currentResult }),
    }
  )
);
```

### PWA Configuration

```json
// public/manifest.json
{
  "name": "NamAI Bayi",
  "short_name": "NamAI Bayi",
  "description": "Konsultan Nama Bayi AI untuk Orang Tua Indonesia",
  "start_url": "/",
  "display": "standalone",
  "background_color": "#FFFBF5",
  "theme_color": "#C4956A",
  "icons": [
    { "src": "/icons/icon-192x192.png", "sizes": "192x192", "type": "image/png" },
    { "src": "/icons/icon-512x512.png", "sizes": "512x512", "type": "image/png" }
  ]
}
```

### SEO Configuration

```typescript
// src/app/layout.tsx
export const metadata: Metadata = {
  title: {
    default: 'NamAI Bayi - Konsultan Nama Bayi AI',
    template: '%s | NamAI Bayi',
  },
  description:
    'Temukan nama bayi yang sempurna dengan AI. Konsultan nama bayi pintar untuk orang tua Indonesia. Makna mendalam, budaya tepat, rekomendasi personal.',
  keywords: [
    'nama bayi', 'nama bayi Indonesia', 'konsultan nama bayi',
    'nama bayi islami', 'nama bayi jawa', 'nama bayi sunda',
    'nama bayi modern', 'rekomendasi nama bayi', 'nama bayi AI',
  ],
  openGraph: {
    title: 'NamAI Bayi - Konsultan Nama Bayi AI',
    description: 'Temukan nama bayi sempurna dengan kecerdasan AI.',
    images: ['/og-image.png'],
  },
  manifest: '/manifest.json',
  icons: { icon: '/favicon.ico', apple: '/icons/icon-192x192.png' },
};
```

### Performance Targets

| Metric | Target |
|--------|--------|
| LCP | < 2.5s |
| FID | < 100ms |
| CLS | < 0.1 |
| TTFB | < 200ms |
| Lighthouse Score | > 90 |
| Bundle Size (initial) | < 150KB JS |

## Implementation

- Start with Next.js setup and routing
- Build shared UI components first
- Implement Zustand stores
- Create API routes with AI provider layer
- Build page components progressively
- Add PWA support
- Implement analytics

## Future Improvement

- ISR for public share pages
- Edge runtime for API routes (lower latency)
- i18n for multi-language support
- Storybook for component library
- E2E tests with Playwright
- Performance monitoring with Vercel Analytics
