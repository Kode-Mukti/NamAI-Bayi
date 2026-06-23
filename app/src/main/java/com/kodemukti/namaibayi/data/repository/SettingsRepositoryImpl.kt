package com.kodemukti.namaibayi.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import com.kodemukti.namaibayi.domain.model.LanguagePreference
import com.kodemukti.namaibayi.domain.model.Settings
import com.kodemukti.namaibayi.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) : SettingsRepository {

    private object Keys {
        val LANGUAGE = stringPreferencesKey("preferred_language")
        val IS_DARK_MODE = stringPreferencesKey("is_dark_mode")
        val IS_SOUND_ENABLED = booleanPreferencesKey("is_sound_enabled")
        val IS_NOTIFICATION_ENABLED = booleanPreferencesKey("is_notification_enabled")
        val AI_TEMPERATURE = floatPreferencesKey("ai_temperature")
        val MAX_RECOMMENDATIONS = intPreferencesKey("max_recommendations")
    }

    override fun getSettings(): Flow<Settings> {
        return dataStore.data.map { prefs ->
            Settings(
                preferredLanguage = LanguagePreference.valueOf(
                    prefs[Keys.LANGUAGE] ?: LanguagePreference.INDONESIAN.name
                ),
                isDarkMode = prefs[Keys.IS_DARK_MODE]?.toBooleanStrictOrNull(),
                isSoundEnabled = prefs[Keys.IS_SOUND_ENABLED] ?: true,
                isNotificationEnabled = prefs[Keys.IS_NOTIFICATION_ENABLED] ?: true,
                aiTemperature = prefs[Keys.AI_TEMPERATURE] ?: 0.7f,
                maxRecommendations = prefs[Keys.MAX_RECOMMENDATIONS] ?: 10,
            )
        }
    }

    override suspend fun updateSettings(settings: Settings) {
        dataStore.edit { prefs ->
            prefs[Keys.LANGUAGE] = settings.preferredLanguage.name
            settings.isDarkMode?.let { prefs[Keys.IS_DARK_MODE] = it.toString() }
            prefs[Keys.IS_SOUND_ENABLED] = settings.isSoundEnabled
            prefs[Keys.IS_NOTIFICATION_ENABLED] = settings.isNotificationEnabled
            prefs[Keys.AI_TEMPERATURE] = settings.aiTemperature
            prefs[Keys.MAX_RECOMMENDATIONS] = settings.maxRecommendations
        }
    }

    override suspend fun resetSettings() {
        dataStore.edit { it.clear() }
    }
}
