package com.kodemukti.namaibayi.data.remote.dto

import com.google.gson.annotations.SerializedName

data class GenerateResponseDto(
    @SerializedName("recommendations") val recommendations: List<NameDto>,
    @SerializedName("model_used") val modelUsed: String,
    @SerializedName("total_tokens") val totalTokensUsed: Int,
)

data class NameDto(
    @SerializedName("name") val name: String,
    @SerializedName("meaning") val meaning: String,
    @SerializedName("origin") val origin: String,
    @SerializedName("gender") val gender: String = "",
    @SerializedName("pronunciation") val pronunciationGuide: String,
    @SerializedName("cultural_context") val culturalContext: String,
    @SerializedName("alternative_spellings") val alternativeSpellings: List<String>,
    @SerializedName("popularity_rank") val popularityRank: Int?,
    @SerializedName("score") val score: Float,
    @SerializedName("strategy") val strategyUsed: String,
    @SerializedName("reasoning") val reasoning: String,
)
