package com.gones.foodinventorykotlin.domain.entity

import android.os.Parcelable
import androidx.annotation.Keep
import com.squareup.moshi.Json
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
data class ProductResult(
    val status:Int,
    val product:Product?
): Parcelable
