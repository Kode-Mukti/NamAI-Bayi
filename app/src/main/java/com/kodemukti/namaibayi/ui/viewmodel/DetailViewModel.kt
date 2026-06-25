package com.kodemukti.namaibayi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kodemukti.namaibayi.core.common.UiState
import com.kodemukti.namaibayi.domain.model.AIBabyName
import com.kodemukti.namaibayi.domain.model.FavoriteName
import com.kodemukti.namaibayi.domain.repository.FavoriteRepository
import com.kodemukti.namaibayi.domain.repository.GenerateRepository
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
class DetailViewModel @Inject constructor(
    private val generateRepository: GenerateRepository,
    private val favoriteRepository: FavoriteRepository,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<AIBabyName>>(UiState.Idle)
    val uiState: StateFlow<UiState<AIBabyName>> = _uiState.asStateFlow()

    private var currentNameId: String? = null

    val isFavorite: StateFlow<Boolean> = favoriteRepository.getAllFavorites()
        .map { favorites -> favorites.any { it.nameRecommendationId == currentNameId } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun loadDetail(nameRecommendationId: String) {
        if (_uiState.value is UiState.Loading) return
        currentNameId = nameRecommendationId
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            generateRepository.getRecommendationById(nameRecommendationId).fold(
                onSuccess = { name ->
                    _uiState.value = UiState.Success(name)
                },
                onFailure = { error ->
                    _uiState.value = UiState.Error(error.message ?: "Gagal memuat detail nama")
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
                savedAt = System.currentTimeMillis(),
            )
            toggleFavoriteUseCase(favorite)
        }
    }
}
