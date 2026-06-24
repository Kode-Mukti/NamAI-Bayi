package com.kodemukti.namaibayi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.util.Log
import com.kodemukti.namaibayi.core.common.AIErrorLogger
import com.kodemukti.namaibayi.core.common.UiState
import com.kodemukti.namaibayi.domain.model.BabyProfile
import com.kodemukti.namaibayi.domain.model.GenerateRequest
import com.kodemukti.namaibayi.domain.model.AIResponse
import com.kodemukti.namaibayi.domain.model.HistoryItem
import com.kodemukti.namaibayi.domain.model.Gender
import com.kodemukti.namaibayi.domain.repository.GenerateRepository
import com.kodemukti.namaibayi.domain.usecase.AIResponseValidator
import com.kodemukti.namaibayi.domain.usecase.CulturalGuardrailFilter
import com.kodemukti.namaibayi.domain.usecase.GenerateNamesUseCase
import com.kodemukti.namaibayi.domain.usecase.NamingStrategySelector
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
    val name: String = "",
    val gender: Gender = Gender.NEUTRAL,
    val religion: String = "",
    val culture: String = "",
    val province: String = "",
    val desiredMeaning: String = "",
    val personality: String = "",
    val uniquenessLevel: Float = 0.5f,
)

@HiltViewModel
class GenerateViewModel @Inject constructor(
    private val generateNamesUseCase: GenerateNamesUseCase,
    private val saveToHistoryUseCase: SaveToHistoryUseCase,
    private val generateRepository: GenerateRepository,
    private val strategySelector: NamingStrategySelector,
    private val guardrailFilter: CulturalGuardrailFilter,
    private val responseValidator: AIResponseValidator,
    private val errorLogger: AIErrorLogger,
) : ViewModel() {

    private val _formState = MutableStateFlow(GenerateFormState())
    val formState: StateFlow<GenerateFormState> = _formState.asStateFlow()

    private val _uiState = MutableStateFlow<UiState<AIResponse>>(UiState.Idle)
    val uiState: StateFlow<UiState<AIResponse>> = _uiState.asStateFlow()

    private val _generatedProfileId = MutableStateFlow<String?>(null)
    val generatedProfileId: StateFlow<String?> = _generatedProfileId.asStateFlow()

    fun updateFatherName(value: String) { _formState.update { it.copy(fatherName = value) } }
    fun updateMotherName(value: String) { _formState.update { it.copy(motherName = value) } }
    fun updateName(value: String) { _formState.update { it.copy(name = value) } }
    fun updateGender(value: Gender) { _formState.update { it.copy(gender = value) } }
    fun updateReligion(value: String) { _formState.update { it.copy(religion = value) } }
    fun updateCulture(value: String) { _formState.update { it.copy(culture = value) } }
    fun updateProvince(value: String) { _formState.update { it.copy(province = value) } }
    fun updateMeaning(value: String) { _formState.update { it.copy(desiredMeaning = value) } }
    fun updatePersonality(value: String) { _formState.update { it.copy(personality = value) } }
    fun updateUniquenessLevel(value: Float) { _formState.update { it.copy(uniquenessLevel = value) } }

    fun generate() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val currentProfileId = java.util.UUID.randomUUID().toString()
            val state = _formState.value

            val uniquenessDesc = when {
                state.uniquenessLevel < 0.33f -> "Populer"
                state.uniquenessLevel < 0.66f -> "Sedang"
                else -> "Unik"
            }
            val strategy = strategySelector.select(
                religion = state.religion,
                culture = state.culture,
                uniquenessLevel = state.uniquenessLevel,
                hasMeaning = state.desiredMeaning.isNotBlank(),
            )
            val request = GenerateRequest(
                babyProfile = BabyProfile(
                    id = currentProfileId,
                    name = state.name,
                    gender = state.gender,
                    birthDate = null,
                ),
                strategy = strategy,
                additionalPreferences = listOf(
                    "Ayah: ${state.fatherName}",
                    "Ibu: ${state.motherName}",
                    "Nama: ${state.name}",
                    "Agama: ${state.religion}",
                    "Budaya: ${state.culture}",
                    "Provinsi: ${state.province}",
                    "Makna: ${state.desiredMeaning}",
                    "Kepribadian: ${state.personality}",
                    "Tingkat Keunikan: $uniquenessDesc"
                ).filter { it.split(": ")[1].isNotBlank() }
            )

            var lastError: String? = null
            repeat(MAX_RETRIES) { attempt ->
                generateNamesUseCase(request).fold(
                    onSuccess = { response ->
                        val validation = responseValidator.validate(response)
                        if (validation is AIResponseValidator.ValidationResult.Invalid) {
                            errorLogger.logProviderError(TAG, "VALIDATION_FAILED", null)
                            Log.w(TAG, "Validation failed (attempt ${attempt + 1}): ${validation.errors}")
                            lastError = "Gagal menghasilkan nama. Silakan coba lagi."
                            return@fold
                        }
                        val filtered = guardrailFilter.filter(response, state.religion)
                        val finalResponse = response.copy(recommendations = filtered.recommendations)
                        generateRepository.saveResults(currentProfileId, finalResponse)
                        val historyItem = HistoryItem(
                            id = java.util.UUID.randomUUID().toString(),
                            babyName = "Konsultasi ${state.culture} ${state.religion}".trim().ifEmpty { "Konsultasi Cepat" },
                            gender = state.gender.name.lowercase(),
                            timestamp = System.currentTimeMillis(),
                            recommendationsCount = finalResponse.recommendations.size,
                        )
                        saveToHistoryUseCase(historyItem)
                        _uiState.value = UiState.Success(finalResponse)
                        _generatedProfileId.value = currentProfileId
                        return@launch
                    },
                    onFailure = { error ->
                        val errorType = when {
                            error.message?.contains("timeout", ignoreCase = true) == true -> "TIMEOUT"
                            error.message?.contains("rate limit", ignoreCase = true) == true -> "RATE_LIMIT"
                            error.message?.contains("unavailable", ignoreCase = true) == true -> "PROVIDER_DOWN"
                            else -> "UNKNOWN"
                        }
                        errorLogger.logProviderError(TAG, errorType, error)
                        lastError = errorLogger.sanitizeForUser(errorType)
                        Log.w(TAG, "Attempt ${attempt + 1} failed: $errorType")
                    },
                )
            }

            _uiState.value = UiState.Error(lastError ?: "Gagal menghasilkan nama. Silakan coba lagi.")
        }
    }

    fun resetState() {
        _uiState.value = UiState.Idle
        _formState.value = GenerateFormState()
        _generatedProfileId.value = null
    }

    companion object {
        private const val MAX_RETRIES = 3
        private const val TAG = "GenerateVM"
    }
}
