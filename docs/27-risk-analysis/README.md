# 27 — Risk Analysis

## Summary

Risk assessment covering product, technical, AI, business, and operational risks. Each risk includes probability, impact, severity, and mitigation strategy.

## Reasoning

Startups face many unknowns. Proactive risk identification prevents surprises. Every risk has a mitigation plan — either prevent, reduce, or accept.

## Recommendation

### Risk Matrix

```
Severity: ● Critical  ● High  ● Medium  ● Low
Probability: ▲ High   ▲ Medium ▲ Low
```

### Product Risks

| # | Risk | Probability | Impact | Severity | Mitigation |
|---|------|-----------|--------|----------|------------|
| R1 | Users prefer static name lists over AI | Medium | Critical | ● | A/B test AI vs. list in early beta; pivot to hybrid if needed |
| R2 | AI recommendations feel generic | Medium | High | ● | Invest in prompt quality; add personalization depth |
| R3 | Users don't understand AI consultant positioning | High | Medium | ● | Clear copy on home screen; onboarding explanation |
| R4 | Low retention after first use (one-and-done) | High | High | ● | Daily names, push notifications, share features |
| R5 | Users want partner collaboration (Couple Mode) | Medium | Medium | ● | Survey users; fast-follow with Couple Mode if requested |
| R6 | Names conflict with local negative meanings | Low | Critical | ● | Cultural guardrails in prompts; review by Indonesian linguist |

### Technical Risks

| # | Risk | Probability | Impact | Severity | Mitigation |
|---|------|-----------|--------|----------|------------|
| T1 | AI response time > 8 seconds | Medium | High | ● | Use Gemini Flash (fastest); implement streaming; show progress |
| T2 | AI provider downtime | Medium | Critical | ● | Multi-provider fallback; cache common results; error UX |
| T3 | API key leak via client | Low | Critical | ● | API Gateway pattern; never embed keys in clients |
| T4 | Cross-platform inconsistency | Medium | Medium | ● | Shared design system; same prompt/validation code |
| T5 | PWA performance issues on low-end devices | High | Medium | ● | Lightweight bundle; lazy loading; Android native for low-end |
| T6 | Data loss on app update | Low | High | ● | Room migration testing; LocalStorage versioning |

### AI Risks

| # | Risk | Probability | Impact | Severity | Mitigation |
|---|------|-----------|--------|----------|------------|
| A1 | AI generates culturally inappropriate names | Low | Critical | ● | System prompt guardrails; response validation; blacklist |
| A2 | AI hallucinates meanings or origins | Medium | High | ● | Fact-checking in prompt; "AI-generated" disclaimer |
| A3 | AI cost exceeds budget | Medium | High | ● | Monitor cost daily; use cheapest adequate provider; caching |
| A4 | AI provider changes terms/pricing | Medium | Medium | ● | Provider abstraction layer; easy switching |
| A5 | Model bias (gender, religion, ethnicity) | Medium | Critical | ● | Bias testing; diverse prompt design; regular audits |
| A6 | Users prompt-inject the AI | Low | Medium | ● | Input sanitization; strict system prompt constraints |

### Business Risks

| # | Risk | Probability | Impact | Severity | Mitigation |
|---|------|-----------|--------|----------|------------|
| B1 | Low organic adoption (SEO/ASO fails) | Medium | Critical | ● | Invest heavily in SEO from day 1; target long-tail keywords |
| B2 | Competition copies AI concept | High | Medium | ● | Build brand trust first; innovate faster; cultural depth moat |
| B3 | Premium conversion too low | Medium | High | ● | Test pricing; improve premium value prop; free trial |
| B4 | Viral coefficient < 1.0 | Medium | High | ● | Improve share experience; add battle/vote features |
| B5 | Unable to monetize at all | Low | Critical | ● | Keep costs minimal ($46/mo); freemium model |
| B6 | Partnership opportunities don't materialize | Medium | Low | ● | Build self-sustaining growth without partnerships |

### Operational Risks

| # | Risk | Probability | Impact | Severity | Mitigation |
|---|------|-----------|--------|----------|------------|
| O1 | Solo developer bottleneck | High | High | ● | Prioritize ruthlessly; use well-known frameworks; document |
| O2 | Regulatory change (AI regulation in Indonesia) | Medium | Medium | ● | Stay informed; comply with UU PDP; legal review |
| O3 | Negative social media attention | Low | High | ● | Culturally sensitive design; moderation; PR response plan |
| O4 | AI provider blocks Indonesia traffic | Low | Critical | ● | Provider abstraction; multiple provider support |
| O5 | Play Store policy violation | Low | Critical | ● | Review policies; age rating 3+; no inappropriate content |

### Risk Response Plan

```
Critical Risks (immediate action required):
  - R1, R6, T2, T3, A2, A5, B1, B5, O4, O5
  → Add to Sprint 0 planning
  → Weekly monitoring
  → Concrete mitigation actions

High Risks (active monitoring):
  - R2, R4, T1, A1, A3, B2, B3
  → Monthly review
  → Mitigation in product roadmap
  → Trigger: if probability increases, escalate

Medium Risks (awareness):
  - R3, R5, T4, T5, A4, B4, B6, O1, O2
  → Quarterly review
  → Accept or monitor

Low Risks (no action needed):
  - R5, A6, O3
  → Just be aware
```

### Risk Monitoring

| Frequency | Activity |
|-----------|----------|
| Daily | AI response time, error rate, cost |
| Weekly | User feedback, reviews, analytics anomalies |
| Bi-weekly | Risk review in sprint retro |
| Monthly | Risk matrix update, competitive check |
| Quarterly | Full risk reassessment |

### Contingency Budget

| Item | Monthly Allocation |
|------|-------------------|
| AI cost overrun buffer | +50% of expected |
| Emergency provider switch | 1 day dev time |
| Negative feedback PR | 0 (content is free) |
| Server cost spike | +100% (still < $100) |

## Implementation

- Review risk matrix weekly during development
- Add critical risk mitigations to Sprint 0
- Create monitoring dashboard for key risks
- Document risk responses in runbook

## Future Improvement

- Automated risk monitoring
- Risk-based testing prioritization
- Insurance for cyber/data risks
- Legal retainer for regulatory changes
- Scenario planning for market shifts
