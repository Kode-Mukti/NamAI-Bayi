package com.kodemukti.namaibayi.data.remote.mapper

import com.kodemukti.namaibayi.data.remote.dto.GenerateResponseDto
import com.kodemukti.namaibayi.data.remote.dto.NameDto
import com.kodemukti.namaibayi.domain.model.BabyProfile
import com.kodemukti.namaibayi.domain.model.Gender
import com.kodemukti.namaibayi.domain.model.GenerateRequest
import com.kodemukti.namaibayi.domain.model.LanguagePreference
import com.kodemukti.namaibayi.domain.model.NamingStrategy
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class MapperTest {

    private val requestMapper = GenerateRequestMapper()
    private val responseMapper = AIResponseMapper()

    @Test
    fun `map generate request to dto`() {
        val request = GenerateRequest(
            babyProfile = BabyProfile("1", "Arkana", Gender.MALE, null),
            strategy = NamingStrategy.MODERN_TRENDY,
            additionalPreferences = listOf("pendek"),
        )

        val dto = requestMapper.map(request)

        assertEquals("Arkana", dto.babyName)
        assertEquals("male", dto.gender)
        assertEquals("modern_trendy", dto.strategy)
        assertEquals("id", dto.language)
    }

    @Test
    fun `map language preference to dto`() {
        val english = GenerateRequest(
            babyProfile = BabyProfile("1", "Aisyah", Gender.FEMALE, null, preferredLanguage = LanguagePreference.ENGLISH),
        )
        val arabic = GenerateRequest(
            babyProfile = BabyProfile("2", "Muhammad", Gender.MALE, null, preferredLanguage = LanguagePreference.ARABIC),
        )

        assertEquals("en", requestMapper.map(english).language)
        assertEquals("ar", requestMapper.map(arabic).language)
    }

    @Test
    fun `map response dto to domain`() {
        val dto = GenerateResponseDto(
            recommendations = listOf(
                NameDto(
                    name = "Arkana",
                    meaning = "Cahaya",
                    origin = "Sanskerta",
                    pronunciationGuide = "Ar-ka-na",
                    culturalContext = "Budaya Indonesia",
                    alternativeSpellings = listOf("Arqana"),
                    popularityRank = 45,
                    score = 0.95f,
                    strategyUsed = "modern_trendy",
                    reasoning = "Nama yang modern dan bermakna",
                )
            ),
            modelUsed = "gpt-4",
            totalTokensUsed = 150,
        )

        val result = responseMapper.map(dto)

        assertEquals(1, result.recommendations.size)
        val name = result.recommendations.first()
        assertEquals("Arkana", name.name)
        assertEquals("Cahaya", name.meaning)
        assertEquals("gpt-4", result.modelUsed)
        assertEquals(150, result.totalTokensUsed)
    }

    @Test
    fun `map empty response dto`() {
        val dto = GenerateResponseDto(emptyList(), "gpt-4", 0)
        val result = responseMapper.map(dto)

        assertNotNull(result)
        assertEquals(0, result.recommendations.size)
    }
}
