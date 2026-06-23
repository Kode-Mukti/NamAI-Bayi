package com.kodemukti.namaibayi.ui.viewmodel

import com.kodemukti.namaibayi.core.common.UiState
import com.kodemukti.namaibayi.domain.model.AIBabyName
import com.kodemukti.namaibayi.domain.model.AIResponse
import com.kodemukti.namaibayi.domain.model.BabyProfile
import com.kodemukti.namaibayi.domain.model.FavoriteName
import com.kodemukti.namaibayi.domain.model.Gender
import com.kodemukti.namaibayi.domain.model.GenerateRequest
import com.kodemukti.namaibayi.domain.model.HistoryItem
import com.kodemukti.namaibayi.domain.model.Settings
import com.kodemukti.namaibayi.domain.usecase.GenerateNamesUseCase
import com.kodemukti.namaibayi.domain.usecase.GetFavoritesUseCase
import com.kodemukti.namaibayi.domain.usecase.GetHistoryUseCase
import com.kodemukti.namaibayi.domain.usecase.GetSettingsUseCase
import com.kodemukti.namaibayi.domain.usecase.SaveToHistoryUseCase
import com.kodemukti.namaibayi.domain.usecase.ToggleFavoriteUseCase
import com.kodemukti.namaibayi.domain.usecase.UpdateSettingsUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GenerateViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `generate updates state to success`() = runTest {
        val generateUseCase = mockk<GenerateNamesUseCase>()
        val saveHistoryUseCase = mockk<SaveToHistoryUseCase>(relaxed = true)

        val response = AIResponse(
            recommendations = listOf(
                AIBabyName("Arkana", "Cahaya", "Sanskerta", "Ar-ka-na", "Budaya", emptyList(), null, 0.95f, "modern", "Bagus")
            ),
            modelUsed = "gpt-4",
            totalTokensUsed = 100,
        )
        coEvery { generateUseCase(any()) } returns Result.success(response)

        val viewModel = GenerateViewModel(generateUseCase, saveHistoryUseCase)
        viewModel.onNameChanged("Arkana")
        viewModel.generate()
        advanceUntilIdle()

        val state = viewModel.uiState.value
        assertTrue(state is UiState.Success)
        assertEquals("Arkana", (state as UiState.Success).data.recommendations.first().name)
    }

    @Test
    fun `generate with empty name does nothing`() = runTest {
        val generateUseCase = mockk<GenerateNamesUseCase>(relaxed = true)
        val saveHistoryUseCase = mockk<SaveToHistoryUseCase>(relaxed = true)

        val viewModel = GenerateViewModel(generateUseCase, saveHistoryUseCase)
        viewModel.generate()

        assertEquals(UiState.Idle, viewModel.uiState.value)
    }

    @Test
    fun `reset state clears values`() {
        val generateUseCase = mockk<GenerateNamesUseCase>(relaxed = true)
        val saveHistoryUseCase = mockk<SaveToHistoryUseCase>(relaxed = true)

        val viewModel = GenerateViewModel(generateUseCase, saveHistoryUseCase)
        viewModel.onNameChanged("Test")
        viewModel.resetState()

        assertEquals("", viewModel.name.value)
        assertEquals(UiState.Idle, viewModel.uiState.value)
    }
}

class HomeViewModelTest {

    @Test
    fun `loads history and favorites on init`() = runTest {
        val getHistoryUseCase = mockk<GetHistoryUseCase>()
        val getFavoritesUseCase = mockk<GetFavoritesUseCase>()

        coEvery { getHistoryUseCase() } returns flowOf(
            listOf(HistoryItem("1", "Arkana", "Laki-laki", 1000L, 10))
        )
        coEvery { getFavoritesUseCase() } returns flowOf(
            listOf(FavoriteName("1", "r1", "Arkana", "Cahaya", "Sanskerta", 1000L))
        )

        val viewModel = HomeViewModel(getHistoryUseCase, getFavoritesUseCase)

        assertEquals(1, viewModel.history.value.size)
        assertEquals(1, viewModel.favorites.value.size)
    }
}

class SettingsViewModelTest {

    @Test
    fun `loads settings on init`() = runTest {
        val getSettingsUseCase = mockk<GetSettingsUseCase>()
        val updateSettingsUseCase = mockk<UpdateSettingsUseCase>(relaxed = true)

        coEvery { getSettingsUseCase() } returns flowOf(Settings())

        val viewModel = SettingsViewModel(getSettingsUseCase, updateSettingsUseCase)

        assertEquals(true, viewModel.settings.value.isSoundEnabled)
    }
}

class FavoritesViewModelTest {

    @Test
    fun `loads favorites on init`() = runTest {
        val getFavoritesUseCase = mockk<GetFavoritesUseCase>()
        val toggleFavoriteUseCase = mockk<ToggleFavoriteUseCase>(relaxed = true)

        val items = listOf(FavoriteName("1", "r1", "Arkana", "Cahaya", "Sanskerta", 1000L))
        coEvery { getFavoritesUseCase() } returns flowOf(items)

        val viewModel = FavoritesViewModel(getFavoritesUseCase, toggleFavoriteUseCase)

        assertEquals(1, viewModel.favorites.value.size)
    }
}

class HistoryViewModelTest {

    @Test
    fun `loads history on init`() = runTest {
        val getHistoryUseCase = mockk<GetHistoryUseCase>()

        val items = listOf(HistoryItem("1", "Arkana", "Laki-laki", 1000L, 10))
        coEvery { getHistoryUseCase() } returns flowOf(items)

        val viewModel = HistoryViewModel(getHistoryUseCase)

        assertEquals(1, viewModel.history.value.size)
    }
}
