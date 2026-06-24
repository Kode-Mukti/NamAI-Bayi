package com.kodemukti.namaibayi.domain.usecase

import com.kodemukti.namaibayi.domain.model.AIBabyName
import com.kodemukti.namaibayi.domain.model.AIResponse
import javax.inject.Inject

class CulturalGuardrailFilter @Inject constructor() {

    private val islamKeywords = setOf("islam", "muslim", "nabi", "rasul", "quran", "hadits", "sunnah", "islamic")
    private val christianKeywords = setOf("kristen", "christian", "gereja", "injil", "yesus", "kristus", "alkitab", "catholic", "katolik")
    private val hinduBuddhaKeywords = setOf("hindu", "buddha", "dewa", "dewi", "vihara", "pura", "buddhist", "hinduism")

    private val christianOrigins = setOf("ibrani", "latin", "yunani", "welsh", "basque", "rusia")
    private val islamicOrigins = setOf("arab")
    private val hinduBuddhaOrigins = setOf("sanskerta")

    fun filter(response: AIResponse, religion: String): FilterResult {
        val rel = religion.trim().lowercase()

        if (rel.isEmpty()) {
            return FilterResult(
                recommendations = response.recommendations,
                filteredCount = 0,
                conflicts = emptyList(),
            )
        }

        val conflictingKeywords = when (rel) {
            "islam" -> christianKeywords + hinduBuddhaKeywords
            "kristen", "katolik" -> islamKeywords + hinduBuddhaKeywords
            "hindu", "buddha", "konghucu" -> islamKeywords + christianKeywords
            else -> emptySet()
        }

        val conflictingOrigins = when (rel) {
            "islam" -> setOf("ibrani", "welsh", "basque", "rusia")
            "kristen", "katolik" -> islamicOrigins
            "hindu", "buddha", "konghucu" -> islamicOrigins
            else -> emptySet()
        }

        val allowed = mutableListOf<AIBabyName>()
        val conflicts = mutableListOf<String>()

        for (name in response.recommendations) {
            val lowerContext = name.culturalContext.lowercase()
            val lowerOrigin = name.origin.lowercase()

            val hasConflict = conflictingKeywords.any { lowerContext.contains(it) } ||
                conflictingOrigins.any { lowerOrigin.contains(it) }

            if (hasConflict) {
                conflicts.add("${name.name}: origin=${name.origin}, context contains conflicting religion keywords")
            } else {
                allowed.add(name)
            }
        }

        return FilterResult(
            recommendations = allowed,
            filteredCount = response.recommendations.size - allowed.size,
            conflicts = conflicts,
        )
    }

    data class FilterResult(
        val recommendations: List<AIBabyName>,
        val filteredCount: Int,
        val conflicts: List<String>,
    )
}
