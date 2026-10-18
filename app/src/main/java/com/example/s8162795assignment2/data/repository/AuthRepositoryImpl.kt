package com.example.s8162795assignment2.data.repository

import com.example.s8162795assignment2.data.remote.AssignmentApi
import com.example.s8162795assignment2.data.remote.LoginRequest
import com.example.s8162795assignment2.di.IoDispatcher
import com.example.s8162795assignment2.domain.model.AppResult
import com.example.s8162795assignment2.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AssignmentApi,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : AuthRepository {
    override suspend fun login(username: String, password: String): AppResult<String> = withContext(dispatcher) {
        apiCall {
            // Preserve case and whitespace in the password; never silently modify it.
            api.login(LoginRequest(username, password)).keypass
                ?.takeIf { it.isNotBlank() } ?: throw InvalidApiResponseException()
        }
    }
}
