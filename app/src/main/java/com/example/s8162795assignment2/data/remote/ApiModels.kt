package com.example.s8162795assignment2.data.remote

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("username") val username: String,
    @SerializedName("password") val password: String
)
data class LoginResponse(@SerializedName("keypass") val keypass: String? = null)
// The API assigns different topics. Retain every field instead of assuming a schema.
data class DashboardResponse(
    @SerializedName("entities") val entities: List<JsonObject?>? = null,
    @SerializedName("entityTotal") val entityTotal: Int? = null
)
