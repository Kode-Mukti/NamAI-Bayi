package com.kodemukti.namaibayi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kodemukti.namaibayi.core.common.UiState
import com.kodemukti.namaibayi.domain.model.AIBabyName
import com.kodemukti.namaibayi.domain.model.FavoriteName
import com.kodemukti.namaibayi.domain.repository.GenerateRepository
import com.kodemukti.namaibayi.domain.repository.FavoriteRepository
import com.kodemukti.namaibayi.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    private val generateRepository: GenerateRepository,
    private val favoriteRepository: FavoriteRepository,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<AIBabyName>>>(UiState.Idle)
    val uiState: StateFlow<UiState<List<AIBabyName>>> = _uiState.asStateFlow()

    val favoriteIds: StateFlow<Set<String>> = favoriteRepository.getAllFavorites()
        .map { favorites -> favorites.map { it.nameRecommendationId }.toSet() }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptySet())

    fun loadResults(babyProfileId: String) {
        if (_uiState.value is UiState.Loading) return
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            generateRepository.getResultsByProfileId(babyProfileId).fold(
                onSuccess = { response ->
                    _uiState.value = UiState.Success(response.recommendations)
                },
                onFailure = { error ->
                    _uiState.value = UiState.Error(error.message ?: "Gagal memuat hasil")
                },
            )
        }
    }

    fun toggleFavorite(name: AIBabyName) {
        viewModelScope.launch {
            val favorite = FavoriteName(
                id = java.util.UUID.randomUUID().toString(),
                nameRecommendationId = name.id,
                name = name.name,
                meaning = name.meaning,
                origin = name.origin,
                savedAt = System.currentTimeMillis()
            )
            toggleFavoriteUseCase(favorite)
        }
    }
}
