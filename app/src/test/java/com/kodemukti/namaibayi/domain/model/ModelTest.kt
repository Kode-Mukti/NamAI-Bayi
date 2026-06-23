package com.kodemukti.namaibayi.domain.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

class BabyProfileTest {

    @Test
    fun `create baby profile with required fields`() {
        val profile = BabyProfile(
            id = "1",
            name = "Arkana",
            gender = Gender.MALE,
            birthDate = null,
        )

        assertEquals("1", profile.id)
        assertEquals("Arkana", profile.name)
        assertEquals(Gender.MALE, profile.gender)
    }

    @Test
    fun `baby profile defaults to indonesian language`() {
        val profile = BabyProfile(
            id = "1",
            name = "Aisyah",
            gender = Gender.FEMALE,
            birthDate = null,
        )

        assertEquals(LanguagePreference.INDONESIAN, profile.preferredLanguage)
    }
}

class NameRecommendationTest {

    @Test
    fun `create name recommendation with all fields`() {
        val rec = NameRecommendation(
            id = "1",
            name = "Arkana",
            meaning = "Cahaya",
            origin = "Sanskerta",
            gender = "Laki-laki",
            pronunciationGuide = "Ar-ka-na",
            culturalContext = "Budaya Indonesia",
            alternativeSpellings = listOf("Arqana"),
            popularityRank = 45,
            score = 0.95f,
            strategyUsed = NamingStrategy.MODERN_TRENDY,
            reasoning = "Nama modern",
        )

        assertNotNull(rec)
        assertEquals("Arkana", rec.name)
    }

    @Test
    fun `all naming strategies are defined`() {
        val strategies = NamingStrategy.entries
        assertEquals(14, strategies.size)
    }
}

class GenerateRequestTest {

    @Test
    fun `create generate request from baby profile`() {
        val profile = BabyProfile("1", "Arkana", Gender.MALE, null)
        val request = GenerateRequest(
            babyProfile = profile,
            strategy = NamingStrategy.MODERN_TRENDY,
            additionalPreferences = listOf("pendek", "bermakna"),
        )

        assertEquals("Arkana", request.babyProfile.name)
        assertEquals(NamingStrategy.MODERN_TRENDY, request.strategy)
        assertEquals(2, request.additionalPreferences.size)
    }
}

class HistoryItemTest {

    @Test
    fun `create history item`() {
        val item = HistoryItem(
            id = "1",
            babyName = "Arkana",
            gender = "Laki-laki",
            timestamp = System.currentTimeMillis(),
            recommendationsCount = 10,
        )

        assertEquals("Arkana", item.babyName)
        assertEquals(10, item.recommendationsCount)
    }
}

class FavoriteNameTest {

    @Test
    fun `create favorite name`() {
        val fav = FavoriteName(
            id = "1",
            nameRecommendationId = "rec-1",
            name = "Arkana",
            meaning = "Cahaya",
            origin = "Sanskerta",
            savedAt = System.currentTimeMillis(),
        )

        assertEquals("Arkana", fav.name)
        assertEquals("", fav.notes)
    }
}

class SettingsTest {

    @Test
    fun `default settings values`() {
        val settings = Settings()

        assertEquals(LanguagePreference.INDONESIAN, settings.preferredLanguage)
        assertEquals(0.7f, settings.aiTemperature)
        assertEquals(10, settings.maxRecommendations)
        assertEquals(true, settings.isSoundEnabled)
    }
}
