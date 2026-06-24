package com.kodemukti.namaibayi.data.repository

import android.util.Log
import com.kodemukti.namaibayi.data.local.source.NameDataProvider
import com.kodemukti.namaibayi.domain.model.AIBabyName
import com.kodemukti.namaibayi.domain.model.AIResponse
import com.kodemukti.namaibayi.domain.model.GenerateRequest
import com.kodemukti.namaibayi.domain.repository.GenerateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FallbackGenerateRepositoryImpl @Inject constructor(
    private val geminiRepo: com.kodemukti.namaibayi.data.remote.gemini.GeminiGenerateRepositoryImpl,
    private val localRepo: LocalGenerateRepositoryImpl,
) : GenerateRepository {

    private val resultsCache = ConcurrentHashMap<String, AIResponse>()

    override suspend fun saveResults(babyProfileId: String, response: AIResponse) {
        resultsCache[babyProfileId] = response
    }

    override suspend fun getResultsByProfileId(babyProfileId: String): Result<AIResponse> {
        val cached = resultsCache[babyProfileId]
        if (cached != null) return Result.success(cached)
        return geminiRepo.getResultsByProfileId(babyProfileId)
            .recoverCatching { localRepo.getResultsByProfileId(babyProfileId).getOrThrow() }
    }

    override suspend fun getRecommendationById(recommendationId: String): Result<AIBabyName> {
        val cached = resultsCache.values.firstOrNull { r ->
            r.recommendations.any { it.id == recommendationId }
        }?.recommendations?.firstOrNull { it.id == recommendationId }
        if (cached != null) return Result.success(cached)
        return geminiRepo.getRecommendationById(recommendationId)
            .recoverCatching { localRepo.getRecommendationById(recommendationId).getOrThrow() }
    }

    override suspend fun generateNames(request: GenerateRequest): Result<AIResponse> {
        val result = geminiRepo.generateNames(request)
        if (result.isSuccess) {
            Log.d(TAG, "Gemini API succeeded")
            return result
        }
        Log.w(TAG, "Gemini failed, falling back to local provider: ${result.exceptionOrNull()?.message}")
        return localRepo.generateNames(request)
    }

    override suspend fun generateNamesStream(request: GenerateRequest): Flow<Result<AIResponse>> = flow {
        emit(generateNames(request))
    }

    companion object {
        private const val TAG = "FallbackRepo"
    }
}
