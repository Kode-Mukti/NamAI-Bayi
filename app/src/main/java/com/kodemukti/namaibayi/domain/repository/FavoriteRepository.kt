package com.kodemukti.namaibayi.domain.repository

import com.kodemukti.namaibayi.domain.model.FavoriteName
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    fun getAllFavorites(): Flow<List<FavoriteName>>
    suspend fun addToFavorites(item: FavoriteName)
    suspend fun removeFromFavorites(id: String)
    suspend fun removeFromFavoritesByRecommendationId(recommendationId: String)
    suspend fun isFavorite(nameRecommendationId: String): Boolean
}
