package com.example.s8162795assignment2.data.repository

import com.example.s8162795assignment2.domain.model.AppError
import com.example.s8162795assignment2.domain.model.AppResult
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException

internal class InvalidApiResponseException : Exception()
internal suspend fun <T> apiCall(block: suspend () -> T): AppResult<T> = try {
    AppResult.Success(block())
} catch (error: CancellationException) {
    // Structured cancellation is control flow, not a network failure.
    throw error
} catch (error: HttpException) {
    AppResult.Failure(if (error.code() in listOf(401, 403)) AppError.Unauthorized else AppError.Http(error.code()))
} catch (error: SocketTimeoutException) {
    AppResult.Failure(AppError.Timeout)
} catch (error: JsonParseException) {
    AppResult.Failure(AppError.InvalidResponse)
} catch (error: InvalidApiResponseException) {
    AppResult.Failure(AppError.InvalidResponse)
} catch (error: MalformedJsonException) {
    AppResult.Failure(AppError.InvalidResponse)
} catch (error: IOException) {
    AppResult.Failure(AppError.Network)
} catch (error: Exception) {
    AppResult.Failure(AppError.Unexpected)
}
