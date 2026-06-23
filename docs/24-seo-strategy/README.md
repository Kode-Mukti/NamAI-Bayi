# 24 — SEO Strategy

## Summary

Comprehensive SEO strategy to capture organic traffic from Indonesian parents searching for baby names. Focus on long-tail keywords, local search, and content that outranks competitors.

## Reasoning

Baby name search is inherently SEO-driven. Indonesian parents search "nama bayi perempuan islami modern" or "nama bayi laki-laki jawa" — hundreds of thousands of searches per month. SEO is the highest-ROI acquisition channel for NamAI Bayi.

## Recommendation

### Keyword Strategy

#### Primary Keywords (High Volume)

| Keyword | Monthly Search (Est.) | Intent | Priority |
|---------|----------------------|--------|----------|
| nama bayi perempuan | 200K+ | Informational | High |
| nama bayi laki-laki | 200K+ | Informational | High |
| nama bayi islami | 150K+ | Informational | High |
| nama bayi modern | 100K+ | Informational | High |
| rekomendasi nama bayi | 80K+ | Commercial | High |
| arti nama bayi | 60K+ | Informational | High |

#### Long-Tail Keywords (High Conversion)

| Keyword | Intent | Content Type |
|---------|--------|-------------|
| nama bayi perempuan islami modern 3 kata | Commercial | Landing page |
| nama bayi laki-laki jawa kuno dan artinya | Commercial | Landing page |
| rangkaian nama bayi perempuan dari sansekerta | Commercial | Landing page |
| nama bayi laki-laki huruf a sampai z | Informational | Tool page |
| arti nama arumi dalam islam | Informational | Name detail |
| nama bayi sunda perempuan yang jarang dipakai | Commercial | Landing page |
| nama bayi kristen yang unik dan modern | Commercial | Landing page |
| kombinasi nama ayah dan ibu untuk bayi | Commercial | Tool page |

#### Strategy-Based Keywords (13 strategies)

Create targeted pages for each naming strategy:
- nama bayi islami
- nama bayi jawa
- nama bayi sunda
- nama bayi bali
- nama bayi sansekerta
- nama bayi modern
- nama bayi tradisional
- nama bayi internasional
- nama bayi arab
- nama bayi korea
- nama bayi jepang
- nama kombinasi orang tua
- nama bayi AI generated

### Content Strategy

#### Pillar Pages

Each pillar page covers a major category comprehensively:

```
/nama-bayi-perempuan/
/nama-bayi-laki-laki/
/nama-bayi-islami/
/nama-bayi-modern/
/nama-bayi-jawa/
```

#### Cluster Pages (Long-tail)

Each pillar links to 10-20 cluster pages:

```
/nama-bayi-perempuan/islami-modern/
/nama-bayi-perempuan/sunda/
/nama-bayi-laki-laki/jawa-kuno/
/nama-bayi-laki-laki/sansekerta/
```

#### Tool Pages (High Conversion)

```
/konsultasi              → Main AI tool (indexed)
/hasil?nama=arumi        → Public result pages (noindex)
/nama/[name]             → Individual name details (indexed)
```

#### Blog / Article Content

| Topic | Frequency | Purpose |
|-------|-----------|---------|
| Nama bayi berdasarkan bulan lahir | Monthly | Seasonal traffic |
| Tren nama bayi 2024/2025 | Yearly | Trend content |
| Panduan memilih nama bayi | Evergreen | Informational |
| Arti nama populer | Weekly | Name detail pages |
| Tips memilih nama sesuai agama | Monthly | Religious intent |

### Technical SEO

| Element | Implementation |
|---------|---------------|
| Meta Titles | Dynamic: "{name} — Arti Nama dan Filosofi | NamAI Bayi" |
| Meta Descriptions | 150-160 chars, include keyword + CTA |
| Canonical URLs | Self-referencing canonical |
| Open Graph | Title, description, image for all shareable pages |
| JSON-LD Schema | Article, FAQ, HowTo, Product schemas |
| Sitemap | Dynamic sitemap.xml updated daily |
| Robots.txt | Allow all public pages, disallow /api/ |
| Core Web Vitals | LCP < 2.5s, FID < 100ms, CLS < 0.1 |
| Mobile First | Fully responsive design |
| AMP | Not needed (Next.js SSG is fast enough) |
| Hreflang | bahasa Indonesia (id) |

### On-Page SEO

#### URL Structure

```
/nama-bayi-perempuan/
/nama-bayi-perempuan/islami-modern/
/konsultasi/
/hasil/konsultasi/{id}/    → noindex
/nama/{slug}/               → individual name page
```

#### Internal Linking

- Every name page links to related strategies
- Every pillar page links to all cluster pages
- Tool pages link to relevant content pages
- Breadcrumbs on all pages

#### Content Optimization

- Target keyword in H1 (first 100 words)
- 2-3 related keywords in H2s
- Alt text on all images with keywords
- FAQ schema for "nama bayi [keyword]" searches
- Table of contents for long articles

### Off-Page SEO

| Tactic | Strategy |
|--------|----------|
| Backlinks | Guest posts on parenting blogs, baby product sites |
| Social Signals | Share results on Instagram, WA drives engagement |
| PR | Partner with parenting influencers (ibu blogger) |
| Brand Mentions | Kode Mukti mentioned in articles, forums |
| Local SEO | Google My Business for Kode Mukti |

### SEO Measurement

| Metric | Tool | Target |
|--------|------|--------|
| Organic Traffic | Google Search Console | 70% of total traffic |
| Keyword Rankings | Ahrefs / SEMrush | Top 10 for 50+ keywords |
| Click-Through Rate | GSC | > 5% average |
| Bounce Rate | Google Analytics | < 40% |
| Pages Per Session | GA | > 3 |
| Indexed Pages | GSC | 500+ by month 6 |
| Domain Authority | Moz | > 30 by year 1 |

### SEO Execution Plan

| Phase | Activities | Timeline |
|-------|------------|----------|
| Foundation | Technical SEO, sitemap, schema, meta | Pre-launch |
| Content Pillars | Create 5 pillar pages | Month 1-2 |
| Cluster Content | Create 20 cluster pages | Month 2-3 |
| Tool Pages | Index AI result pages (static versions) | Month 3-4 |
| Blog Content | Weekly blog posts | Ongoing |
| Link Building | Outreach, guest posts | Month 4+ |
| Optimization | A/B test titles, improve CTR | Ongoing |

## Implementation

- Next.js SSR/SSG for all content pages
- Dynamic metadata per route
- Automatic sitemap generation
- Structured data injection via JSON-LD
- Analytics integration for search performance

## Future Improvement

- Programmatic SEO (generate 10,000+ name pages from database)
- Video content (YouTube shorts on nama bayi)
- Voice search optimization
- Featured snippet targeting
- International SEO for Indonesian diaspora
