package com.kodemukti.namaibayi.domain.model

data class BabyProfile(
    val id: String,
    val name: String,
    val gender: Gender,
    val birthDate: Long?,
    val birthOrder: Int = 1,
    val siblings: List<String> = emptyList(),
    val familyOrigins: List<String> = emptyList(),
    val preferredLanguage: LanguagePreference = LanguagePreference.INDONESIAN,
)

enum class Gender { MALE, FEMALE, NEUTRAL }

enum class LanguagePreference { INDONESIAN, ENGLISH, ARABIC, JAVANESE, SUNDANESE }
