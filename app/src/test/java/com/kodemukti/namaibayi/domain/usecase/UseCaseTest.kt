package com.kodemukti.namaibayi.domain.usecase

import com.kodemukti.namaibayi.domain.model.AIBabyName
import com.kodemukti.namaibayi.domain.model.AIResponse
import com.kodemukti.namaibayi.domain.model.FavoriteName
import com.kodemukti.namaibayi.domain.model.HistoryItem
import com.kodemukti.namaibayi.domain.model.NamingStrategy
import com.kodemukti.namaibayi.domain.model.Settings
import com.kodemukti.namaibayi.domain.repository.FavoriteRepository
import com.kodemukti.namaibayi.domain.repository.HistoryRepository
import com.kodemukti.namaibayi.domain.repository.SettingsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class GetHistoryUseCaseTest {

    @Test
    fun `get history returns flow of items`() = runTest {
        val repo = mockk<HistoryRepository>()
        val items = listOf(HistoryItem("1", "Arkana", "Laki-laki", 1000L, 10))
        coEvery { repo.getAllHistory() } returns flowOf(items)

        val useCase = GetHistoryUseCase(repo)
        val result = useCase()

        coVerify { repo.getAllHistory() }
    }
}

class SaveToHistoryUseCaseTest {

    @Test
    fun `save history delegates to repository`() = runTest {
        val repo = mockk<HistoryRepository>(relaxed = true)
        val item = HistoryItem("1", "Arkana", "Laki-laki", 1000L, 10)

        val useCase = SaveToHistoryUseCase(repo)
        useCase(item)

        coVerify { repo.addToHistory(item) }
    }
}

class GetFavoritesUseCaseTest {

    @Test
    fun `get favorites returns flow of items`() = runTest {
        val repo = mockk<FavoriteRepository>()
        val items = listOf(FavoriteName("1", "r1", "Arkana", "Cahaya", "Sanskerta", 1000L))
        coEvery { repo.getAllFavorites() } returns flowOf(items)

        val useCase = GetFavoritesUseCase(repo)
        val result = useCase()

        coVerify { repo.getAllFavorites() }
    }
}

class ToggleFavoriteUseCaseTest {

    @Test
    fun `toggle favorite removes when exists`() = runTest {
        val repo = mockk<FavoriteRepository>(relaxed = true)
        coEvery { repo.isFavorite("r1") } returns true
        val item = FavoriteName("1", "r1", "Arkana", "Cahaya", "Sanskerta", 1000L)

        val useCase = ToggleFavoriteUseCase(repo)
        useCase(item)

        coVerify { repo.removeFromFavorites("1") }
    }

    @Test
    fun `toggle favorite adds when not exists`() = runTest {
        val repo = mockk<FavoriteRepository>(relaxed = true)
        coEvery { repo.isFavorite("r1") } returns false
        val item = FavoriteName("1", "r1", "Arkana", "Cahaya", "Sanskerta", 1000L)

        val useCase = ToggleFavoriteUseCase(repo)
        useCase(item)

        coVerify { repo.addToFavorites(item) }
    }
}

class GetSettingsUseCaseTest {

    @Test
    fun `get settings returns flow`() = runTest {
        val repo = mockk<SettingsRepository>()
        coEvery { repo.getSettings() } returns flowOf(Settings())

        val useCase = GetSettingsUseCase(repo)
        val result = useCase()

        coVerify { repo.getSettings() }
    }
}

class UpdateSettingsUseCaseTest {

    @Test
    fun `update settings delegates to repository`() = runTest {
        val repo = mockk<SettingsRepository>(relaxed = true)
        val settings = Settings()

        val useCase = UpdateSettingsUseCase(repo)
        useCase(settings)

        coVerify { repo.updateSettings(settings) }
    }
}

class NamingStrategySelectorTest {

    private val selector = NamingStrategySelector()

    @Test
    fun `islam returns religious islamic`() {
        assertEquals(NamingStrategy.RELIGIOUS_ISLAMIC, selector.select("Islam", "", 0.5f, false))
    }

    @Test
    fun `kristen katolik returns religious christian`() {
        assertEquals(NamingStrategy.RELIGIOUS_CHRISTIAN, selector.select("Kristen", "", 0.5f, false))
        assertEquals(NamingStrategy.RELIGIOUS_CHRISTIAN, selector.select("Katolik", "", 0.5f, false))
    }

    @Test
    fun `hindu buddha konghucu returns religious hindu buddha`() {
        assertEquals(NamingStrategy.RELIGIOUS_HINDU_BUDDHA, selector.select("Hindu", "", 0.5f, false))
        assertEquals(NamingStrategy.RELIGIOUS_HINDU_BUDDHA, selector.select("Buddha", "", 0.5f, false))
    }

    @Test
    fun `lainnya falls through to culture`() {
        assertEquals(NamingStrategy.TRADITIONAL_CULTURAL, selector.select("Lainnya", "Jawa", 0.5f, false))
    }

    @Test
    fun `culture returns traditional cultural`() {
        assertEquals(NamingStrategy.TRADITIONAL_CULTURAL, selector.select("", "Jawa", 0.5f, false))
    }

    @Test
    fun `tionghoa returns combination`() {
        assertEquals(NamingStrategy.COMBINATION, selector.select("", "Tionghoa", 0.5f, false))
    }

    @Test
    fun `meaning returns meaning based`() {
        assertEquals(NamingStrategy.MEANING_BASED, selector.select("", "", 0.5f, true))
    }

    @Test
    fun `high uniqueness returns unique rare`() {
        assertEquals(NamingStrategy.UNIQUE_RARE, selector.select("", "", 0.66f, false))
    }

    @Test
    fun `low uniqueness returns short simple`() {
        assertEquals(NamingStrategy.SHORT_SIMPLE, selector.select("", "", 0.33f, false))
    }

    @Test
    fun `medium uniqueness returns modern trendy`() {
        assertEquals(NamingStrategy.MODERN_TRENDY, selector.select("", "", 0.5f, false))
    }

    @Test
    fun `religion overrides culture`() {
        assertEquals(NamingStrategy.RELIGIOUS_ISLAMIC, selector.select("Islam", "Tionghoa", 0.5f, false))
    }
}

class CulturalGuardrailFilterTest {

    private val filter = CulturalGuardrailFilter()

    private fun name(n: String, o: String = "Arab", c: String = "Budaya Islam") = AIBabyName(
        name = n, meaning = "M", origin = o, pronunciationGuide = "Pg",
        culturalContext = c, reasoning = "R", strategyUsed = "s", score = 0.9f,
    )

    @Test
    fun `empty religion returns all`() {
        val r = AIResponse(listOf(name("Gabriel", "Ibrani", "Kristen")), "t", 1)
        assertEquals(1, filter.filter(r, "").recommendations.size)
    }

    @Test
    fun `islam filters christian context`() {
        val names = listOf(name("Muhammad"), name("Gabriel", "Ibrani", "Kristen"))
        val result = filter.filter(AIResponse(names, "t", 2), "Islam")
        assertEquals(1, result.recommendations.size)
        assertEquals("Muhammad", result.recommendations[0].name)
    }

    @Test
    fun `christian filters islamic context`() {
        val names = listOf(name("Matthew", "Ibrani", "Kristen"), name("Muhammad", "Arab", "Nabi Islam"))
        val result = filter.filter(AIResponse(names, "t", 2), "Kristen")
        assertEquals(1, result.recommendations.size)
        assertEquals("Matthew", result.recommendations[0].name)
    }
}

class AIResponseValidatorTest {

    private val validator = AIResponseValidator()

    private fun name(id: Int = 0) = AIBabyName(
        name = "N$id", meaning = "M", origin = "O", pronunciationGuide = "P",
        culturalContext = "C", reasoning = "R", strategyUsed = "S",
        score = 0.9f, uniquenessScore = 50,
    )

    @Test
    fun `valid 5 names passes`() {
        val r = AIResponse((0..4).map { name(it) }, "t", 5)
        assert(validator.validate(r) is AIResponseValidator.ValidationResult.Valid)
    }

    @Test
    fun `wrong count fails`() {
        assert(validator.validate(AIResponse((0..2).map { name(it) }, "t", 3)) is AIResponseValidator.ValidationResult.Invalid)
    }

    @Test
    fun `blank name fails`() {
        val names = (0..4).map { if (it == 0) name().copy(name = "") else name(it) }
        assert(validator.validate(AIResponse(names, "t", 5)) is AIResponseValidator.ValidationResult.Invalid)
    }

    @Test
    fun `blank meaning fails`() {
        val names = (0..4).map { if (it == 0) name().copy(meaning = "") else name(it) }
        assert(validator.validate(AIResponse(names, "t", 5)) is AIResponseValidator.ValidationResult.Invalid)
    }

    @Test
    fun `score out of range fails`() {
        val names = (0..4).map { if (it == 0) name().copy(score = 1.5f) else name(it) }
        assert(validator.validate(AIResponse(names, "t", 5)) is AIResponseValidator.ValidationResult.Invalid)
    }

    @Test
    fun `uniqueness out of range fails`() {
        val names = (0..4).map { if (it == 0) name().copy(uniquenessScore = 150) else name(it) }
        assert(validator.validate(AIResponse(names, "t", 5)) is AIResponseValidator.ValidationResult.Invalid)
    }
}
