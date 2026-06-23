package com.kodemukti.namaibayi.data.remote.gemini

import com.google.gson.Gson
import com.kodemukti.namaibayi.domain.model.AIBabyName
import com.kodemukti.namaibayi.domain.model.AIResponse
import com.kodemukti.namaibayi.domain.model.GenerateRequest
import com.kodemukti.namaibayi.domain.repository.GenerateRepository
import java.util.concurrent.ConcurrentHashMap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeminiGenerateRepositoryImpl @Inject constructor(
    private val promptBuilder: GeminiPromptBuilder,
    @GeminiApiKey private val apiKey: String,
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

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .build()

    private val gson = Gson()
    private val jsonMediaType = "application/json".toMediaType()

    override suspend fun generateNames(request: GenerateRequest): Result<AIResponse> {
        return try {
            val prompt = promptBuilder.build(request)
            val body = GeminiRequest(
                contents = listOf(
                    Content(parts = listOf(Part(text = prompt)))
                ),
            )

            val jsonBody = gson.toJson(body).toRequestBody(jsonMediaType)
            val httpRequest = Request.Builder()
                .url("${GeminiConfig.BASE_URL}v1beta/models/${GeminiConfig.MODEL}:generateContent?key=$apiKey")
                .post(jsonBody)
                .build()

            val response = client.newCall(httpRequest).execute()
            val responseBody = response.body?.string()

            if (!response.isSuccessful || responseBody == null) {
                return Result.failure(Exception("Gemini API error: ${response.code}"))
            }

            val result = parseGeminiResponse(responseBody)
            Result.success(result)

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun generateNamesStream(request: GenerateRequest): Flow<Result<AIResponse>> = flow {
        emit(generateNames(request))
    }

    private fun parseGeminiResponse(json: String): AIResponse {
        val root = JSONObject(json)
        val candidates = root.optJSONArray("candidates")
        val text = candidates
            ?.optJSONObject(0)
            ?.optJSONObject("content")
            ?.optJSONArray("parts")
            ?.optJSONObject(0)
            ?.optString("text", "{}")
            ?: "{}"

        val jsonStr = extractJson(text)
        val namesArray = JSONObject(jsonStr).optJSONArray("recommendations") ?: JSONArray()

        val recommendations = mutableListOf<AIBabyName>()
        for (i in 0 until namesArray.length()) {
            val item = namesArray.getJSONObject(i)
            recommendations.add(
                AIBabyName(
                    name = item.optString("name", ""),
                    meaning = item.optString("meaning", ""),
                    origin = item.optString("origin", ""),
                    gender = item.optString("gender", ""),
                    pronunciationGuide = item.optString("pronunciation", ""),
                    culturalContext = item.optString("cultural_context", ""),
                    alternativeSpellings = item.optJSONArray("alternative_spellings")
                        ?.let { arr -> (0 until arr.length()).map { arr.optString(it) } }
                        ?: emptyList(),
                    popularityRank = if (item.has("popularity_rank")) item.optInt("popularity_rank") else null,
                    score = item.optDouble("score", 0.5).toFloat(),
                    strategyUsed = item.optString("strategy_used", "modern_trendy"),
                    reasoning = item.optString("reasoning", ""),
                )
            )
        }

        return AIResponse(
            recommendations = recommendations,
            modelUsed = GeminiConfig.MODEL,
            totalTokensUsed = recommendations.size,
        )
    }

    private fun extractJson(text: String): String {
        val start = text.indexOf('{')
        val end = text.lastIndexOf('}')
        return if (start >= 0 && end > start) text.substring(start, end + 1) else "{}"
    }
}
