package com.example.s8162795assignment2.domain.repository

import com.example.s8162795assignment2.domain.model.AppResult
interface AuthRepository {
    suspend fun login(username: String, password: String): AppResult<String>
}
