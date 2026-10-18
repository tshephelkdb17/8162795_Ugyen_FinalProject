package com.example.s8162795assignment2.domain.model

sealed interface AppResult<out T> {
    data class Success<T>(val data: T) : AppResult<T>
    data class Failure(val error: AppError) : AppResult<Nothing>
}
sealed interface AppError {
    data object InvalidInput : AppError
    data object Unauthorized : AppError
    data object Network : AppError
    data object Timeout : AppError
    data object InvalidResponse : AppError
    data object Unexpected : AppError
    data class Http(val statusCode: Int) : AppError
}
