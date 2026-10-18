package com.example.s8162795assignment2

import com.example.s8162795assignment2.domain.model.AppResult
import com.example.s8162795assignment2.domain.model.Dashboard
import com.example.s8162795assignment2.domain.repository.AuthRepository
import com.example.s8162795assignment2.domain.repository.DashboardRepository

// Constructor-injected interfaces make plain test fakes sufficient; no JVM agent needed.
class FakeAuthRepository : AuthRepository {
    val calls = mutableListOf<Pair<String, String>>()
    var answer: suspend (String, String) -> AppResult<String> = { _, _ -> error("Unexpected call") }
    override suspend fun login(username: String, password: String): AppResult<String> {
        calls += username to password
        return answer(username, password)
    }
}
class FakeDashboardRepository : DashboardRepository {
    val calls = mutableListOf<String>()
    var answer: suspend (String) -> AppResult<Dashboard> = { error("Unexpected call") }
    override suspend fun getDashboard(keypass: String): AppResult<Dashboard> {
        calls += keypass
        return answer(keypass)
    }
}
