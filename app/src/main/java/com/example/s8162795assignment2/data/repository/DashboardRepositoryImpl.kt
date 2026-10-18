package com.example.s8162795assignment2.data.repository

import com.example.s8162795assignment2.data.remote.AssignmentApi
import com.example.s8162795assignment2.di.IoDispatcher
import com.example.s8162795assignment2.domain.model.AppResult
import com.example.s8162795assignment2.domain.model.Dashboard
import com.example.s8162795assignment2.domain.model.Entity
import com.example.s8162795assignment2.domain.model.EntityField
import com.example.s8162795assignment2.domain.repository.DashboardRepository
import com.google.gson.JsonElement
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DashboardRepositoryImpl @Inject constructor(
    private val api: AssignmentApi,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) : DashboardRepository {
    override suspend fun getDashboard(keypass: String): AppResult<Dashboard> = withContext(dispatcher) {
        apiCall {
            val response = api.getDashboard(keypass)
            val objects = response.entities ?: throw InvalidApiResponseException()
            val total = response.entityTotal?.takeIf { it >= 0 } ?: throw InvalidApiResponseException()
            Dashboard(objects.map { item ->
                val json = item ?: throw InvalidApiResponseException()
                Entity(json.entrySet().map { EntityField(it.key, it.value.displayValue()) })
            }, total)
        }
    }
}
private fun JsonElement.displayValue(): String = when {
    isJsonNull -> "—"
    isJsonPrimitive -> asString
    else -> toString() // Nested fields are retained, not discarded.
}
