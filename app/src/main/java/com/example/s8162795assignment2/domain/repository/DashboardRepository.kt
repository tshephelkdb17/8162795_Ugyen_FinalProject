package com.example.s8162795assignment2.domain.repository

import com.example.s8162795assignment2.domain.model.AppResult
import com.example.s8162795assignment2.domain.model.Dashboard
interface DashboardRepository {
    suspend fun getDashboard(keypass: String): AppResult<Dashboard>
}
