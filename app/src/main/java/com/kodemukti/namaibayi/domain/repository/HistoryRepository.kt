package com.kodemukti.namaibayi.domain.repository

import com.kodemukti.namaibayi.domain.model.HistoryItem
import kotlinx.coroutines.flow.Flow

interface HistoryRepository {
    fun getAllHistory(): Flow<List<HistoryItem>>
    suspend fun addToHistory(item: HistoryItem)
    suspend fun deleteFromHistory(id: String)
    suspend fun clearHistory()
}
