package com.kodemukti.namaibayi.data.repository

import com.kodemukti.namaibayi.data.local.dao.FavoriteDao
import com.kodemukti.namaibayi.data.local.dao.HistoryDao
import com.kodemukti.namaibayi.data.local.entity.FavoriteEntity
import com.kodemukti.namaibayi.data.local.entity.HistoryEntity
import com.kodemukti.namaibayi.data.remote.api.NamAIApi
import com.kodemukti.namaibayi.data.remote.dto.GenerateRequestDto
import com.kodemukti.namaibayi.data.remote.dto.GenerateResponseDto
import com.kodemukti.namaibayi.data.remote.dto.NameDto
import com.kodemukti.namaibayi.data.remote.mapper.AIResponseMapper
import com.kodemukti.namaibayi.data.remote.mapper.GenerateRequestMapper
import com.kodemukti.namaibayi.domain.model.BabyProfile
import com.kodemukti.namaibayi.domain.model.FavoriteName
import com.kodemukti.namaibayi.domain.model.Gender
import com.kodemukti.namaibayi.domain.model.GenerateRequest
import com.kodemukti.namaibayi.domain.model.HistoryItem
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.Response

class HistoryRepositoryImplTest {

    @Test
    fun `get all history maps entities to domain`() = runTest {
        val dao = mockk<HistoryDao>()
        val entities = listOf(
            HistoryEntity("1", "Arkana", "Laki-laki", 1000L, 10)
        )
        coEvery { dao.getAllHistory() } returns flowOf(entities)

        val repo = HistoryRepositoryImpl(dao)
        val result = repo.getAllHistory().first()

        assertEquals(1, result.size)
        assertEquals("Arkana", result[0].babyName)
    }

    @Test
    fun `add to history delegates to dao`() = runTest {
        val dao = mockk<HistoryDao>(relaxed = true)
        val item = HistoryItem("1", "Arkana", "Laki-laki", 1000L, 10)

        val repo = HistoryRepositoryImpl(dao)
        repo.addToHistory(item)

        coVerify { dao.insert(any()) }
    }

    @Test
    fun `delete from history delegates to dao`() = runTest {
        val dao = mockk<HistoryDao>(relaxed = true)

        val repo = HistoryRepositoryImpl(dao)
        repo.deleteFromHistory("1")

        coVerify { dao.deleteById("1") }
    }
}

class FavoriteRepositoryImplTest {

    @Test
    fun `get all favorites maps entities to domain`() = runTest {
        val dao = mockk<FavoriteDao>()
        val entities = listOf(
            FavoriteEntity("1", "r1", "Arkana", "Cahaya", "Sanskerta", 1000L)
        )
        coEvery { dao.getAllFavorites() } returns flowOf(entities)

        val repo = FavoriteRepositoryImpl(dao)
        val result = repo.getAllFavorites().first()

        assertEquals(1, result.size)
        assertEquals("Arkana", result[0].name)
    }

    @Test
    fun `is favorite returns true when count gt 0`() = runTest {
        val dao = mockk<FavoriteDao>()
        coEvery { dao.isFavorite("r1") } returns 1

        val repo = FavoriteRepositoryImpl(dao)
        val result = repo.isFavorite("r1")

        assertTrue(result)
    }
}

class GenerateRepositoryImplTest {

    @Test
    fun `generate names returns success from api`() = runTest {
        val api = mockk<NamAIApi>()
        val requestMapper = GenerateRequestMapper()
        val responseMapper = AIResponseMapper()

        val dto = GenerateResponseDto(
            recommendations = listOf(
                NameDto(name = "Arkana", meaning = "Cahaya", origin = "Sanskerta", pronunciationGuide = "Ar-ka-na", culturalContext = "Budaya", alternativeSpellings = emptyList(), popularityRank = null, score = 0.95f, strategyUsed = "modern", reasoning = "Bagus")
            ),
            modelUsed = "gpt-4",
            totalTokensUsed = 100,
        )
        coEvery { api.generateNames(any()) } returns Response.success(dto)

        val repo = GenerateRepositoryImpl(api, requestMapper, responseMapper)
        val request = GenerateRequest(
            babyProfile = BabyProfile("1", "Arkana", Gender.MALE, null),
        )

        val result = repo.generateNames(request)

        assertTrue(result.isSuccess)
        assertEquals("Arkana", result.getOrNull()?.recommendations?.first()?.name)
    }

    @Test
    fun `generate names returns failure on api error`() = runTest {
        val api = mockk<NamAIApi>()
        val requestMapper = GenerateRequestMapper()
        val responseMapper = AIResponseMapper()

        coEvery { api.generateNames(any()) } returns Response.error(500, okhttp3.ResponseBody.create(null, "{}"))

        val repo = GenerateRepositoryImpl(api, requestMapper, responseMapper)
        val request = GenerateRequest(
            babyProfile = BabyProfile("1", "Arkana", Gender.MALE, null),
        )

        val result = repo.generateNames(request)

        assertTrue(result.isFailure)
    }
}
