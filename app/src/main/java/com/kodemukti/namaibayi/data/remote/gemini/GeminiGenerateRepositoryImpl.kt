package com.kodemukti.namaibayi.data.remote.gemini

import android.util.Log
import com.google.gson.Gson
import com.kodemukti.namaibayi.domain.model.AIBabyName
import com.kodemukti.namaibayi.domain.model.AIResponse
import com.kodemukti.namaibayi.domain.model.GenerateRequest
import com.kodemukti.namaibayi.domain.repository.GenerateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GeminiGenerateRepositoryImpl @Inject constructor(
    private val promptBuilder: GeminiPromptBuilder,
    @GeminiApiKey private val apiKey: String,
    private val client: OkHttpClient,
) : GenerateRepository {

    private val resultsCache = ConcurrentHashMap<String, AIResponse>()
    private val gson = Gson()
    private val jsonMediaType = "application/json".toMediaType()

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
        val primaryResult = executeApiCall(GeminiConfig.MODEL, request)
        if (primaryResult.isSuccess) return primaryResult

        val error = primaryResult.exceptionOrNull()
        if (error is GeminiApiException && (error.code == 404 || error.code == 400)) {
            return executeApiCall(GeminiConfig.FALLBACK_MODEL, request)
        }

        return primaryResult
    }

    private fun executeApiCall(model: String, request: GenerateRequest): Result<AIResponse> {
        return try {
            val prompt = promptBuilder.build(request)
            val body = GeminiRequest(
                contents = listOf(
                    Content(parts = listOf(Part(text = prompt)))
                ),
            )

            val jsonBody = gson.toJson(body).toRequestBody(jsonMediaType)
            val httpRequest = Request.Builder()
                .url("${GeminiConfig.BASE_URL}v1beta/models/$model:generateContent?key=$apiKey")
                .post(jsonBody)
                .build()

            client.newBuilder()
                .readTimeout(60, TimeUnit.SECONDS)
                .build()
                .newCall(httpRequest)
                .execute()
                .use { response ->
                    val responseBody = response.body?.string()

                    if (!response.isSuccessful || responseBody == null) {
                        return Result.failure(GeminiApiException(response.code, "API Error for model $model: ${response.code}"))
                    }

                    val result = parseGeminiResponse(responseBody, model)
                    Result.success(result)
                }

        } catch (e: Exception) {
            Log.e(TAG, "Error generating names", e)
            Result.failure(e)
        }
    }

    override suspend fun generateNamesStream(request: GenerateRequest): Flow<Result<AIResponse>> = flow {
        emit(generateNames(request))
    }

    private fun parseGeminiResponse(json: String, modelUsed: String): AIResponse {
        val responseDto = gson.fromJson(json, GeminiResponse::class.java)
        val text = responseDto.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: "{}"

        val jsonStr = extractJson(text)
        val recommendations = parseRecommendations(jsonStr)

        return AIResponse(
            recommendations = recommendations,
            modelUsed = modelUsed,
            totalTokensUsed = responseDto.usageMetadata?.totalTokenCount ?: recommendations.size,
        )
    }

    private fun parseRecommendations(jsonStr: String): List<AIBabyName> {
        val recommendations = mutableListOf<AIBabyName>()
        try {
            val root = JSONObject(jsonStr)
            val namesArray = root.optJSONArray("recommendations") ?: JSONArray()

            for (i in 0 until namesArray.length()) {
                val item = namesArray.getJSONObject(i)
                recommendations.add(
                    AIBabyName(
                        name = item.optString("name", ""),
                        fullNameSuggestion = item.optString("fullNameSuggestion", ""),
                        meaning = item.optString("meaning", ""),
                        philosophy = item.optString("philosophy", ""),
                        nickname = item.optString("nickname", ""),
                        origin = item.optString("origin", ""),
                        gender = item.optString("gender", ""),
                        pronunciationGuide = item.optString("pronunciation", ""),
                        culturalContext = item.optString("cultural_context", ""),
                        alternativeSpellings = item.optJSONArray("alternative_spellings")
                            ?.let { arr -> (0 until arr.length()).map { arr.optString(it) } }
                            ?: emptyList(),
                        uniquenessScore = item.optInt("uniquenessScore", 0),
                        internationalReadability = item.optString("internationalReadability", ""),
                        siblingCompatibility = item.optString("siblingCompatibility", ""),
                        popularityRank = if (item.has("popularity_rank")) item.optInt("popularity_rank") else null,
                        score = item.optDouble("score", 0.5).toFloat(),
                        strategyUsed = item.optString("strategy_used", "modern_trendy"),
                        reasoning = item.optString("reasoning", ""),
                    )
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error parsing recommendations JSON", e)
        }
        return recommendations
    }

    private fun extractJson(text: String): String {
        val regex = "```json([\\s\\S]*?)```".toRegex()
        val match = regex.find(text)
        if (match != null) {
            return match.groups[1]?.value?.trim() ?: "{}"
        }

        val start = text.indexOf('{')
        val end = text.lastIndexOf('}')
        return if (start >= 0 && end > start) text.substring(start, end + 1) else "{}"
    }

    private class GeminiApiException(val code: Int, message: String) : Exception(message)

    companion object {
        private const val TAG = "GeminiRepo"
    }
}
