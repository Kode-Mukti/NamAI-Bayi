package com.kodemukti.namaibayi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kodemukti.namaibayi.core.common.UiState
import com.kodemukti.namaibayi.domain.model.AIBabyName
import com.kodemukti.namaibayi.domain.model.FavoriteName
import com.kodemukti.namaibayi.domain.repository.FavoriteRepository
import com.kodemukti.namaibayi.domain.repository.GenerateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    private val generateRepository: GenerateRepository,
    private val favoriteRepository: FavoriteRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<AIBabyName>>>(UiState.Idle)
    val uiState: StateFlow<UiState<List<AIBabyName>>> = _uiState.asStateFlow()

    private val _favoriteIds = MutableStateFlow<Set<String>>(emptySet())
    val favoriteIds: StateFlow<Set<String>> = _favoriteIds.asStateFlow()

    fun loadResults(babyProfileId: String) {
        if (babyProfileId.isEmpty()) {
            _uiState.value = UiState.Error("ID konsultasi tidak ditemukan")
            return
        }
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            generateRepository.getResultsByProfileId(babyProfileId).fold(
                onSuccess = { response ->
                    val names = response.recommendations
                    _uiState.value = UiState.Success(names)
                    loadFavoriteIds(names)
                },
                onFailure = { error ->
                    _uiState.value = UiState.Error(error.message ?: "Gagal memuat hasil")
                },
            )
        }
    }

    fun toggleFavorite(name: AIBabyName) {
        viewModelScope.launch {
            val isFav = favoriteRepository.isFavorite(name.id)
            if (isFav) {
                favoriteRepository.removeFromFavoritesByRecommendationId(name.id)
                _favoriteIds.value = _favoriteIds.value - name.id
            } else {
                val fav = FavoriteName(
                    id = java.util.UUID.randomUUID().toString(),
                    nameRecommendationId = name.id,
                    name = name.name,
                    meaning = name.meaning,
                    origin = name.origin,
                    savedAt = System.currentTimeMillis(),
                )
                favoriteRepository.addToFavorites(fav)
                _favoriteIds.value = _favoriteIds.value + name.id
            }
        }
    }

    private suspend fun loadFavoriteIds(names: List<AIBabyName>) {
        val ids = mutableSetOf<String>()
        for (name in names) {
            if (favoriteRepository.isFavorite(name.id)) ids.add(name.id)
        }
        _favoriteIds.value = ids
    }
}
