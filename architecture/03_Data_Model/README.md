# 03 — Data Model

## Summary

Complete data model specifications across all layers: domain models (pure Kotlin), Room entities (persistence), DTOs (network), and the mapping between them.

## Reasoning

Data models are the contract between agents. A domain agent defines models, a data agent creates entities/DTOs, and a mapping agent writes the converters. Without precise specifications, models drift apart and cause runtime crashes.

## Recommendation

---

### Domain Models (domain/model/)

These are pure Kotlin data classes. Used by presentation and domain layers. Never annotated with Room or serialization.

#### BabyProfile

```kotlin
data class BabyProfile(
    val fatherName: String = "",           // Nama ayah (opsional)
    val motherName: String = "",           // Nama ibu (opsional)
    val gender: Gender = Gender.MALE,      // Jenis kelamin
    val religion: Religion = Religion.ISLAM, // Agama
    val culture: Culture = Culture.JAVA,   // Budaya/suku
    val province: String = "",             // Provinsi asal
    val desiredMeaning: String = "",       // Makna yang diinginkan
    val desiredPersonality: String = "",   // Kepribadian yang diinginkan
    val firstLetter: Char? = null,         // Huruf awal (opsional)
    val lastLetter: Char? = null,          // Huruf akhir (opsional)
    val maxLength: Int = 15,               // Panjang maksimal nama
    val uniquenessLevel: Int = 3,          // 1=Sangat Populer, 5=Sangat Unik
    val traditionalLevel: Int = 3          // 1=Sangat Modern, 5=Sangat Tradisional
)

enum class Gender { MALE, FEMALE }
enum class Religion { ISLAM, CHRISTIAN, CATHOLIC, HINDU, BUDDHA, CONFUCIAN, OTHER }
enum class Culture {
    JAVA, SUNDA, BALI, BATAK, MINANG, BETAWI, MELAYU, BUGIS, CHINESE, ARAB, OTHER
}
```

#### BabyResult

```kotlin
data class BabyResult(
    val id: String,                         // UUID
    val profile: BabyProfile,               // Snapshot of input
    val recommendations: List<NameRecommendation>, // Top names
    val strategyUsed: NamingStrategy,       // Strategy selected
    val strategyExplanation: String,         // Why this strategy
    val createdAt: Long,                     // epoch millis
    val isFavorite: Boolean = false          // Quick favorite flag
)
```

#### NameRecommendation

```kotlin
data class NameRecommendation(
    val name: String,                          // Nama utama
    val meaning: String,                       // Arti dalam Bahasa Indonesia
    val philosophy: String,                    // Filosofi mendalam
    val pronunciation: String,                 // Panduan pengucapan
    val nicknames: List<String>,               // Opsi nama panggilan
    val origin: String,                        // Asal bahasa/budaya
    val culturalRelevance: String,              // Relevansi budaya
    val uniquenessScore: Int,                  // 1-100
    val uniquenessLabel: UniquenessLabel,       // Human label
    val popularityEstimate: PopularityLevel,    // Estimasi popularitas
    val internationalReadability: Readability,  // Kemudahan baca global
    val fullNameSuggestion: String,             // Saran nama lengkap
    val siblingCompatibility: String,           // Kompatibilitas dengan saudara
    val whyAIRecommends: String,                // Alasan AI merekomendasikan
    val variations: List<String>               // Variasi penulisan lain
)

enum class UniquenessLabel {
    SANGAT_UNIK, UNIK, CUKUP_UNIK, POPULER, SANGAT_POPULER
}
enum class PopularityLevel {
    SANGAT_POPULER, POPULER, CUKUP_POPULER, JARANG, SANGAT_JARANG
}
enum class Readability { MUDAH, SEDANG, SULIT }
```

#### NamingStrategy

```kotlin
enum class NamingStrategy {
    ISLAMI,
    MODERN,
    TRADISIONAL,
    JAWA,
    SUNDA,
    BALI,
    SANSKERTA,
    INTERNASIONAL,
    ARAB,
    KOREA_INSPIRED,
    JEPANG_INSPIRED,
    KOMBINASI_ORANG_TUA,
    AI_GENERATED,
    CAMPURAN,
    AI_SELECTED
}
```

#### FavoriteName

```kotlin
data class FavoriteName(
    val id: String,                     // UUID
    val babyResultId: String,           // FK ke BabyResult
    val name: String,
    val meaning: String,
    val uniquenessScore: Int,
    val origin: String,
    val createdAt: Long
)
```

#### HistoryItem

```kotlin
data class HistoryItem(
    val id: String,                     // UUID (sama dengan BabyResult.id)
    val gender: Gender,
    val religion: Religion,
    val culture: Culture,
    val strategyUsed: NamingStrategy,
    val nameCount: Int,                 // Jumlah nama di result
    val topName: String,                // Nama pertama di result
    val createdAt: Long
)
```

#### Settings

```kotlin
data class Settings(
    val isDarkMode: Boolean = false,
    val isOnboardingCompleted: Boolean = false,
    val aiProvider: AIProviderType = AIProviderType.OPENROUTER,
    val openRouterModel: String = "openai/gpt-4o-mini",
    val temperature: Float = 0.8f,
    val maxTokens: Int = 4096,
    val language: AppLanguage = AppLanguage.INDONESIAN
)

enum class AIProviderType { OPENROUTER, GEMINI, OPENAI, MOCK }
enum class AppLanguage { INDONESIAN, ENGLISH }
```

#### AIPromptRequest / AIResponse

```kotlin
data class AIPromptRequest(
    val systemPrompt: String,
    val userPrompt: String,
    val temperature: Float,
    val maxTokens: Int
)

data class AIResponse(
    val rawJson: String,                       // Raw JSON dari AI
    val parsed: BabyResult?,                   // Parsed result (null if parse failed)
    val provider: AIProviderType,
    val latencyMs: Long,
    val tokenCount: TokenCount,
    val error: String?                         // Error message if failed
)

data class TokenCount(
    val prompt: Int,
    val completion: Int,
    val total: Int
)
```

---

### Room Entities (data/local/entity/)

Annotated with Room. Mirror domain models but with Room constraints.

#### BabyResultEntity

```kotlin
@Entity(tableName = "baby_results")
data class BabyResultEntity(
    @PrimaryKey val id: String,
    val profileJson: String,              // BabyProfile serialized as JSON
    val recommendationsJson: String,      // List<NameRecommendation> as JSON
    val strategyUsed: String,             // NamingStrategy.name
    val strategyExplanation: String,
    val createdAt: Long,
    val isFavorite: Boolean
)
```

#### FavoriteEntity

```kotlin
@Entity(
    tableName = "favorites",
    foreignKeys = [ForeignKey(
        entity = BabyResultEntity::class,
        parentColumns = ["id"],
        childColumns = ["babyResultId"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class FavoriteEntity(
    @PrimaryKey val id: String,
    val babyResultId: String,
    val name: String,
    val meaning: String,
    val uniquenessScore: Int,
    val origin: String,
    val createdAt: Long
)
```

#### HistoryEntity

```kotlin
@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey val id: String,
    val gender: String,                   // Gender.name
    val religion: String,                 // Religion.name
    val culture: String,                  // Culture.name
    val strategyUsed: String,             // NamingStrategy.name
    val nameCount: Int,
    val topName: String,
    val createdAt: Long
)
```

---

### Network DTOs (data/remote/dto/)

#### OpenRouterRequest

```kotlin
data class OpenRouterRequest(
    val model: String,                    // e.g. "openai/gpt-4o-mini"
    val messages: List<Message>,
    val temperature: Float,
    val max_tokens: Int,
    val response_format: ResponseFormat? = null
) {
    data class Message(
        val role: String,                 // "system" or "user"
        val content: String
    )
    data class ResponseFormat(
        val type: String = "json_object"
    )
}
```

#### OpenRouterResponse

```kotlin
data class OpenRouterResponse(
    val id: String,
    val choices: List<Choice>,
    val usage: Usage?,
    val error: OpenRouterError? = null
) {
    data class Choice(
        val index: Int,
        val message: Message,
        val finish_reason: String?
    ) {
        data class Message(
            val role: String,
            val content: String
        )
    }
    data class Usage(
        val prompt_tokens: Int,
        val completion_tokens: Int,
        val total_tokens: Int
    )
}

data class OpenRouterError(
    val code: Int,
    val message: String
)
```

---

### Mapping Layer Rules

| Source | Target | When | Agent |
|--------|--------|------|-------|
| `BabyResultEntity` → `BabyResult` | Domain model | Room read | Data Agent |
| `BabyResult` → `BabyResultEntity` | Room entity | Room write | Data Agent |
| `FavoriteEntity` → `FavoriteName` | Domain model | Room read | Data Agent |
| `FavoriteName` → `FavoriteEntity` | Room entity | Room write | Data Agent |
| `HistoryEntity` → `HistoryItem` | Domain model | Room read | Data Agent |
| `HistoryItem` → `HistoryEntity` | Room entity | Room write | Data Agent |
| `OpenRouterResponse` → `AIResponse` | Domain model | API success | AI Agent |
| `BabyProfile` → JSON string | String for Room | Save profile | Data Agent |
| JSON string → `BabyProfile` | Domain model | Load profile | Data Agent |

---

### Field Validation Rules

| Field | Rule |
|-------|------|
| `BabyProfile.fatherName` | Max 50 chars, letters/spaces only |
| `BabyProfile.motherName` | Max 50 chars, letters/spaces only |
| `BabyProfile.desiredMeaning` | Max 200 chars |
| `BabyProfile.maxLength` | Range 3-30 |
| `BabyProfile.uniquenessLevel` | Range 1-5 |
| `BabyProfile.traditionalLevel` | Range 1-5 |
| `NameRecommendation.name` | 1-50 chars, letters only |
| `NameRecommendation.uniquenessScore` | 1-100 |
| `NameRecommendation.meaning` | 5-500 chars |
| `NameRecommendation.philosophy` | 20-1000 chars |
| `NameRecommendation.fullNameSuggestion` | 3-200 chars |
