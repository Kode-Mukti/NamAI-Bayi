package com.kodemukti.namaibayi.domain.usecase

import com.kodemukti.namaibayi.domain.model.FavoriteName
import com.kodemukti.namaibayi.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoritesUseCase @Inject constructor(
    private val repository: FavoriteRepository,
) {
    operator fun invoke(): Flow<List<FavoriteName>> {
        return repository.getAllFavorites()
    }
}
