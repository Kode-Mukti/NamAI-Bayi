package com.kodemukti.namaibayi.domain.usecase

import com.kodemukti.namaibayi.domain.model.Settings
import com.kodemukti.namaibayi.domain.repository.SettingsRepository
import javax.inject.Inject

class UpdateSettingsUseCase @Inject constructor(
    private val repository: SettingsRepository,
) {
    suspend operator fun invoke(settings: Settings) {
        repository.updateSettings(settings)
    }
}
