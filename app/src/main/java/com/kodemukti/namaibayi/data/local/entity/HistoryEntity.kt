package com.kodemukti.namaibayi.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "history")
data class HistoryEntity(
    @PrimaryKey val id: String,
    val babyName: String,
    val gender: String,
    val timestamp: Long,
    val recommendationsCount: Int,
)
