package com.kodemukti.namaibayi.domain.repository

import com.kodemukti.namaibayi.domain.model.AIBabyName
import com.kodemukti.namaibayi.domain.model.AIResponse
import com.kodemukti.namaibayi.domain.model.BabyProfile
import com.kodemukti.namaibayi.domain.model.GenerateRequest

interface GenerateRepository {
    suspend fun generateNames(request: GenerateRequest): Result<AIResponse>
    suspend fun generateNamesStream(request: GenerateRequest): kotlinx.coroutines.flow.Flow<Result<AIResponse>>
    suspend fun saveResults(babyProfileId: String, response: AIResponse)
    suspend fun getResultsByProfileId(babyProfileId: String): Result<AIResponse>
    suspend fun getRecommendationById(recommendationId: String): Result<AIBabyName>
}
