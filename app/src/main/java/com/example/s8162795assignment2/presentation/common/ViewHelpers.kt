package com.example.s8162795assignment2.presentation.common

import android.content.Context
import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.s8162795assignment2.R
import com.example.s8162795assignment2.domain.model.AppError
import java.util.Locale

/** Add system-bar/keyboard insets without accumulating padding on redispatch. */
fun View.applySystemInsets() {
    val left = paddingLeft; val top = paddingTop; val right = paddingRight; val bottom = paddingBottom
    ViewCompat.setOnApplyWindowInsetsListener(this) { view, insets ->
        val safe = insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime())
        view.setPadding(left + safe.left, top + safe.top, right + safe.right, bottom + safe.bottom)
        insets
    }
    ViewCompat.requestApplyInsets(this)
}
fun String.fieldLabel(): String = replace(Regex("([a-z])([A-Z])"), "$1 $2")
    .replace('_', ' ').replace('-', ' ')
    .replaceFirstChar { it.titlecase(Locale.getDefault()) }

fun Context.errorMessage(error: AppError): String = getString(when (error) {
    AppError.InvalidInput -> R.string.error_input
    AppError.Unauthorized -> R.string.error_auth
    AppError.Network -> R.string.error_network
    AppError.Timeout -> R.string.error_timeout
    AppError.InvalidResponse -> R.string.error_response
    AppError.Unexpected -> R.string.error_unexpected
    is AppError.Http -> when (error.statusCode) {
        400 -> R.string.error_request
        404 -> R.string.error_not_found
        429 -> R.string.error_rate_limit
        in 500..599 -> R.string.error_server
        else -> R.string.error_unexpected
    }
})
