package com.kodemukti.namaibayi.domain.usecase

import com.kodemukti.namaibayi.domain.model.HistoryItem
import com.kodemukti.namaibayi.domain.repository.HistoryRepository
import javax.inject.Inject

class SaveToHistoryUseCase @Inject constructor(
    private val repository: HistoryRepository,
) {
    suspend operator fun invoke(item: HistoryItem) {
        repository.addToHistory(item)
    }
}
