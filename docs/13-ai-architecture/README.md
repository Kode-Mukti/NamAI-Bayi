# 13 — AI Architecture

## Summary

Provider-agnostic AI architecture that dynamically selects naming strategies, generates candidates, evaluates quality, and produces structured, explainable recommendations.

## Reasoning

The AI must behave like an expert consultant, not a random generator. This requires a multi-stage pipeline: understand → analyze → strategize → generate → evaluate → rank → explain. Each stage is independently testable and improvable.

## Recommendation

### AI Pipeline

```
┌────────────────────────────────────────────────────┐
│                    Stage 1: Understand              │
│  Parse user preferences from input form            │
│  Extract: religion, culture, meaning, style, etc.  │
└────────────────────┬───────────────────────────────┘
                     │
┌────────────────────┴───────────────────────────────┐
│                    Stage 2: Analyze                 │
│  Identify key patterns in user preferences          │
│  Detect conflicting preferences                     │
│  Determine priority dimensions                      │
└────────────────────┬───────────────────────────────┘
                     │
┌────────────────────┴───────────────────────────────┐
│                    Stage 3: Strategize              │
│  Select optimal naming strategy(ies)                │
│  Possible: Islamic, Modern, Traditional, Javanese,  │
│  Sundanese, Balinese, Sanskrit, International,      │
│  Arabic, Korean inspired, Japanese inspired,        │
│  Parent combination, AI-generated                   │
└────────────────────┬───────────────────────────────┘
                     │
┌────────────────────┴───────────────────────────────┐
│                    Stage 4: Generate                │
│  Generate 20-30 candidate names                    │
│  Apply selected strategy constraints                │
│  Consider: meaning, sound, cultural fit, length     │
└────────────────────┬───────────────────────────────┘
                     │
┌────────────────────┴───────────────────────────────┐
│                    Stage 5: Evaluate                │
│  Score each candidate on:                          │
│  - Cultural relevance                              │
│  - Meaning alignment                               │
│  - Uniqueness                                      │
│  - Pronunciation ease                              │
│  - International readability                       │
│  - Sibling compatibility                           │
│  - Popularity                                      │
└────────────────────┬───────────────────────────────┘
                     │
┌────────────────────┴───────────────────────────────┐
│                    Stage 6: Rank                    │
│  Rank candidates by composite score                │
│  Select top 5 recommendations                      │
│  Ensure diversity in style/origin                  │
└────────────────────┬───────────────────────────────┘
                     │
┌────────────────────┴───────────────────────────────┐
│                    Stage 7: Explain                 │
│  For each top name, generate:                      │
│  - Meaning (Bahasa Indonesia)                      │
│  - Philosophy behind the name                      │
│  - Why it matches user preferences                 │
│  - Cultural context                                 │
│  - Practical considerations                        │
└────────────────────────────────────────────────────┘
```

### Strategy Selection Logic

```
function selectStrategy(preferences: UserPreferences): NamingStrategy[] {
  const strategies: NamingStrategy[] = [];

  // Religion-based strategies
  if (preferences.religion === 'Islam') strategies.push('Islamic', 'Arabic');
  if (preferences.religion === 'Hindu') strategies.push('Sanskrit', 'Balinese');
  if (preferences.religion === 'Kristen' || preferences.religion === 'Katolik')
    strategies.push('International', 'Modern');

  // Culture-based strategies
  if (preferences.culture === 'Jawa') strategies.push('Javanese', 'Traditional');
  if (preferences.culture === 'Sunda') strategies.push('Sundanese');
  if (preferences.culture === 'Bali') strategies.push('Balinese', 'Sanskrit');
  if (preferences.culture === 'Tionghoa') strategies.push('International', 'Modern');

  // Preference-based
  if (preferences.uniqueLevel >= 4) strategies.push('AI-generated');
  if (preferences.traditionalLevel >= 4) strategies.push('Traditional');
  if (preferences.traditionalLevel <= 2) strategies.push('Modern');

  // Name combination
  if (preferences.fatherName && preferences.motherName)
    strategies.push('Parent combination');

  // Dynamic: let AI choose if user didn't specify
  if (strategies.length === 0) {
    strategies.push('AI-selected'); // AI decides best strategy
  }

  return strategies.slice(0, 3); // Max 3 strategies per consultation
}
```

### Quality Evaluation Scoring

```
interface QualityScore {
  culturalRelevance: number;     // 0-100
  meaningAlignment: number;      // 0-100
  uniqueness: number;            // 0-100
  pronunciationEase: number;     // 0-100
  internationalReadability: number; // 0-100
  siblingCompatibility: number;  // 0-100
  popularity: number;            // 0-100 (lower = more unique)
  aestheticBeauty: number;       // 0-100
  religiousAppropriateness: number; // 0-100
}

function calculateCompositeScore(scores: QualityScore): number {
  const weights = {
    culturalRelevance: 0.20,
    meaningAlignment: 0.20,
    uniqueness: 0.15,
    pronunciationEase: 0.10,
    internationalReadability: 0.10,
    siblingCompatibility: 0.05,
    popularity: 0.05,
    aestheticBeauty: 0.10,
    religiousAppropriateness: 0.05,
  };

  return Object.entries(weights)
    .reduce((sum, [key, weight]) => sum + scores[key] * weight, 0);
}
```

### Provider Abstraction

```
interface AIProvider {
  generate(prefs: UserPreferences): Promise<RawAIResponse>;
  validate(response: RawAIResponse): boolean;
  healthCheck(): Promise<boolean>;
}

class GeminiProvider implements AIProvider {
  async generate(prefs: UserPreferences): Promise<RawAIResponse> {
    const prompt = buildPrompt(prefs);  // See Prompt Architecture
    const response = await geminiModel.generateContent(prompt);
    return parseResponse(response);
  }
}

class OpenAIProvider implements AIProvider {
  async generate(prefs: UserPreferences): Promise<RawAIResponse> {
    const prompt = buildPrompt(prefs);
    const response = await openai.chat.completions.create({
      model: 'gpt-4',
      messages: [{ role: 'user', content: prompt }],
      response_format: { type: 'json_object' },
    });
    return parseResponse(response);
  }
}
```

### AI Cost Management

| Provider | Cost per 1K tokens | Est. cost per consultation |
|----------|-------------------|---------------------------|
| Gemini 1.5 Flash | $0.000075 (input) / $0.0003 (output) | ~$0.005 |
| Gemini 1.5 Pro | $0.00125 (input) / $0.005 (output) | ~$0.08 |
| GPT-4o | $0.0025 (input) / $0.01 (output) | ~$0.15 |
| GPT-4o Mini | $0.00015 (input) / $0.0006 (output) | ~$0.01 |

## Implementation

- Default to Gemini (best cost-quality balance for Indonesia context)
- Implement strategy selection as deterministic logic (not AI-decided)
- Quality scoring first as AI-generated, later as algorithmic
- Cache identical inputs to reduce cost
- Monitor cost per consultation daily

## Future Improvement

- Fine-tune a model on Indonesian naming data
- Implement caching for common queries
- Use smaller model for strategy selection
- Add human-in-the-loop for quality assurance
- Train custom ranking model from user feedback data
