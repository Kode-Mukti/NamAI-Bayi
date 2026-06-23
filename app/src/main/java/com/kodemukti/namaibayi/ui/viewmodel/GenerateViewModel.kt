package com.kodemukti.namaibayi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kodemukti.namaibayi.core.common.UiState
import com.kodemukti.namaibayi.domain.model.BabyProfile
import com.kodemukti.namaibayi.domain.model.GenerateRequest
import com.kodemukti.namaibayi.domain.model.AIResponse
import com.kodemukti.namaibayi.domain.model.HistoryItem
import com.kodemukti.namaibayi.domain.model.Gender
import com.kodemukti.namaibayi.domain.repository.GenerateRepository
import com.kodemukti.namaibayi.domain.usecase.GenerateNamesUseCase
import com.kodemukti.namaibayi.domain.usecase.SaveToHistoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GenerateFormState(
    val fatherName: String = "",
    val motherName: String = "",
    val gender: Gender = Gender.NEUTRAL,
    val religion: String = "",
    val culture: String = "",
    val province: String = "",
    val desiredMeaning: String = "",
    val personality: String = "",
)

@HiltViewModel
class GenerateViewModel @Inject constructor(
    private val generateNamesUseCase: GenerateNamesUseCase,
    private val saveToHistoryUseCase: SaveToHistoryUseCase,
    private val generateRepository: GenerateRepository,
) : ViewModel() {

    private val _formState = MutableStateFlow(GenerateFormState())
    val formState: StateFlow<GenerateFormState> = _formState.asStateFlow()

    private val _uiState = MutableStateFlow<UiState<AIResponse>>(UiState.Idle)
    val uiState: StateFlow<UiState<AIResponse>> = _uiState.asStateFlow()

    private val _generatedProfileId = MutableStateFlow<String?>(null)
    val generatedProfileId: StateFlow<String?> = _generatedProfileId.asStateFlow()

    fun updateFatherName(value: String) { _formState.update { it.copy(fatherName = value) } }
    fun updateMotherName(value: String) { _formState.update { it.copy(motherName = value) } }
    fun updateGender(value: Gender) { _formState.update { it.copy(gender = value) } }
    fun updateReligion(value: String) { _formState.update { it.copy(religion = value) } }
    fun updateCulture(value: String) { _formState.update { it.copy(culture = value) } }
    fun updateProvince(value: String) { _formState.update { it.copy(province = value) } }
    fun updateMeaning(value: String) { _formState.update { it.copy(desiredMeaning = value) } }
    fun updatePersonality(value: String) { _formState.update { it.copy(personality = value) } }

    fun generate() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val currentProfileId = java.util.UUID.randomUUID().toString()
            val state = _formState.value

            val request = GenerateRequest(
                babyProfile = BabyProfile(
                    id = currentProfileId,
                    name = "",
                    gender = state.gender,
                    birthDate = null,
                ),
                additionalPreferences = listOf(
                    "Ayah: ${state.fatherName}",
                    "Ibu: ${state.motherName}",
                    "Agama: ${state.religion}",
                    "Budaya: ${state.culture}",
                    "Provinsi: ${state.province}",
                    "Makna: ${state.desiredMeaning}",
                    "Kepribadian: ${state.personality}"
                ).filter { it.split(": ")[1].isNotBlank() }
            )

            generateNamesUseCase(request).fold(
                onSuccess = { response ->
                    generateRepository.saveResults(currentProfileId, response)
                    val historyItem = HistoryItem(
                        id = java.util.UUID.randomUUID().toString(),
                        babyName = "Konsultasi ${state.culture} ${state.religion}".trim().ifEmpty { "Konsultasi Cepat" },
                        gender = state.gender.name.lowercase(),
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
        _formState.value = GenerateFormState()
        _generatedProfileId.value = null
    }
}
