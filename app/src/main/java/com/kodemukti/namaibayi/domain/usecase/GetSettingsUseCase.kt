package com.kodemukti.namaibayi.domain.usecase

import com.kodemukti.namaibayi.domain.model.Settings
import com.kodemukti.namaibayi.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSettingsUseCase @Inject constructor(
    private val repository: SettingsRepository,
) {
    operator fun invoke(): Flow<Settings> {
        return repository.getSettings()
    }
}
