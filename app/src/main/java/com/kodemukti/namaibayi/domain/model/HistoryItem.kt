package com.kodemukti.namaibayi.domain.model

data class HistoryItem(
    val id: String,
    val babyName: String,
    val gender: String,
    val timestamp: Long,
    val recommendationsCount: Int,
)
