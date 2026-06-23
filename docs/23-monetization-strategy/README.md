# 23 — Monetization Strategy

## Summary

Freemium monetization model with Premium AI as the primary revenue driver. Free tier drives adoption and virality; Premium tier generates revenue through deeper AI capabilities.

## Reasoning

Baby naming is an infrequent need (1-3 times per parent). This makes subscription-only models unsuitable. The strategy must maximize free users (for SEO, virality, brand building) while converting power users who want more depth, uniqueness, or multiple consultations.

## Recommendation

### Monetization Model: Freemium

```
┌─────────────────────────────────────────────────────┐
│                    FREE TIER                         │
│  • 3 consultations per day (soft limit)              │
│  • 5 name recommendations per consultation           │
│  • Basic explanation (meaning, origin, pronunciation)│
│  • Save up to 20 names to wishlist                   │
│  • Share to WhatsApp / Instagram                     │
│  • Public link (basic)                               │
│                                                     │
│  Cost: $0   Goal: Adoption + Virality + SEO          │
└─────────────────────────────────────────────────────┘
                          │
                          ▼
┌─────────────────────────────────────────────────────┐
│                   PREMIUM TIER                       │
│  • Unlimited consultations                           │
│  • 10 name recommendations per consultation          │
│  • Full explanation (philosophy, cultural depth)     │
│  • Unlock all advanced input fields                  │
│  • Name battle with detailed comparison              │
│  • AI Chat follow-up (future)                        │
│  • Priority AI processing (faster)                   │
│  • Unlimited wishlist                                │
│  • Ad-free experience                                │
│                                                     │
│  Price: Rp 35,000/month or Rp 350,000/year         │
│  Goal: Revenue                                       │
└─────────────────────────────────────────────────────┘
```

### Pricing Strategy

| Tier | Price (Monthly) | Price (Yearly) | Est. Conversion |
|------|-----------------|----------------|-----------------|
| Free | Rp 0 | Rp 0 | 95% |
| Premium | Rp 35,000 | Rp 350,000 (-17%) | 5% |

### Revenue Projections

```
Year 1 (Conservative):
  MAU: 50,000
  Premium Conversion: 3% → 1,500 paid users
  Monthly Revenue: 1,500 × Rp 35,000 = Rp 52,500,000
  Annual Revenue (if yearly): Assume 30% yearly → Rp ~700,000,000

Year 1 (Target):
  MAU: 50,000
  Premium Conversion: 5% → 2,500 paid users
  Monthly Revenue: 2,500 × Rp 35,000 = Rp 87,500,000
  Annual Revenue: Rp ~1,000,000,000

Year 3 (Growth):
  MAU: 500,000
  Premium Conversion: 5% → 25,000 paid users
  Monthly Revenue: Rp ~875,000,000
  + B2B API revenue: Rp 50,000,000/month
```

### Cost Structure

| Cost Item | MVP Monthly | Scale Monthly |
|-----------|-------------|---------------|
| Gemini AI (5K consultations/month) | $25 | $250 (50K) |
| Vercel Hosting | $20 | $200 |
| Play Store (one-time $25) | — | — |
| Domain | $1 | $1 |
| Analytics (Amplitude) | Free tier | $50 |
| Firebase | Free tier | $25 |
| **Total** | **~$46** | **~$526** |

### Additional Revenue Streams

| Stream | Description | Timeline | Est. Revenue |
|--------|-------------|----------|--------------|
| Premium AI | Core subscription | Month 4 | Rp 50-100M/mo |
| B2B API | Hospitals, clinics, baby brands | Year 2 | Rp 25-50M/mo |
| Sponsored Content | Baby product promotions | Year 2 | Rp 5-10M/mo |
| White Label | Branded version for companies | Year 3 | Rp 20-50M/mo |
| Naming Insights | Data report for brands | Year 3 | Rp 10-25M/mo |
| AI Chat (Premium) | Premium add-on | Year 2 | Included in Premium |

### Monetization Principles

1. **Free tier must be genuinely useful** — otherwise users leave before considering Premium
2. **Premium value must be obvious** — users should see what they're missing
3. **No dark patterns** — clear pricing, easy cancellation
4. **Pay for AI depth, not features** — Premium unlocks more AI capability, not basic features
5. **Annual discount to improve retention**
6. **Free trial of Premium** — 3 days unlimited, no payment needed

### Conversion Optimization

| Tactic | Trigger | Description |
|--------|---------|-------------|
| Consultation limit reached | After 3rd free consultation | "Ingin lebih banyak? Coba Premium gratis 3 hari" |
| Premium feature preview | When trying to use Premium feature | "Fitur ini tersedia di Premium" with 1 free use |
| Name detail depth | After viewing 3 basic details | "Dapatkan analisis lebih dalam dengan Premium" |
| Battle feature | After saving 5+ names | "Bandingkan nama dengan Premium Battle" |
| End of session | After consultation | "Nikmati konsultasi tanpa batas" upsell |

### Payment Methods (Indonesia)

- GoPay
- OVO
- DANA
- LinkAja
- Bank Transfer (BCA, Mandiri, BNI)
- Credit/Debit Card
- Google Play Store billing (Android)

## Implementation

- Build free tier completely for MVP
- Add Premium gating in post-MVP sprint (month 4-6)
- Implement payment integration with Midtrans (Indonesian payment gateway)
- Run 3-day free trial for Premium acquisition
- Monitor conversion and adjust pricing

## Future Improvement

- Dynamic pricing based on usage
- Family plan (shared Premium across devices)
- Premium gifting
- Bundle with future Kode Mukti products
- Tiered Premium plans (Premium Lite, Premium Pro)
