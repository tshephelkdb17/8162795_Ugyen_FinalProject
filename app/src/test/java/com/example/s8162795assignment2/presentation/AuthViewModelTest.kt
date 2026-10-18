package com.example.s8162795assignment2.presentation

import com.example.s8162795assignment2.MainDispatcherRule
import com.example.s8162795assignment2.domain.model.AppError
import com.example.s8162795assignment2.domain.model.AppResult
import com.example.s8162795assignment2.domain.repository.AuthRepository
import com.example.s8162795assignment2.presentation.common.UiState
import com.example.s8162795assignment2.presentation.login.AuthViewModel
import com.example.s8162795assignment2.FakeAuthRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {
    @get:Rule val main = MainDispatcherRule()
    private val repository = FakeAuthRepository()

    @Test fun `invalid username never reaches API`() = runTest {
        val vm = AuthViewModel(repository)
        vm.login("s8162795", "Example")
        assertEquals(UiState.Error(AppError.InvalidInput), vm.state.value)
        assertEquals(0, repository.calls.size)
    }
    @Test fun `blank password never reaches API`() = runTest {
        val vm = AuthViewModel(repository)
        vm.login("8162795", " ")
        assertEquals(UiState.Error(AppError.InvalidInput), vm.state.value)
        assertEquals(0, repository.calls.size)
    }
    @Test fun `loading becomes success and password is unchanged`() = runTest {
        repository.answer = { _, _ -> AppResult.Success("topic") }
        val vm = AuthViewModel(repository)
        vm.login(" 8162795 ", "ExAmple")
        assertEquals(UiState.Loading, vm.state.value)
        advanceUntilIdle()
        assertEquals(UiState.Success("topic"), vm.state.value)
        assertEquals(listOf("8162795" to "ExAmple"), repository.calls)
        vm.navigationHandled()
        assertEquals(UiState.Idle, vm.state.value)
    }
    @Test fun `failure is visible and retry can succeed`() = runTest {
        repository.answer = { _, _ ->
            if (repository.calls.size == 1) AppResult.Failure(AppError.Unauthorized) else AppResult.Success("topic")
        }
        val vm = AuthViewModel(repository)
        vm.login("8162795", "Example")
        advanceUntilIdle()
        assertEquals(UiState.Error(AppError.Unauthorized), vm.state.value)
        vm.login("8162795", "Example")
        advanceUntilIdle()
        assertEquals(UiState.Success("topic"), vm.state.value)
    }
    @Test fun `duplicate tap makes one request`() = runTest {
        repository.answer = { _, _ -> AppResult.Success("topic") }
        val vm = AuthViewModel(repository)
        vm.login("8162795", "Example")
        vm.login("8162795", "Example")
        advanceUntilIdle()
        assertEquals(1, repository.calls.size)
    }
}
