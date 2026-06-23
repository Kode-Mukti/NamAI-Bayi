# 09 — Prompt Engineering

## Summary

Complete prompt engineering specification for AI name generation. Defines system prompt, user prompt builder, output format enforcement, error handling, and retry logic.

## Reasoning

The AI/Prompt Agent owns all prompt logic. The prompt determines output quality. No other agent modifies prompts. Prompts are versioned and stored as string resources/templates in the AI module.

## Recommendation

---

### Prompt Architecture

```
┌─────────────────────────────────────────────┐
│               PromptBuilder                 │
│                                             │
│  Input: BabyProfile + NamingStrategy        │
│  Output: AIPromptRequest                    │
│                                             │
│  1. Select strategy (strategy logic)        │
│  2. Build system prompt (from template)     │
│  3. Build user prompt (from profile)        │
│  4. Apply format enforcement                │
│  5. Return AIPromptRequest                  │
└─────────────────────────────────────────────┘
```

---

### System Prompt (Template)

This is sent as the `system` role message. Fixed for all consultations.

```text
ANDA ADALAH KONSULTAN NAMA BAYI AHLI UNTUK INDONESIA.

IDENTITAS:
- Anda adalah konsultan nama bayi yang sangat berpengalaman
- Anda memiliki pengetahuan mendalam tentang budaya Indonesia
- Anda memahami 300+ suku, 6 agama, dan tradisi penamaan
- Anda ahli dalam 13 strategi penamaan

PERILAKU:
1. Bersikaplah seperti konsultan ahli, bukan generator acak
2. Setiap rekomendasi WAJIB memiliki alasan yang jelas
3. Pertimbangkan konteks budaya, agama, dan preferensi pengguna
4. JANGAN merekomendasikan nama yang tidak sesuai dengan agama
5. JANGAN merekomendasikan nama dengan konotasi negatif
6. Pastikan variasi antar rekomendasi (jangan semua dari budaya sama)

KUALITAS:
- Setiap nama harus memiliki makna yang jelas dalam Bahasa Indonesia
- Nama harus mudah diucapkan dalam konteks Indonesia
- Filosofi harus mendalam dan bermakna (minimal 50 karakter)
- Skor keunikan harus mencerminkan realita (jangan semua 90+)

STRUKTUR OUTPUT:
RESPON ANDA HARUS DALAM FORMAT JSON. TANPA TEKS LAIN.
Gunakan schema yang telah ditentukan.
```

---

### User Prompt Builder

Builds the `user` role message dynamically from BabyProfile and selected strategy.

```kotlin
class UserPromptBuilder {

    fun build(profile: BabyProfile, strategy: NamingStrategy): String {
        return buildString {
            appendLine("Saya orang tua yang mencari nama bayi dengan preferensi berikut:")
            appendLine()
            appendLine("STRATEGI PENAMAAN: ${translateStrategy(strategy)}")
            appendLine()
            appendLine("PREFERENSI:")
            appendField("Nama Ayah", profile.fatherName)
            appendField("Nama Ibu", profile.motherName)
            appendField("Jenis Kelamin", translateGender(profile.gender))
            appendField("Agama", translateReligion(profile.religion))
            appendField("Budaya", translateCulture(profile.culture))
            appendField("Provinsi", profile.province)
            appendField("Makna yang Diinginkan", profile.desiredMeaning)
            appendField("Kepribadian", profile.desiredPersonality)
            if (profile.firstLetter != null) {
                appendField("Huruf Awal", profile.firstLetter.toString())
            }
            if (profile.lastLetter != null) {
                appendField("Huruf Akhir", profile.lastLetter.toString())
            }
            appendField("Maksimal Panjang Nama", "${profile.maxLength} karakter")
            appendField("Tingkat Keunikan (1-5)", profile.uniquenessLevel.toString())
            appendField("Tingkat Tradisional (1-5)", profile.traditionalLevel.toString())
            appendLine()
            appendLine("INSTRUKSI:")
            appendLine("1. Hasilkan 20-30 kandidat nama sesuai strategi '$strategy'")
            appendLine("2. Evaluasi setiap kandidat berdasarkan kriteria kualitas")
            appendLine("3. Pilih 5 terbaik dengan skor komposit tertinggi")
            appendLine("4. Pastikan diversitas dalam 5 rekomendasi")
            appendLine("5. Berikan skor keunikan yang realistis (1-100)")
            appendLine("6. Untuk setiap nama, berikan penjelasan lengkap")
        }
    }

    private fun appendField(label: String, value: String) {
        if (value.isNotBlank()) appendLine("- $label: $value")
    }
}
```

---

### Strategy Selection Logic

```kotlin
class StrategySelector {

    fun select(profile: BabyProfile): NamingStrategy {
        val candidates = mutableListOf<NamingStrategy>()

        // Religion-based
        when (profile.religion) {
            Religion.ISLAM -> candidates.addAll(listOf(NamingStrategy.ISLAMI, NamingStrategy.ARAB))
            Religion.HINDU -> candidates.addAll(listOf(NamingStrategy.SANSKERTA, NamingStrategy.BALI))
            Religion.CHRISTIAN, Religion.CATHOLIC -> candidates.add(NamingStrategy.INTERNASIONAL)
            Religion.BUDDHA -> candidates.add(NamingStrategy.SANSKERTA)
            Religion.CONFUCIAN -> candidates.add(NamingStrategy.INTERNASIONAL)
            Religion.OTHER -> candidates.add(NamingStrategy.MODERN)
        }

        // Culture-based
        when (profile.culture) {
            Culture.JAVA -> candidates.add(NamingStrategy.JAWA)
            Culture.SUNDA -> candidates.add(NamingStrategy.SUNDA)
            Culture.BALI -> candidates.add(NamingStrategy.BALI)
            Culture.BATAK -> candidates.add(NamingStrategy.TRADISIONAL)
            Culture.MINANG -> candidates.add(NamingStrategy.TRADISIONAL)
            Culture.BETAWI -> candidates.add(NamingStrategy.TRADISIONAL)
            Culture.MELAYU -> candidates.add(NamingStrategy.TRADISIONAL)
            Culture.BUGIS -> candidates.add(NamingStrategy.TRADISIONAL)
            Culture.CHINESE -> candidates.add(NamingStrategy.INTERNASIONAL)
            Culture.ARAB -> candidates.add(NamingStrategy.ARAB)
            Culture.OTHER -> candidates.add(NamingStrategy.MODERN)
        }

        // Preference-based
        if (profile.uniquenessLevel >= 4) candidates.add(NamingStrategy.AI_GENERATED)
        if (profile.traditionalLevel >= 4) candidates.add(NamingStrategy.TRADISIONAL)
        if (profile.traditionalLevel <= 2) candidates.add(NamingStrategy.MODERN)

        // Name combination
        if (profile.fatherName.isNotBlank() && profile.motherName.isNotBlank()) {
            candidates.add(NamingStrategy.KOMBINASI_ORANG_TUA)
        }

        // Default
        if (candidates.isEmpty()) candidates.add(NamingStrategy.AI_SELECTED)

        // Pick first valid. For MVP, simple selection. Future: ensemble.
        return candidates.distinct().first()
    }
}
```

---

### JSON Format Enforcement

```kotlin
class FormatEnforcer {

    /**
     * Applies JSON enforcement to the request.
     * For OpenRouter: use response_format: { type: "json_object" }
     * For fallback: append format instructions.
     */
    fun enforce(provider: AIProviderType, prompt: String): String {
        return when (provider) {
            AIProviderType.OPENROUTER -> prompt  // Uses response_format param
            AIProviderType.GEMINI -> "$prompt\n\nRESPON DALAM FORMAT JSON SAJA."
            AIProviderType.OPENAI -> prompt  // Uses response_format param
            AIProviderType.MOCK -> prompt
        }
    }

    /**
     * Attempt to extract JSON from potentially messy response.
     */
    fun extractJson(raw: String): String {
        // Try direct parse first
        // Fallback: find first { and last }
        val start = raw.indexOf('{')
        val end = raw.lastIndexOf('}')
        return if (start >= 0 && end > start) {
            raw.substring(start, end + 1)
        } else {
            raw  // Fall through to error
        }
    }
}
```

---

### Prompt Templates Folder

```
data/remote/ai/prompts/
├── system_prompt_v1.txt           # System prompt text
├── user_prompt_template_v1.txt    # User prompt template
├── strategy_descriptions.json     # Strategy translations
└── translations.json              # Gender/religion/culture labels
```

---

### Error & Retry Prompts

#### On AI Response Parse Failure

```text
Maaf, AI memberikan respons yang tidak valid. Mohon coba lagi.
(Internal: parse error — response did not match JSON schema)
```

#### On AI Content Filter

```text
Maaf, AI menolak permintaan karena alasan keamanan.
Silakan perbaiki input dan coba lagi.
```

#### On AI Timeout

```text
AI sedang sibuk. Silakan coba lagi dalam beberapa saat.
```

#### Retry Strategy (Technical)

```kotlin
class RetryStrategy {
    val maxRetries = 2
    val baseDelayMs = 1000L
    val backoffMultiplier = 2.0

    // Retry 1: wait 1s
    // Retry 2: wait 2s
    // After 3 failures: return error to user
}
```

---

### Prompt Versioning

| Version | Date | Changes |
|---------|------|---------|
| v1.0 | Sprint 3 | Initial prompt |
| v1.1 | Post-beta | Cultural guardrails added |
| v1.2 | Post-launch | Uniqueness score calibration |
