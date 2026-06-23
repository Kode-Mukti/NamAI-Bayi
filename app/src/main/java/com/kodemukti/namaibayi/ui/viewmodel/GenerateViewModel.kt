package com.kodemukti.namaibayi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kodemukti.namaibayi.core.common.UiState
import com.kodemukti.namaibayi.domain.model.BabyProfile
import com.kodemukti.namaibayi.domain.model.GenerateRequest
import com.kodemukti.namaibayi.domain.model.AIResponse
import com.kodemukti.namaibayi.domain.model.HistoryItem
import com.kodemukti.namaibayi.domain.model.NamingStrategy
import com.kodemukti.namaibayi.domain.repository.GenerateRepository
import com.kodemukti.namaibayi.domain.usecase.GenerateNamesUseCase
import com.kodemukti.namaibayi.domain.usecase.SaveToHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GenerateViewModel @Inject constructor(
    private val generateNamesUseCase: GenerateNamesUseCase,
    private val saveToHistoryUseCase: SaveToHistoryUseCase,
    private val generateRepository: GenerateRepository,
) : ViewModel() {

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name.asStateFlow()

    private val _uiState = MutableStateFlow<UiState<AIResponse>>(UiState.Idle)
    val uiState: StateFlow<UiState<AIResponse>> = _uiState.asStateFlow()

    private val _generatedProfileId = MutableStateFlow<String?>(null)
    val generatedProfileId: StateFlow<String?> = _generatedProfileId.asStateFlow()

    private var currentProfileId: String? = null

    fun onNameChanged(value: String) {
        _name.value = value
    }

    fun generate() {
        val babyName = _name.value.trim()
        if (babyName.isEmpty()) return

        viewModelScope.launch {
            _uiState.value = UiState.Loading
            currentProfileId = java.util.UUID.randomUUID().toString()

            val request = GenerateRequest(
                babyProfile = BabyProfile(
                    id = currentProfileId!!,
                    name = babyName,
                    gender = com.kodemukti.namaibayi.domain.model.Gender.NEUTRAL,
                    birthDate = null,
                )
            )

            generateNamesUseCase(request).fold(
                onSuccess = { response ->
                    generateRepository.saveResults(currentProfileId!!, response)
                    val historyItem = HistoryItem(
                        id = java.util.UUID.randomUUID().toString(),
                        babyName = babyName,
                        gender = "neutral",
                        timestamp = System.currentTimeMillis(),
                        recommendationsCount = response.recommendations.size,
                    )
                    saveToHistoryUseCase(historyItem)
                    _uiState.value = UiState.Success(response)
                    _generatedProfileId.value = currentProfileId
                },
                onFailure = { error ->
                    _uiState.value = UiState.Error(error.message ?: "Gagal menghasilkan nama")
                },
            )
        }
    }

    fun resetState() {
        _uiState.value = UiState.Idle
        _name.value = ""
        _generatedProfileId.value = null
        currentProfileId = null
    }
}
