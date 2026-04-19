package com.example.artistico_android.core.common

import android.content.Context
import androidx.annotation.StringRes

/**
 * Framework-free text wrapper so ViewModels can emit messages without holding a Context.
 */
sealed interface UiText {
    data class Dynamic(val value: String) : UiText
    data class StringResource(@StringRes val resId: Int, val args: List<Any> = emptyList()) : UiText

    fun asString(context: Context): String = when (this) {
        is Dynamic -> value
        is StringResource -> context.getString(resId, *args.toTypedArray())
    }

    companion object {
        fun of(@StringRes resId: Int, vararg args: Any): UiText = StringResource(resId, args.toList())
        fun of(value: String): UiText = Dynamic(value)
    }
}
