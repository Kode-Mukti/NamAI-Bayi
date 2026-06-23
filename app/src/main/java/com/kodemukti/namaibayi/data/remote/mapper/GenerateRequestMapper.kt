package com.kodemukti.namaibayi.data.remote.mapper

import com.kodemukti.namaibayi.data.remote.dto.GenerateRequestDto
import com.kodemukti.namaibayi.domain.model.GenerateRequest
import com.kodemukti.namaibayi.domain.model.LanguagePreference
import javax.inject.Inject

class GenerateRequestMapper @Inject constructor() {

    fun map(request: GenerateRequest): GenerateRequestDto {
        return GenerateRequestDto(
            babyName = request.babyProfile.name,
            gender = request.babyProfile.gender.name.lowercase(),
            preferences = request.additionalPreferences,
            strategy = request.strategy?.name?.lowercase(),
            language = mapLanguage(request.babyProfile.preferredLanguage),
        )
    }

    private fun mapLanguage(preference: LanguagePreference): String = when (preference) {
        LanguagePreference.INDONESIAN -> "id"
        LanguagePreference.ENGLISH -> "en"
        LanguagePreference.ARABIC -> "ar"
        LanguagePreference.JAVANESE -> "jv"
        LanguagePreference.SUNDANESE -> "su"
    }
}
