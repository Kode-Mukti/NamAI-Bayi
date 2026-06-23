# 26 — Security

## Summary

Security architecture for NamAI Bayi covering API key management, data privacy, network security, and AI-specific risks. Zero-knowledge design: user data stays on device wherever possible.

## Reasoning

NamAI Bayi handles family information (names, religion, culture) which users consider private. As an AI product, it also sends data to third-party AI providers. Security must protect user trust while enabling AI functionality.

## Recommendation

### Security Principles

1. **Zero Knowledge** — User data stays client-side whenever possible
2. **Least Privilege** — Only send minimum data required for AI generation
3. **Encrypt in Transit** — All network calls over HTTPS/TLS 1.3
4. **No Secrets in Client** — API keys in server-only code or secure backend
5. **Minimal Data Collection** — Only anonymous analytics, no PII
6. **Transparency** — Clear privacy policy about data usage

### API Key Management

```
┌─────────────────────────────────────────────┐
│               Web Architecture               │
│                                              │
│  Browser ──► Next.js API Route ──► AI API   │
│  (no key)    (server-side)      (key here)   │
│                                              │
│  API key stored in .env.local                │
│  Never exposed to client bundle              │
└─────────────────────────────────────────────┘

┌─────────────────────────────────────────────┐
│            Android Architecture              │
│                                              │
│  Option A (MVP):                             │
│  Android ──► Next.js API Route ──► AI API   │
│  (no key)      (server-side)    (key here)  │
│                                              │
│  Option B (future backend):                  │
│  Android ──► Backend API ──► AI API          │
│  (auth token)  (server)      (key here)     │
│                                              │
│  NEVER embed API key in Android APK          │
└─────────────────────────────────────────────┘
```

### MVP Approach: API Gateway Pattern

```
Client App (Android/Web)
        │
        │ HTTPS
        ▼
Internal API Route (/api/generate)
  - Rate limited by IP
  - No authentication needed (MVP)
  - Server-side API key
  - Input validation
  - Response sanitization
        │
        ▼
AI Provider (Gemini/OpenAI)
  - API key stored server-side
  - Data handled per provider terms
```

### Data Privacy

| Data Type | Stored Where | Shared With | Retention |
|-----------|-------------|-------------|-----------|
| User Preferences | Device only (Room/LocalStorage) | AI provider (in prompt) | Deleted after session |
| AI Results | Device wishlist (Room/LocalStorage) | Nobody | Until user removes |
| Analytics (anonymized) | Amplitude/Firebase | Analytics platform | 24 months |
| Name choices | Device only | Shared if user shares | Until user removes |
| Crash logs | Firebase Crashlytics | Firebase | 90 days |

### Privacy-First Design

```
User Input
    │
    ├─► Device Storage (Room/LocalStorage)
    │       └─ Wishlist, preferences, history
    │
    ├─► AI Provider (temporary, for generation only)
    │       └─ Data not used for training
    │       └─ Deleted after response
    │
    └─► Analytics (anonymous)
            └─ No names, no PII
            └─ Aggregated events only
```

### Network Security

| Layer | Measure |
|-------|---------|
| Transport | TLS 1.3, HTTPS only |
| API Keys | Server-side only, rotated quarterly |
| Rate Limiting | Per IP: 10 requests/minute (MVP) |
| CORS | Restrict to own domain (Web) |
| Input Validation | Zod schema validation server-side |
| Output Sanitization | Sanitize AI output before rendering |
| Certificate Pinning | Android: OkHttp certificate pinning |

### AI-Specific Security

| Risk | Mitigation |
|------|------------|
| Prompt injection | Strict system prompt, input sanitization |
| Sensitive data leakage | No PII in prompts, data minimization |
| Hallucinated names | Response validation against schema |
| Inappropriate names | Cultural guardrails in prompts, content filtering |
| Provider data handling | Review each provider's data policy before use |
| Model bias | Regular evaluation for cultural/religious bias |

### Android Security

| Area | Implementation |
|------|---------------|
| Network | Certificate pinning with OkHttp |
| Storage | EncryptedSharedPreferences for settings |
| Database | Room with auto-migration |
| Logging | No sensitive data in logs |
| Obfuscation | ProGuard/R8 in release builds |
| Root detection | Basic detection for sensitive features |
| Secure flag | FLAG_SECURE on result screen (prevent screenshots) |

### Web Security

| Area | Implementation |
|------|---------------|
| CSP | Strict Content Security Policy headers |
| XSS | React's built-in XSS protection |
| CSRF | SameSite cookies, CSRF tokens for API |
| Iframe | X-Frame-Options: DENY |
| HSTS | Strict-Transport-Security header |
| API Security | Rate limiting, input validation |

### Data Breach Response Plan

1. **Detection**: Automated monitoring for unusual API patterns
2. **Containment**: Revoke compromised API keys, rotate secrets
3. **Assessment**: Determine scope of breach
4. **Notification**: Inform affected users within 72 hours (UU PDP)
5. **Remediation**: Fix vulnerability, improve monitoring
6. **Review**: Post-mortem with security improvements

### Privacy Policy (Required Content)

- What data we collect
- How data is used (AI generation)
- How data is stored
- Third-party data sharing (AI providers, analytics)
- User rights (access, deletion, portability)
- Contact for privacy concerns
- UU PDP compliance statement

### Compliance

| Regulation | Applicability | Status |
|------------|--------------|--------|
| UU PDP (Indonesia) | Full compliance required | P0 |
| GDPR | Not required (Indonesia-only), but good practice | P2 |
| COPPA | Not applicable (no children users) | N/A |

## Implementation

- Implement API Gateway pattern from day 1
- Never embed API keys in client code
- Add input validation server-side
- Set up rate limiting before public launch
- Write privacy policy before launch
- Security audit before public release

## Future Improvement

- End-to-end encryption for chat features
- Biometric authentication for Premium accounts
- Security audit by third-party firm
- Bug bounty program
- SOC 2 compliance for B2B features
