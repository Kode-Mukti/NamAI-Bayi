package com.kodemukti.namaibayi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kodemukti.namaibayi.domain.model.FavoriteName
import com.kodemukti.namaibayi.domain.model.HistoryItem
import com.kodemukti.namaibayi.domain.usecase.GetFavoritesUseCase
import com.kodemukti.namaibayi.domain.usecase.GetHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHistoryUseCase: GetHistoryUseCase,
    private val getFavoritesUseCase: GetFavoritesUseCase,
) : ViewModel() {

    private val _history = MutableStateFlow<List<HistoryItem>>(emptyList())
    val history: StateFlow<List<HistoryItem>> = _history.asStateFlow()

    private val _favorites = MutableStateFlow<List<FavoriteName>>(emptyList())
    val favorites: StateFlow<List<FavoriteName>> = _favorites.asStateFlow()

    init {
        loadHistory()
        loadFavorites()
    }

    private fun loadHistory() {
        viewModelScope.launch {
            getHistoryUseCase().collect { items ->
                _history.value = items
            }
        }
    }

    private fun loadFavorites() {
        viewModelScope.launch {
            getFavoritesUseCase().collect { items ->
                _favorites.value = items
            }
        }
    }
}
