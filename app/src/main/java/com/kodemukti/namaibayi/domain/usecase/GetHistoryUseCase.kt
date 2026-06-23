package com.kodemukti.namaibayi.domain.usecase

import com.kodemukti.namaibayi.domain.model.HistoryItem
import com.kodemukti.namaibayi.domain.repository.HistoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetHistoryUseCase @Inject constructor(
    private val repository: HistoryRepository,
) {
    operator fun invoke(): Flow<List<HistoryItem>> {
        return repository.getAllHistory()
    }
}
