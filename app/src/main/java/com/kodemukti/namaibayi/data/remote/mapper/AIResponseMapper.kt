package com.kodemukti.namaibayi.data.remote.mapper

import com.kodemukti.namaibayi.data.remote.dto.GenerateResponseDto
import com.kodemukti.namaibayi.data.remote.dto.NameDto
import com.kodemukti.namaibayi.domain.model.AIBabyName
import com.kodemukti.namaibayi.domain.model.AIResponse
import javax.inject.Inject

class AIResponseMapper @Inject constructor() {

    fun map(dto: GenerateResponseDto): AIResponse {
        return AIResponse(
            recommendations = dto.recommendations.map { it.toDomain() },
            modelUsed = dto.modelUsed,
            totalTokensUsed = dto.totalTokensUsed,
        )
    }

    private fun NameDto.toDomain(): AIBabyName {
        return AIBabyName(
            name = name,
            meaning = meaning,
            origin = origin,
            gender = gender,
            pronunciationGuide = pronunciationGuide,
            culturalContext = culturalContext,
            alternativeSpellings = alternativeSpellings,
            popularityRank = popularityRank,
            score = score,
            strategyUsed = strategyUsed,
            reasoning = reasoning,
        )
    }
}
