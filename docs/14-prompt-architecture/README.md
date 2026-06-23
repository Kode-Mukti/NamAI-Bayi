# 14 — Prompt Architecture

## Summary

Systematic prompt engineering framework for reliable, structured, and culturally-aware AI outputs. Prompts are versioned, testable, and optimized for the selected AI provider.

## Reasoning

Prompt quality directly determines output quality. A poorly designed prompt produces culturally inappropriate names, weak explanations, and inconsistent formatting. The prompt architecture ensures every AI interaction follows a consistent, controllable structure.

## Recommendation

### Prompt Structure

Every prompt follows this structure:

```
[SYSTEM PROMPT]
[Role Definition]
[Behavior Rules]
[Output Format]
[Quality Standards]
[Constraints]
[Cultural Knowledge]

[USER PROMPT]
[User Preferences]
[Strategy Instruction]
[Specific Requirements]

[CONTEXT]
[Previous Names (if regenerating)]
[Disliked Names to Avoid]
```

### System Prompt Template

```
# Role
Anda adalah Konsultan Nama Bayi AI yang sangat berpengalaman untuk orang tua Indonesia.
Anda memiliki pengetahuan mendalam tentang:
- 300+ suku dan budaya Indonesia
- 6 agama resmi dan tradisi penamaan
- 700+ bahasa daerah
- Tren nama Indonesia modern dan tradisional
- Nama internasional yang mudah diucapkan orang Indonesia

# Behavior Rules
1. Anda WAJIB bertindak seperti konsultan ahli, bukan generator acak
2. Setiap rekomendasi WAJIB memiliki alasan yang jelas
3. Anda HARUS mempertimbangkan konteks budaya dan agama pengguna
4. Anda TIDAK BOLEH merekomendasikan nama yang tidak sesuai dengan agama pengguna
5. Anda HARUS menjelaskan filosofi di balik setiap nama
6. Anda TIDAK BOLEH merekomendasikan nama yang terlalu umum jika pengguna minta unik
7. Anda HARUS mempertimbangkan kombinasi nama ayah dan ibu jika diberikan

# Quality Standards
- Setiap nama harus memiliki makna yang jelas dalam Bahasa Indonesia
- Nama harus mudah diucapkan dalam konteks Indonesia
- Nama harus sesuai dengan norma budaya dan agama yang dipilih
- Hindari nama yang memiliki konotasi negatif dalam bahasa daerah mana pun
- Pastikan variasi dalam rekomendasi (jangan semua dari satu budaya)

# Constraints
- Maksimal 5 rekomendasi per konsultasi
- Setiap rekomendasi maksimal 4 kata untuk nama lengkap
- Gunakan Bahasa Indonesia yang baik dan benar
- Hindari saran yang terlalu kontroversial atau tidak pantas

# Output Format
RESPON ANDA HARUS DALAM FORMAT JSON. JANGAN ADA TEKS LAIN DI LUAR JSON.
Gunakan schema yang telah ditentukan.
```

### User Prompt Template

```
# Informasi Pengguna
{
  "namaAyah": "$fatherName",
  "namaIbu": "$motherName",
  "jenisKelamin": "$gender",
  "agama": "$religion",
  "budaya": "$culture",
  "provinsi": "$province",
  "makna": "$meaning",
  "kepribadian": "$personality",
  "hurufAwal": "$firstLetter",
  "hurufAkhir": "$lastLetter",
  "maksPanjang": "$maxLength",
  "namaFavorit": $favoriteNames,
  "namaTidakDisukai": $dislikedNames,
  "tingkatKeunikan": $uniqueLevel,
  "tingkatTradisional": $traditionalLevel
}

# Strategi Penamaan
Strategi yang dipilih: $selectedStrategy

# Instruksi Khusus
1. Hasilkan 20-30 kandidat nama
2. Evaluasi setiap kandidat berdasarkan kriteria kualitas
3. Pilih 5 terbaik dengan skor komposit tertinggi
4. Pastikan diversitas dalam 5 rekomendasi
5. Untuk setiap nama, berikan penjelasan lengkap
```

### Versioning Strategy

```
prompts/
├── v1/
│   ├── system-prompts/
│   │   └── main-consultant.md
│   └── user-prompts/
│       └── generate-names.md
├── v2/
│   ├── system-prompts/
│   │   ├── main-consultant.md
│   │   └── premium-consultant.md
│   └── user-prompts/
│       ├── generate-names.md
│       └── regenerate-with-feedback.md
└── current -> v1  (symlink)
```

### Prompt Testing Framework

```
Test Case: "User Muslim, Jawa, cari nama unik untuk perempuan"
Expected: Islamic-Javanese blend, unique, female
Validation: Check cultural relevance, religious appropriateness

Test Case: "User Hindu, Bali, tradisional untuk laki-laki"
Expected: Balinese/Sanskrit, traditional, male
Validation: Check caste appropriateness, Balinese conventions

Test Case: "No preferences entered"
Expected: AI selects best strategy from full context
Validation: Check diversity, general Indonesia relevance

Test Case: "User specifies disliked names"
Expected: None of the disliked names appear
Validation: Exact match check
```

### Prompt Optimization Guidelines

1. **Be specific**: "Nama harus memiliki makna dalam Bahasa Indonesia" not "Nama harus bermakna"
2. **Use examples**: Show good and bad examples in system prompt
3. **Negative constraints**: "JANGAN gunakan nama yang berarti 'kesedihan'"
4. **Format strictness**: "RESPON ANDA HARUS DALAM FORMAT JSON"
5. **Cultural guardrails**: "Pastikan nama tidak memiliki konotasi negatif di budaya manapun di Indonesia"
6. **Cost optimization**: Keep system prompt under 2K tokens, user prompt under 1K tokens

## Implementation

- Store prompts as versioned markdown files
- Load prompts dynamically based on version config
- Test each prompt version against test cases
- Monitor output quality per prompt version
- A/B test prompt variations for quality

## Future Improvement

- Automated prompt testing suite
- Prompt version analytics (quality by version)
- Dynamic prompt assembly based on user input
- Multi-language prompt support
- User feedback loop for prompt refinement
