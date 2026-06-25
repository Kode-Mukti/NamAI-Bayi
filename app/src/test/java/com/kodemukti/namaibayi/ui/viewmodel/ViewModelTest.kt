package com.kodemukti.namaibayi.ui.viewmodel

import android.util.Log
import com.kodemukti.namaibayi.core.common.UiState
import com.kodemukti.namaibayi.domain.model.AIBabyName
import com.kodemukti.namaibayi.domain.model.AIResponse
import com.kodemukti.namaibayi.domain.model.FavoriteName
import com.kodemukti.namaibayi.domain.model.HistoryItem
import com.kodemukti.namaibayi.domain.model.Settings
import com.kodemukti.namaibayi.domain.repository.GenerateRepository
import com.kodemukti.namaibayi.domain.usecase.AIResponseValidator
import com.kodemukti.namaibayi.domain.usecase.CulturalGuardrailFilter
import com.kodemukti.namaibayi.domain.usecase.GenerateNamesUseCase
import com.kodemukti.namaibayi.domain.usecase.GetFavoritesUseCase
import com.kodemukti.namaibayi.domain.usecase.GetHistoryUseCase
import com.kodemukti.namaibayi.domain.usecase.GetSettingsUseCase
import com.kodemukti.namaibayi.domain.usecase.NamingStrategySelector
import com.kodemukti.namaibayi.domain.usecase.SaveToHistoryUseCase
import com.kodemukti.namaibayi.domain.usecase.ToggleFavoriteUseCase
import com.kodemukti.namaibayi.domain.usecase.UpdateSettingsUseCase
import com.kodemukti.namaibayi.core.common.AIErrorLogger
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GenerateViewModelTest {

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.w(any<String>(), any<String>()) } returns 0
    }

    @Test
    fun `generate updates state to success`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)
        val generateUseCase = mockk<GenerateNamesUseCase>()
        val saveHistoryUseCase = mockk<SaveToHistoryUseCase>(relaxed = true)

        val names = (0..4).map { i ->
            AIBabyName(name = "Nama$i", meaning = "Makna$i", origin = "Sanskerta", pronunciationGuide = "Pg$i", culturalContext = "Budaya$i", alternativeSpellings = emptyList(), popularityRank = i, score = 0.9f, strategyUsed = "modern", reasoning = "Bagus$i")
        }
        val response = AIResponse(recommendations = names, modelUsed = "gpt-4", totalTokensUsed = 5)
        coEvery { generateUseCase(any()) } returns Result.success(response)

        val generateRepo = mockk<GenerateRepository>(relaxed = true)
        val validator = mockk<AIResponseValidator>()
        coEvery { validator.validate(any()) } returns AIResponseValidator.ValidationResult.Valid
        val guardrail = mockk<CulturalGuardrailFilter>()
        coEvery { guardrail.filter(any(), any()) } returns CulturalGuardrailFilter.FilterResult(names, 0, emptyList())
        val errorLogger = mockk<AIErrorLogger>(relaxed = true)
        val viewModel = GenerateViewModel(generateUseCase, saveHistoryUseCase, generateRepo, NamingStrategySelector(), guardrail, validator, errorLogger)
        viewModel.updateName("Arkana")
        viewModel.generate()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue("Expected Success but got $state", state is UiState.Success)
        assertEquals("Nama0", (state as UiState.Success).data.recommendations.first().name)
    }

    @Test
    fun `generate with empty name returns error`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)
        val generateUseCase = mockk<GenerateNamesUseCase>()
        val saveHistoryUseCase = mockk<SaveToHistoryUseCase>(relaxed = true)
        coEvery { generateUseCase(any()) } returns Result.success(AIResponse(emptyList(), "test", 0))

        val generateRepo = mockk<GenerateRepository>(relaxed = true)
        val errorLogger = mockk<AIErrorLogger>(relaxed = true)
        val viewModel = GenerateViewModel(generateUseCase, saveHistoryUseCase, generateRepo, NamingStrategySelector(), CulturalGuardrailFilter(), AIResponseValidator(), errorLogger)
        viewModel.generate()
        advanceUntilIdle()

        assertTrue(viewModel.uiState.value is UiState.Error)
    }

    @Test
    fun `reset state clears values`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)
        val generateUseCase = mockk<GenerateNamesUseCase>(relaxed = true)
        val saveHistoryUseCase = mockk<SaveToHistoryUseCase>(relaxed = true)

        val generateRepo = mockk<GenerateRepository>(relaxed = true)
        val errorLogger = mockk<AIErrorLogger>(relaxed = true)
        val viewModel = GenerateViewModel(generateUseCase, saveHistoryUseCase, generateRepo, NamingStrategySelector(), CulturalGuardrailFilter(), AIResponseValidator(), errorLogger)
        viewModel.updateName("Test")
        viewModel.resetState()

        assertEquals("", viewModel.formState.value.name)
        assertEquals(UiState.Idle, viewModel.uiState.value)
    }
}

class HomeViewModelTest {

    @Test
    fun `loads history and favorites on init`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)
        val getHistoryUseCase = mockk<GetHistoryUseCase>()
        val getFavoritesUseCase = mockk<GetFavoritesUseCase>()

        coEvery { getHistoryUseCase() } returns flowOf(
            listOf(HistoryItem("1", "Arkana", "Laki-laki", 1000L, 10))
        )
        coEvery { getFavoritesUseCase() } returns flowOf(
            listOf(FavoriteName("1", "r1", "Arkana", "Cahaya", "Sanskerta", 1000L))
        )

        val viewModel = HomeViewModel(getHistoryUseCase, getFavoritesUseCase)

        advanceUntilIdle()
        assertEquals(1, viewModel.history.value.size)
        assertEquals(1, viewModel.favorites.value.size)
    }
}

class SettingsViewModelTest {

    @Test
    fun `loads settings on init`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)
        val getSettingsUseCase = mockk<GetSettingsUseCase>()
        val updateSettingsUseCase = mockk<UpdateSettingsUseCase>(relaxed = true)

        coEvery { getSettingsUseCase() } returns flowOf(Settings())

        val viewModel = SettingsViewModel(getSettingsUseCase, updateSettingsUseCase)

        advanceUntilIdle()
        assertEquals(true, viewModel.settings.value.isSoundEnabled)
    }
}

class FavoritesViewModelTest {

    @Test
    fun `loads favorites on init`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)
        val getFavoritesUseCase = mockk<GetFavoritesUseCase>()
        val toggleFavoriteUseCase = mockk<ToggleFavoriteUseCase>(relaxed = true)

        val items = listOf(FavoriteName("1", "r1", "Arkana", "Cahaya", "Sanskerta", 1000L))
        coEvery { getFavoritesUseCase() } returns flowOf(items)

        val viewModel = FavoritesViewModel(getFavoritesUseCase, toggleFavoriteUseCase)

        advanceUntilIdle()
        assertEquals(1, viewModel.favorites.value.size)
    }
}

class HistoryViewModelTest {

    @Test
    fun `loads history on init`() = runTest {
        val dispatcher = StandardTestDispatcher(testScheduler)
        Dispatchers.setMain(dispatcher)
        val getHistoryUseCase = mockk<GetHistoryUseCase>()

        val items = listOf(HistoryItem("1", "Arkana", "Laki-laki", 1000L, 10))
        coEvery { getHistoryUseCase() } returns flowOf(items)

        val viewModel = HistoryViewModel(getHistoryUseCase)

        advanceUntilIdle()
        assertEquals(1, viewModel.history.value.size)
    }
}
