package com.kodemukti.namaibayi.domain.model

data class GenerateRequest(
    val babyProfile: BabyProfile,
    val strategy: NamingStrategy? = null,
    val additionalPreferences: List<String> = emptyList(),
)

data class AIResponse(
    val recommendations: List<AIBabyName>,
    val modelUsed: String,
    val totalTokensUsed: Int,
)

data class AIBabyName(
    val id: String = java.util.UUID.randomUUID().toString(),
    val name: String,
    val meaning: String,
    val origin: String,
    val gender: String = "",
    val pronunciationGuide: String,
    val culturalContext: String,
    val alternativeSpellings: List<String> = emptyList(),
    val popularityRank: Int? = null,
    val score: Float,
    val strategyUsed: String,
    val reasoning: String,
)
