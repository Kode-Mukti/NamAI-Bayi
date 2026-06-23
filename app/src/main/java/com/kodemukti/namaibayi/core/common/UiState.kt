package com.kodemukti.namaibayi.core.common

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

sealed class UiState<out T> {
    data object Idle : UiState<Nothing>()
    data object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}

fun <T> Flow<T>.asUiState(): Flow<UiState<T>> = this
    .map { UiState.Success(it) as UiState<T> }
    .onStart { emit(UiState.Loading) }
    .catch { emit(UiState.Error(it.message ?: "Unknown error")) }
