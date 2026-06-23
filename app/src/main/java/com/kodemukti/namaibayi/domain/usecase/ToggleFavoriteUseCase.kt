package com.kodemukti.namaibayi.domain.usecase

import com.kodemukti.namaibayi.domain.model.FavoriteName
import com.kodemukti.namaibayi.domain.repository.FavoriteRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val repository: FavoriteRepository,
) {
    suspend operator fun invoke(item: FavoriteName) {
        val exists = repository.isFavorite(item.nameRecommendationId)
        if (exists) {
            repository.removeFromFavorites(item.id)
        } else {
            repository.addToFavorites(item)
        }
    }
}
