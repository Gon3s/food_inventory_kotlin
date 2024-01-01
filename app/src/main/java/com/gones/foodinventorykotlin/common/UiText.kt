package com.gones.foodinventorykotlin.common

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

/**
 * Source: https://github.com/philipplackner/UniversalStringResources/blob/final/app/src/main/java/com/plcoding/universalstringresources/UiText.kt
 */
sealed class UiText {
    data class DynamicString(val value: String) : UiText()


    class StringResource(
        @StringRes val resId: Int,
        vararg val args: Any,
    ) : UiText(), Comparable<StringResource> {
        override fun compareTo(other: StringResource): Int {
            return this.resId.compareTo(other.resId)
        }
    }

    class PluralResource(
        @StringRes val resId: Int,
        val quantity: Int,
        vararg val args: Any,
    ) : UiText()

    @Composable
    fun asString(): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> stringResource(resId, *args)
            is PluralResource -> stringResource(resId, quantity, *args)
        }
    }

    fun asString(context: Context): String {
        return when (this) {
            is DynamicString -> value
            is StringResource -> context.getString(resId, *args)
            is PluralResource -> context.resources.getQuantityString(resId, quantity, *args)
        }
    }
}