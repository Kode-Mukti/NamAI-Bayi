package com.kodemukti.namaibayi.data.remote.gemini

data class GeminiRequest(
    val contents: List<Content>,
    val generationConfig: GenerationConfig = GenerationConfig(),
)

data class Content(
    val parts: List<Part>,
)

data class Part(
    val text: String,
)

data class GenerationConfig(
    val temperature: Float = GeminiConfig.TEMPERATURE,
    val maxOutputTokens: Int = GeminiConfig.MAX_OUTPUT_TOKENS,
    val topP: Float = GeminiConfig.TOP_P,
    val thinkingConfig: ThinkingConfig? = ThinkingConfig(),
)

data class ThinkingConfig(
    val includeThinkingProcess: Boolean = true,
    val thinkingLevel: String = "MINIMAL",
)

data class GeminiResponse(
    val candidates: List<Candidate>? = null,
)

data class Candidate(
    val content: Content? = null,
    val finishReason: String? = null,
)
