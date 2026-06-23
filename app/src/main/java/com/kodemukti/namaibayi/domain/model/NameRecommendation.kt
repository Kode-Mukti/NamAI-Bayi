package com.kodemukti.namaibayi.domain.model

data class NameRecommendation(
    val id: String,
    val name: String,
    val meaning: String,
    val origin: String,
    val gender: String,
    val pronunciationGuide: String,
    val culturalContext: String,
    val alternativeSpellings: List<String>,
    val popularityRank: Int?,
    val score: Float,
    val strategyUsed: NamingStrategy,
    val reasoning: String,
)

enum class NamingStrategy {
    MODERN_TRENDY,
    TRADITIONAL_CULTURAL,
    RELIGIOUS_ISLAMIC,
    RELIGIOUS_CHRISTIAN,
    RELIGIOUS_HINDU_BUDDHA,
    NATURE_INSPIRED,
    LITERARY,
    ROYAL_NOBLE,
    SHORT_SIMPLE,
    UNIQUE_RARE,
    FAMILY_HERITAGE,
    COMBINATION,
    MEANING_BASED,
    ASTROLOGY,
}
