package com.kodemukti.namaibayi.data.repository

import com.kodemukti.namaibayi.data.remote.api.NamAIApi
import com.kodemukti.namaibayi.data.remote.mapper.AIResponseMapper
import com.kodemukti.namaibayi.data.remote.mapper.GenerateRequestMapper
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
class GenerateRepositoryImpl @Inject constructor(
    private val api: NamAIApi,
    private val requestMapper: GenerateRequestMapper,
    private val responseMapper: AIResponseMapper,
) : GenerateRepository {

    private val resultsCache = ConcurrentHashMap<String, AIResponse>()

    override suspend fun saveResults(babyProfileId: String, response: AIResponse) {
        resultsCache[babyProfileId] = response
    }

    override suspend fun getResultsByProfileId(babyProfileId: String): Result<AIResponse> {
        val response = resultsCache[babyProfileId]
        return if (response != null) {
            Result.success(response)
        } else {
            Result.failure(Exception("Hasil tidak ditemukan"))
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
        return try {
            val dto = requestMapper.map(request)
            val response = api.generateNames(dto)
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(responseMapper.map(body))
                } else {
                    Result.failure(Exception("Response body is null"))
                }
            } else {
                Result.failure(Exception("API error: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun generateNamesStream(request: GenerateRequest): Flow<Result<AIResponse>> = flow {
        emit(generateNames(request))
    }
}
