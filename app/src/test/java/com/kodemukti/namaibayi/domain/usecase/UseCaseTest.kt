package com.kodemukti.namaibayi.domain.usecase

import com.kodemukti.namaibayi.domain.model.FavoriteName
import com.kodemukti.namaibayi.domain.model.HistoryItem
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
