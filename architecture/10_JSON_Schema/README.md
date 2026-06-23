# 10 — JSON Schema

## Summary

Strict JSON schema that the AI must follow. Validated by the AI/Prompt Agent. Every response is checked against this schema before being used.

## Reasoning

The AI must output structured JSON for parseability. Without a strict schema, parsing fails silently, features break, and user experience degrades. This schema is the contract between the AI and the app.

## Recommendation

---

### AI Output Schema (JSON)

```json
{
  "$schema": "http://json-schema.org/draft-07/schema#",
  "title": "NamAI Bayi AI Response",
  "description": "Structured response from AI naming consultant",
  "type": "object",
  "required": [
    "strategy",
    "strategy_description",
    "recommendations"
  ],
  "properties": {
    "strategy": {
      "type": "string",
      "description": "Naming strategy used",
      "enum": [
        "Islami", "Modern", "Tradisional", "Jawa", "Sunda",
        "Bali", "Sansekerta", "Internasional", "Arab",
        "Korea-Inspired", "Jepang-Inspired",
        "Kombinasi Orang Tua", "AI-Generated", "Campuran"
      ]
    },
    "strategy_description": {
      "type": "string",
      "description": "Why this strategy was selected",
      "minLength": 10,
      "maxLength": 500
    },
    "recommendations": {
      "type": "array",
      "description": "Top 5 name recommendations",
      "minItems": 5,
      "maxItems": 5,
      "items": {
        "$ref": "#/definitions/NameRecommendation"
      }
    }
  },
  "definitions": {
    "NameRecommendation": {
      "type": "object",
      "required": [
        "name",
        "meaning",
        "philosophy",
        "pronunciation",
        "nicknames",
        "origin",
        "cultural_relevance",
        "uniqueness_score",
        "uniqueness_label",
        "popularity_estimate",
        "international_readability",
        "full_name_suggestion",
        "sibling_compatibility",
        "why_ai_recommends",
        "variations"
      ],
      "properties": {
        "name": {
          "type": "string",
          "description": "The recommended baby name",
          "minLength": 1,
          "maxLength": 50,
          "examples": ["Arumi", "Pramudya", "Kiranadewi"]
        },
        "meaning": {
          "type": "string",
          "description": "Meaning in Bahasa Indonesia",
          "minLength": 5,
          "maxLength": 300,
          "examples": ["Mutiara laut yang indah dan bercahaya"]
        },
        "philosophy": {
          "type": "string",
          "description": "Deep philosophical meaning",
          "minLength": 50,
          "maxLength": 1000,
          "examples": ["Nama ini melambangkan keindahan yang lahir dari ketekunan, seperti mutiara yang terbentuk di kedalaman laut..." ]
        },
        "pronunciation": {
          "type": "string",
          "description": "Pronunciation guide",
          "minLength": 3,
          "maxLength": 100,
          "examples": ["A-roo-mee"]
        },
        "nicknames": {
          "type": "array",
          "description": "Common nickname options",
          "minItems": 1,
          "maxItems": 5,
          "items": {
            "type": "string",
            "minLength": 1,
            "maxLength": 30
          },
          "examples": [["Arum", "Rumi", "Muti"]]
        },
        "origin": {
          "type": "string",
          "description": "Language/culture of origin",
          "minLength": 2,
          "maxLength": 100,
          "examples": ["Sansekerta", "Jawa Kuno", "Arab"]
        },
        "cultural_relevance": {
          "type": "string",
          "description": "Cultural context",
          "minLength": 10,
          "maxLength": 500
        },
        "uniqueness_score": {
          "type": "integer",
          "description": "Uniqueness score 1-100",
          "minimum": 1,
          "maximum": 100
        },
        "uniqueness_label": {
          "type": "string",
          "description": "Human-readable label",
          "enum": ["Sangat Unik", "Unik", "Cukup Unik", "Populer", "Sangat Populer"]
        },
        "popularity_estimate": {
          "type": "string",
          "description": "Popularity level",
          "enum": ["Sangat Populer", "Populer", "Cukup Populer", "Jarang", "Sangat Jarang"]
        },
        "international_readability": {
          "type": "string",
          "description": "How easy for non-Indonesian speakers",
          "enum": ["Mudah", "Sedang", "Sulit"]
        },
        "full_name_suggestion": {
          "type": "string",
          "description": "Full name combination",
          "minLength": 3,
          "maxLength": 200,
          "examples": ["Arumi Pramudya Putri"]
        },
        "sibling_compatibility": {
          "type": "string",
          "description": "How it pairs with siblings",
          "minLength": 10,
          "maxLength": 500
        },
        "why_ai_recommends": {
          "type": "string",
          "description": "AI reasoning for this recommendation",
          "minLength": 50,
          "maxLength": 1000
        },
        "variations": {
          "type": "array",
          "description": "Spelling/style variations",
          "minItems": 0,
          "maxItems": 5,
          "items": {
            "type": "string",
            "minLength": 1
          },
          "examples": [["Arumi", "Arumy", "Aroomi"]]
        }
      }
    }
  }
}
```

---

### Kotlin Parsing Model (in data/remote/dto/)

```kotlin
@Serializable
data class AIOutputDto(
    val strategy: String,
    val strategy_description: String,
    val recommendations: List<NameRecommendationDto>
)

@Serializable
data class NameRecommendationDto(
    val name: String,
    val meaning: String,
    val philosophy: String,
    val pronunciation: String,
    val nicknames: List<String>,
    val origin: String,
    val cultural_relevance: String,
    val uniqueness_score: Int,
    val uniqueness_label: String,
    val popularity_estimate: String,
    val international_readability: String,
    val full_name_suggestion: String,
    val sibling_compatibility: String,
    val why_ai_recommends: String,
    val variations: List<String>
)
```

---

### Validation Rules (Kotlin)

```kotlin
class AIResponseValidator {

    fun validate(json: String): ValidationResult {
        return try {
            val dto = Json.decodeFromString<AIOutputDto>(json)
            val errors = mutableListOf<String>()

            // Strategy validation
            if (!VALID_STRATEGIES.contains(dto.strategy)) {
                errors.add("Invalid strategy: ${dto.strategy}")
            }
            if (dto.strategy_description.length < 10) {
                errors.add("Strategy description too short")
            }

            // Recommendations validation
            if (dto.recommendations.size != 5) {
                errors.add("Must have exactly 5 recommendations, got ${dto.recommendations.size}")
            }

            dto.recommendations.forEachIndexed { i, rec ->
                if (rec.name.isBlank()) errors.add("Rekomendasi $i: nama kosong")
                if (rec.meaning.length < 5) errors.add("Rekomendasi $i: arti terlalu pendek")
                if (rec.philosophy.length < 50) errors.add("Rekomendasi $i: filosofi terlalu pendek")
                if (rec.uniqueness_score !in 1..100) errors.add("Rekomendasi $i: skor tidak valid")
                if (rec.nicknames.isEmpty()) errors.add("Rekomendasi $i: tidak ada nama panggilan")
                if (rec.why_ai_recommends.length < 50) errors.add("Rekomendasi $i: alasan terlalu pendek")
            }

            if (errors.isEmpty()) ValidationResult.Valid(parseToDomain(dto))
            else ValidationResult.Invalid(errors)

        } catch (e: Exception) {
            ValidationResult.Invalid(listOf("JSON parse error: ${e.message}"))
        }
    }

    sealed class ValidationResult {
        data class Valid(val result: BabyResult) : ValidationResult()
        data class Invalid(val errors: List<String>) : ValidationResult()
    }
}
```

---

### Uniqueness Score Mapping

| AI Score | Label | Meaning |
|----------|-------|---------|
| 1-20 | Sangat Populer | >10,000 estimated users |
| 21-40 | Populer | 5,000-10,000 |
| 41-60 | Cukup Unik | 1,000-5,000 |
| 61-80 | Unik | 100-1,000 |
| 81-100 | Sangat Unik | <100 estimated users |

---

### Error Response Schema (for AI)

```json
{
  "type": "object",
  "required": ["error"],
  "properties": {
    "error": { "type": "boolean", "const": true },
    "code": {
      "type": "string",
      "enum": [
        "INVALID_INPUT",
        "AI_TIMEOUT",
        "AI_INVALID_RESPONSE",
        "AI_CONTENT_FILTERED",
        "RATE_LIMITED",
        "PROVIDER_ERROR",
        "UNKNOWN_ERROR"
      ]
    },
    "message": { "type": "string" },
    "retryable": { "type": "boolean" }
  }
}
```

---

### Example Valid Response

```json
{
  "strategy": "Islami Modern",
  "strategy_description": "Berdasarkan preferensi agama Islam dan keinginan nama modern, strategi Islami Modern dipilih untuk memadukan nilai religius dengan gaya kontemporer.",
  "recommendations": [
    {
      "name": "Arumi",
      "meaning": "Mutiara laut yang indah dan bercahaya",
      "philosophy": "Nama Arumi berasal dari bahasa Sansekerta yang berarti mutiara. Dalam filosofi Jawa, mutiara melambangkan sesuatu yang berharga yang lahir dari proses panjang. Nama ini mewakili harapan bahwa anak akan menjadi pribadi yang berharga melalui perjalanan hidupnya...",
      "pronunciation": "A-roo-mee",
      "nicknames": ["Arum", "Rumi", "Aru"],
      "origin": "Sansekerta (Jawa)",
      "cultural_relevance": "Cocok untuk keluarga Jawa-Muslim modern. Mudah diterima di budaya Jawa dan memiliki nuansa Islami yang lembut.",
      "uniqueness_score": 85,
      "uniqueness_label": "Sangat Unik",
      "popularity_estimate": "Jarang",
      "international_readability": "Mudah",
      "full_name_suggestion": "Arumi Pramudya Putri",
      "sibling_compatibility": "Cocok dipasangkan dengan nama seperti Arjuna, Dewani, atau Pramesti yang memiliki nuansa Sanskerta serupa.",
      "why_ai_recommends": "Arumi memadukan nilai Islami dengan keindahan budaya Jawa. Nama ini unik, mudah diucapkan, memiliki makna mendalam, dan sangat sesuai dengan preferensi Anda untuk nama yang modern namun bermakna.",
      "variations": ["Arumi", "Arumy", "Aroomi"]
    }
  ]
}
```
