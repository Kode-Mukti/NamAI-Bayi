package com.kodemukti.namaibayi.domain.model

data class FavoriteName(
    val id: String,
    val nameRecommendationId: String,
    val name: String,
    val meaning: String,
    val origin: String,
    val savedAt: Long,
    val notes: String = "",
)
