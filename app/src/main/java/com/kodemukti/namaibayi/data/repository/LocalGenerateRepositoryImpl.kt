package com.kodemukti.namaibayi.data.repository

import com.kodemukti.namaibayi.data.local.source.NameDataProvider
import com.kodemukti.namaibayi.domain.model.AIBabyName
import com.kodemukti.namaibayi.domain.model.AIResponse
import com.kodemukti.namaibayi.domain.model.GenerateRequest
import com.kodemukti.namaibayi.domain.repository.GenerateRepository
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalGenerateRepositoryImpl @Inject constructor() : GenerateRepository {

    private val resultsCache = ConcurrentHashMap<String, AIResponse>()

    override suspend fun saveResults(babyProfileId: String, response: AIResponse) {
        resultsCache[babyProfileId] = response
    }

    override suspend fun getResultsByProfileId(babyProfileId: String): Result<AIResponse> {
        val response = resultsCache[babyProfileId]
        return if (response != null) {
            Result.success(response)
        } else {
            Result.failure(Exception("Hasil tidak ditemukan untuk konsultasi ini"))
        }
    }

    override suspend fun getRecommendationById(recommendationId: String): Result<AIBabyName> {
        val found = resultsCache.values.firstOrNull { response ->
            response.recommendations.any { it.id == recommendationId }
        }?.recommendations?.firstOrNull { it.id == recommendationId }
        return if (found != null) {
            Result.success(found)
        } else {
            Result.failure(Exception("Rekomendasi tidak ditemukan"))
        }
    }

    override suspend fun generateNames(request: GenerateRequest): Result<AIResponse> {
        delay(800)

        val gender = when (request.babyProfile.gender) {
            com.kodemukti.namaibayi.domain.model.Gender.MALE -> "Laki-laki"
            com.kodemukti.namaibayi.domain.model.Gender.FEMALE -> "Perempuan"
            com.kodemukti.namaibayi.domain.model.Gender.NEUTRAL -> "neutral"
        }

        val strategy = request.strategy?.name?.lowercase()
        val matches = NameDataProvider.recommend(gender, strategy, count = 10)
            .filter { it.name.contains(request.babyProfile.name, ignoreCase = true).not() }
            .take(5)

        val babyNames = matches.map { it.toDomain() }

        val response = AIResponse(
            recommendations = babyNames,
            modelUsed = "local:v1",
            totalTokensUsed = babyNames.size,
        )

        return Result.success(response)
    }

    override suspend fun generateNamesStream(request: GenerateRequest): Flow<Result<AIResponse>> = flow {
        emit(generateNames(request))
    }

    private fun com.kodemukti.namaibayi.data.local.source.LocalName.toDomain() = AIBabyName(
        name = name,
        meaning = meaning,
        origin = origin,
        gender = gender,
        pronunciationGuide = pronunciation,
        culturalContext = culturalContext,
        alternativeSpellings = alternativeSpellings,
        popularityRank = popularityRank,
        score = 0.8f + (0.2f * (1f - popularityRank.toFloat() / 500f)),
        strategyUsed = strategyTag,
        reasoning = "Nama $name berasal dari $origin yang berarti \"$meaning\". $culturalContext",
    )
}
