package com.kodemukti.namaibayi.domain.model

data class BabyResult(
    val id: String,
    val babyProfileId: String,
    val recommendations: List<NameRecommendation>,
    val createdAt: Long,
)
