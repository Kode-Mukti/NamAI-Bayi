package com.kodemukti.namaibayi.data.repository

import com.kodemukti.namaibayi.data.local.dao.HistoryDao
import com.kodemukti.namaibayi.data.local.entity.HistoryEntity
import com.kodemukti.namaibayi.domain.model.HistoryItem
import com.kodemukti.namaibayi.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HistoryRepositoryImpl @Inject constructor(
    private val historyDao: HistoryDao,
) : HistoryRepository {

    override fun getAllHistory(): Flow<List<HistoryItem>> {
        return historyDao.getAllHistory().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun addToHistory(item: HistoryItem) {
        historyDao.insert(item.toEntity())
    }

    override suspend fun deleteFromHistory(id: String) {
        historyDao.deleteById(id)
    }

    override suspend fun clearHistory() {
        historyDao.clearAll()
    }

    private fun HistoryItem.toEntity() = HistoryEntity(
        id = id,
        babyName = babyName,
        gender = gender,
        timestamp = timestamp,
        recommendationsCount = recommendationsCount,
    )

    private fun HistoryEntity.toDomain() = HistoryItem(
        id = id,
        babyName = babyName,
        gender = gender,
        timestamp = timestamp,
        recommendationsCount = recommendationsCount,
    )
}
