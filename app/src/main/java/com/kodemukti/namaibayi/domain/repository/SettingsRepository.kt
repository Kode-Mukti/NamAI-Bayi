package com.kodemukti.namaibayi.domain.repository

import com.kodemukti.namaibayi.domain.model.Settings
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    fun getSettings(): Flow<Settings>
    suspend fun updateSettings(settings: Settings)
    suspend fun resetSettings()
}
