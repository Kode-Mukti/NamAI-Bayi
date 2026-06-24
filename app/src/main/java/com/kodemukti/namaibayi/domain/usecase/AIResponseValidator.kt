package com.kodemukti.namaibayi.domain.usecase

import com.kodemukti.namaibayi.domain.model.AIResponse
import javax.inject.Inject

class AIResponseValidator @Inject constructor() {

    fun validate(response: AIResponse): ValidationResult {
        val errors = mutableListOf<String>()

        if (response.recommendations.size != 5) {
            errors.add("Expected 5 recommendations, got ${response.recommendations.size}")
        }

        response.recommendations.forEachIndexed { index, name ->
            if (name.name.isBlank()) {
                errors.add("Recommendation $index: name is blank")
            }
            if (name.meaning.isBlank()) {
                errors.add("Recommendation $index (${name.name}): meaning is blank")
            }
            if (name.origin.isBlank()) {
                errors.add("Recommendation $index (${name.name}): origin is blank")
            }
            if (name.pronunciationGuide.isBlank()) {
                errors.add("Recommendation $index (${name.name}): pronunciationGuide is blank")
            }
            if (name.culturalContext.isBlank()) {
                errors.add("Recommendation $index (${name.name}): culturalContext is blank")
            }
            if (name.reasoning.isBlank()) {
                errors.add("Recommendation $index (${name.name}): reasoning is blank")
            }
            if (name.strategyUsed.isBlank()) {
                errors.add("Recommendation $index (${name.name}): strategyUsed is blank")
            }
            if (name.score < 0f || name.score > 1f) {
                errors.add("Recommendation $index (${name.name}): score ${name.score} out of range [0,1]")
            }
            if (name.uniquenessScore < 0 || name.uniquenessScore > 100) {
                errors.add("Recommendation $index (${name.name}): uniquenessScore ${name.uniquenessScore} out of range [0,100]")
            }
        }

        return if (errors.isEmpty()) {
            ValidationResult.Valid
        } else {
            ValidationResult.Invalid(errors)
        }
    }

    sealed class ValidationResult {
        data object Valid : ValidationResult()
        data class Invalid(val errors: List<String>) : ValidationResult()
    }
}
