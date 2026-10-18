package com.example.s8162795assignment2.presentation.dashboard

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.s8162795assignment2.domain.model.AppError
import com.example.s8162795assignment2.domain.model.AppResult
import com.example.s8162795assignment2.domain.model.Dashboard
import com.example.s8162795assignment2.domain.repository.DashboardRepository
import com.example.s8162795assignment2.presentation.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: DashboardRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    // Activity Intent extras become default SavedStateHandle arguments.
    val topic: String = savedStateHandle.get<String>("keypass").orEmpty()
    private val _state = MutableStateFlow<UiState<Dashboard>>(UiState.Idle)
    val state = _state.asStateFlow()
    init { load() }

    fun load() {
        if (_state.value == UiState.Loading) return
        if (topic.isBlank()) { _state.value = UiState.Error(AppError.Unauthorized); return }
        _state.value = UiState.Loading
        viewModelScope.launch {
            _state.value = when (val result = repository.getDashboard(topic)) {
                is AppResult.Success -> UiState.Success(result.data)
                is AppResult.Failure -> UiState.Error(result.error)
            }
        }
    }
}
