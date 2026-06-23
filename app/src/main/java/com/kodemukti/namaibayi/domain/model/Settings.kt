package com.kodemukti.namaibayi.domain.model

data class Settings(
    val preferredLanguage: LanguagePreference = LanguagePreference.INDONESIAN,
    val isDarkMode: Boolean? = null,
    val isSoundEnabled: Boolean = true,
    val isNotificationEnabled: Boolean = true,
    val aiTemperature: Float = 0.7f,
    val maxRecommendations: Int = 10,
)
