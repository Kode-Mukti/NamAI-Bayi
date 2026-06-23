# 16 — JSON Output Schema

## Summary

Strict JSON schema for all AI responses. Ensures parseability, type safety, and consistent rendering across Android and Web platforms.

## Reasoning

Unstructured AI output is the #1 cause of rendering bugs. A strict schema with validation ensures the app never crashes due to unexpected AI responses. Every field has defined types, constraints, and fallback values.

## Recommendation

### Main Response Schema

```json
{
  "$schema": "http://json-schema.org/draft-07/schema#",
  "title": "NamAI Bayi Response",
  "type": "object",
  "required": ["strategy", "strategyDescription", "recommendations"],
  "properties": {
    "strategy": {
      "type": "string",
      "description": "Naming strategy selected",
      "enum": [
        "Islami",
        "Modern",
        "Tradisional",
        "Jawa",
        "Sunda",
        "Bali",
        "Sansekerta",
        "Internasional",
        "Arab",
        "Korea-Inspired",
        "Jepang-Inspired",
        "Kombinasi Orang Tua",
        "AI-Generated",
        "Campuran"
      ]
    },
    "strategyDescription": {
      "type": "string",
      "description": "Explanation of why this strategy was selected",
      "minLength": 10,
      "maxLength": 500
    },
    "recommendations": {
      "type": "array",
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
        "name", "meaning", "philosophy", "pronunciation",
        "nickname", "origin", "culturalRelevance",
        "uniquenessScore", "popularityEstimate",
        "internationalReadability", "fullNameSuggestion",
        "siblingCompatibility", "whyAIRecommends"
      ],
      "properties": {
        "name": {
          "type": "string",
          "description": "The recommended name",
          "minLength": 1,
          "maxLength": 50,
          "examples": ["Arumi", "Pramudya", "Kiranadewi"]
        },
        "meaning": {
          "type": "string",
          "description": "Meaning of the name in Bahasa Indonesia",
          "minLength": 5,
          "maxLength": 200,
          "examples": ["Mutiara lautan yang indah", "Kesatria yang bijaksana"]
        },
        "philosophy": {
          "type": "string",
          "description": "Deep philosophical explanation",
          "minLength": 20,
          "maxLength": 500
        },
        "pronunciation": {
          "type": "string",
          "description": "Pronunciation guide",
          "minLength": 3,
          "maxLength": 100,
          "examples": ["A-roo-mee", "Pra-moo-dya"]
        },
        "nickname": {
          "type": "string",
          "description": "Common nickname options",
          "minLength": 1,
          "maxLength": 100,
          "examples": ["Arum", "Rumi", "Muti"]
        },
        "origin": {
          "type": "string",
          "description": "Language/culture of origin",
          "minLength": 2,
          "maxLength": 100,
          "examples": ["Sansekerta", "Jawa Kuno", "Arab"]
        },
        "culturalRelevance": {
          "type": "string",
          "description": "Cultural context and relevance",
          "minLength": 10,
          "maxLength": 300
        },
        "uniquenessScore": {
          "type": "integer",
          "description": "Uniqueness score 1-100",
          "minimum": 1,
          "maximum": 100
        },
        "uniquenessLabel": {
          "type": "string",
          "description": "Human-readable uniqueness label",
          "enum": ["Sangat Unik", "Unik", "Cukup Unik", "Populer", "Sangat Populer"]
        },
        "popularityEstimate": {
          "type": "string",
          "description": "Popularity estimate",
          "enum": ["Sangat Populer", "Populer", "Cukup Populer", "Jarang", "Sangat Jarang"]
        },
        "internationalReadability": {
          "type": "string",
          "description": "How easy for non-Indonesian speakers to pronounce",
          "enum": ["Mudah", "Sedang", "Sulit"]
        },
        "fullNameSuggestion": {
          "type": "string",
          "description": "Complete name suggestion (first + middle + last)",
          "minLength": 3,
          "maxLength": 200,
          "examples": ["Arumi Pramudya Putri", "Kiranadewi Safira Zahra"]
        },
        "siblingCompatibility": {
          "type": "string",
          "description": "How this name pairs with potential sibling names",
          "minLength": 10,
          "maxLength": 300
        },
        "whyAIRecommends": {
          "type": "string",
          "description": "AI's reasoning for this recommendation",
          "minLength": 20,
          "maxLength": 500
        }
      }
    }
  }
}
```

### TypeScript Types (for Web)

```typescript
interface AIResponse {
  strategy: NamingStrategy;
  strategyDescription: string;
  recommendations: NameRecommendation[];
}

type NamingStrategy =
  | 'Islami' | 'Modern' | 'Tradisional' | 'Jawa' | 'Sunda'
  | 'Bali' | 'Sansekerta' | 'Internasional' | 'Arab'
  | 'Korea-Inspired' | 'Jepang-Inspired' | 'Kombinasi Orang Tua'
  | 'AI-Generated' | 'Campuran';

interface NameRecommendation {
  name: string;
  meaning: string;
  philosophy: string;
  pronunciation: string;
  nickname: string;
  origin: string;
  culturalRelevance: string;
  uniquenessScore: number;       // 1-100
  uniquenessLabel: UniquenessLabel;
  popularityEstimate: PopularityLevel;
  internationalReadability: ReadabilityLevel;
  fullNameSuggestion: string;
  siblingCompatibility: string;
  whyAIRecommends: string;
}

type UniquenessLabel =
  | 'Sangat Unik' | 'Unik' | 'Cukup Unik' | 'Populer' | 'Sangat Populer';

type PopularityLevel =
  | 'Sangat Populer' | 'Populer' | 'Cukup Populer' | 'Jarang' | 'Sangat Jarang';

type ReadabilityLevel = 'Mudah' | 'Sedang' | 'Sulit';
```

### Kotlin Data Classes (for Android)

```kotlin
data class AIResponse(
    val strategy: NamingStrategy,
    val strategyDescription: String,
    val recommendations: List<NameRecommendation>
)

enum class NamingStrategy {
    ISLAMI, MODERN, TRADISIONAL, JAWA, SUNDA,
    BALI, SANSEKERTA, INTERNASIONAL, ARAB,
    KOREA_INSPIRED, JEPANG_INSPIRED, KOMBINASI_ORANG_TUA,
    AI_GENERATED, CAMPURAN
}

data class NameRecommendation(
    val name: String,
    val meaning: String,
    val philosophy: String,
    val pronunciation: String,
    val nickname: String,
    val origin: String,
    val culturalRelevance: String,
    val uniquenessScore: Int,
    val uniquenessLabel: UniquenessLabel,
    val popularityEstimate: PopularityLevel,
    val internationalReadability: ReadabilityLevel,
    val fullNameSuggestion: String,
    val siblingCompatibility: String,
    val whyAIRecommends: String
)

enum class UniquenessLabel {
    SANGAT_UNIK, UNIK, CUKUP_UNIK, POPULER, SANGAT_POPULER
}

enum class PopularityLevel {
    SANGAT_POPULER, POPULER, CUKUP_POPULER, JARANG, SANGAT_JARANG
}

enum class ReadabilityLevel {
    MUDAH, SEDANG, SULIT
}
```

### Validation Rules

```javascript
function validateAIResponse(response: any): ValidationResult {
  const errors = [];

  if (!response || typeof response !== 'object')
    return { valid: false, errors: ['Response is not an object'] };

  if (!response.strategy)
    errors.push('Missing strategy');

  if (!response.strategyDescription || response.strategyDescription.length < 10)
    errors.push('Strategy description too short or missing');

  if (!response.recommendations || response.recommendations.length !== 5)
    errors.push('Must have exactly 5 recommendations');

  for (let i = 0; i < response.recommendations.length; i++) {
    const rec = response.recommendations[i];
    if (!rec.name) errors.push(`Recommendation ${i}: missing name`);
    if (!rec.meaning || rec.meaning.length < 5)
      errors.push(`Recommendation ${i}: meaning too short`);
    if (rec.uniquenessScore < 1 || rec.uniquenessScore > 100)
      errors.push(`Recommendation ${i}: invalid uniqueness score`);
    // ... validate all fields
  }

  return { valid: errors.length === 0, errors };
}
```

### Error Response Schema

```json
{
  "type": "object",
  "required": ["error", "code"],
  "properties": {
    "error": {
      "type": "boolean",
      "const": true
    },
    "code": {
      "type": "string",
      "enum": [
        "INVALID_INPUT",
        "AI_TIMEOUT",
        "AI_INVALID_RESPONSE",
        "RATE_LIMITED",
        "PROVIDER_ERROR",
        "UNKNOWN_ERROR"
      ]
    },
    "message": {
      "type": "string",
      "description": "User-friendly error message in Bahasa Indonesia"
    },
    "retryable": {
      "type": "boolean",
      "description": "Whether the user can retry"
    }
  }
}
```

## Implementation

- Validate AI response against schema before rendering
- Implement fallback UI for missing fields
- Log all validation errors for prompt improvement
- Version schema (v1, v2, etc.) with migration support

## Future Improvement

- Add response compression for large payloads
- Streaming response support (SSE)
- Schema evolution with backward compatibility
- Automated schema conformance testing
- Response quality scoring
