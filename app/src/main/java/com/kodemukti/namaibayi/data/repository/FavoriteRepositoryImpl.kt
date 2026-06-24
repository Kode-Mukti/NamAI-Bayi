package com.kodemukti.namaibayi.data.repository

import com.kodemukti.namaibayi.data.local.dao.FavoriteDao
import com.kodemukti.namaibayi.data.local.entity.FavoriteEntity
import com.kodemukti.namaibayi.domain.model.FavoriteName
import com.kodemukti.namaibayi.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteRepositoryImpl @Inject constructor(
    private val favoriteDao: FavoriteDao,
) : FavoriteRepository {

    override fun getAllFavorites(): Flow<List<FavoriteName>> {
        return favoriteDao.getAllFavorites().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun addToFavorites(item: FavoriteName) {
        favoriteDao.insert(item.toEntity())
    }

    override suspend fun removeFromFavorites(id: String) {
        favoriteDao.deleteById(id)
    }

    override suspend fun removeFromFavoritesByRecommendationId(recommendationId: String) {
        favoriteDao.deleteByRecommendationId(recommendationId)
    }

    override suspend fun isFavorite(nameRecommendationId: String): Boolean {
        return favoriteDao.isFavorite(nameRecommendationId) > 0
    }

    private fun FavoriteName.toEntity() = FavoriteEntity(
        id = id,
        nameRecommendationId = nameRecommendationId,
        name = name,
        meaning = meaning,
        origin = origin,
        savedAt = savedAt,
        notes = notes,
    )

    private fun FavoriteEntity.toDomain() = FavoriteName(
        id = id,
        nameRecommendationId = nameRecommendationId,
        name = name,
        meaning = meaning,
        origin = origin,
        savedAt = savedAt,
        notes = notes,
    )
}
