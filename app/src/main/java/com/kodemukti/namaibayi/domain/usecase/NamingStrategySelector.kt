package com.kodemukti.namaibayi.domain.usecase

import com.kodemukti.namaibayi.domain.model.NamingStrategy
import javax.inject.Inject

class NamingStrategySelector @Inject constructor() {

    fun select(
        religion: String,
        culture: String,
        uniquenessLevel: Float,
        hasMeaning: Boolean,
    ): NamingStrategy {
        val trimmedReligion = religion.trim()
        val trimmedCulture = culture.trim()

        if (trimmedReligion.isNotEmpty()) {
            return when (trimmedReligion) {
                "Islam" -> NamingStrategy.RELIGIOUS_ISLAMIC
                "Kristen", "Katolik" -> NamingStrategy.RELIGIOUS_CHRISTIAN
                "Hindu", "Buddha", "Konghucu" -> NamingStrategy.RELIGIOUS_HINDU_BUDDHA
                else -> fallbackByCultureOrUniqueness(trimmedCulture, uniquenessLevel, hasMeaning)
            }
        }

        if (trimmedCulture.isNotEmpty()) {
            return if (trimmedCulture == "Tionghoa") {
                NamingStrategy.COMBINATION
            } else {
                NamingStrategy.TRADITIONAL_CULTURAL
            }
        }

        if (hasMeaning) {
            return NamingStrategy.MEANING_BASED
        }

        return when {
            uniquenessLevel >= 0.66f -> NamingStrategy.UNIQUE_RARE
            uniquenessLevel <= 0.33f -> NamingStrategy.SHORT_SIMPLE
            else -> NamingStrategy.MODERN_TRENDY
        }
    }

    private fun fallbackByCultureOrUniqueness(
        culture: String,
        uniquenessLevel: Float,
        hasMeaning: Boolean,
    ): NamingStrategy {
        val trimmedCulture = culture.trim()
        if (trimmedCulture.isNotEmpty()) {
            return if (trimmedCulture == "Tionghoa") {
                NamingStrategy.COMBINATION
            } else {
                NamingStrategy.TRADITIONAL_CULTURAL
            }
        }
        if (hasMeaning) return NamingStrategy.MEANING_BASED
        return when {
            uniquenessLevel >= 0.66f -> NamingStrategy.UNIQUE_RARE
            uniquenessLevel <= 0.33f -> NamingStrategy.SHORT_SIMPLE
            else -> NamingStrategy.MODERN_TRENDY
        }
    }
}
