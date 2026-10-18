package com.example.s8162795assignment2.presentation.common

import com.example.s8162795assignment2.domain.model.AppError
sealed interface UiState<out T> {
    data object Idle : UiState<Nothing>
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>
    data class Error(val error: AppError) : UiState<Nothing>
}
