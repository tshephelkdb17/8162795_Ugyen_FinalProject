package com.example.s8162795assignment2.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.s8162795assignment2.domain.model.AppError
import com.example.s8162795assignment2.domain.model.AppResult
import com.example.s8162795assignment2.domain.repository.AuthRepository
import com.example.s8162795assignment2.presentation.common.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(private val repository: AuthRepository) : ViewModel() {
    private val _state = MutableStateFlow<UiState<String>>(UiState.Idle)
    val state = _state.asStateFlow()

    fun login(username: String, password: String) {
        if (_state.value == UiState.Loading || _state.value is UiState.Success) return
        val id = username.trim()
        if (!id.matches(Regex("[0-9]+")) || password.isBlank()) {
            _state.value = UiState.Error(AppError.InvalidInput)
            return
        }
        // Synchronous assignment also prevents duplicate button taps.
        _state.value = UiState.Loading
        viewModelScope.launch {
            _state.value = when (val result = repository.login(id, password)) {
                is AppResult.Success -> UiState.Success(result.data)
                is AppResult.Failure -> UiState.Error(result.error)
            }
        }
    }
    fun navigationHandled() { if (_state.value is UiState.Success) _state.value = UiState.Idle }
}
