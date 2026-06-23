package com.kodemukti.namaibayi.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites")
data class FavoriteEntity(
    @PrimaryKey val id: String,
    val nameRecommendationId: String,
    val name: String,
    val meaning: String,
    val origin: String,
    val savedAt: Long,
    val notes: String = "",
)
