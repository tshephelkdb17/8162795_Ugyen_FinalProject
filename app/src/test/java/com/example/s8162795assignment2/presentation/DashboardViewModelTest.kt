package com.example.s8162795assignment2.presentation

import androidx.lifecycle.SavedStateHandle
import com.example.s8162795assignment2.MainDispatcherRule
import com.example.s8162795assignment2.domain.model.AppError
import com.example.s8162795assignment2.domain.model.AppResult
import com.example.s8162795assignment2.domain.model.Dashboard
import com.example.s8162795assignment2.domain.repository.DashboardRepository
import com.example.s8162795assignment2.presentation.common.UiState
import com.example.s8162795assignment2.presentation.dashboard.DashboardViewModel
import com.example.s8162795assignment2.FakeDashboardRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {
    @get:Rule val main = MainDispatcherRule()
    private val repository = FakeDashboardRepository()
    @Test fun `missing keypass prevents request`() = runTest {
        val vm = DashboardViewModel(repository, SavedStateHandle())
        assertEquals(UiState.Error(AppError.Unauthorized), vm.state.value)
        assertEquals(0, repository.calls.size)
    }
    @Test fun `empty list remains a successful dashboard`() = runTest {
        val data = Dashboard(emptyList(), 0)
        repository.answer = { AppResult.Success(data) }
        val vm = DashboardViewModel(repository, SavedStateHandle(mapOf("keypass" to "topic")))
        assertEquals(UiState.Loading, vm.state.value)
        advanceUntilIdle()
        assertEquals(UiState.Success(data), vm.state.value)
    }
    @Test fun `network failure supports retry`() = runTest {
        val data = Dashboard(emptyList(), 0)
        repository.answer = {
            if (repository.calls.size == 1) AppResult.Failure(AppError.Network) else AppResult.Success(data)
        }
        val vm = DashboardViewModel(repository, SavedStateHandle(mapOf("keypass" to "topic")))
        advanceUntilIdle()
        assertEquals(UiState.Error(AppError.Network), vm.state.value)
        vm.load()
        advanceUntilIdle()
        assertEquals(UiState.Success(data), vm.state.value)
        assertEquals(listOf("topic", "topic"), repository.calls)
    }
}
