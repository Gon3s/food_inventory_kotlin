package com.gones.foodinventorykotlin.domain.entity

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Keep
@Parcelize
data class ProductResult(
    val status: Int,
    val product: @RawValue Product?,
) : Parcelable
