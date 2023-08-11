package com.gones.foodinventorykotlin.data.model

import androidx.annotation.Keep
import com.gones.foodinventorykotlin.domain.entity.Product
import com.squareup.moshi.Json

@Keep
data class ProductResponse(
    @field:Json(name = "id")
    var id: Int,
    val categories: String,
    @field:Json(name = "code")
    val code: String,
    @field:Json(name = "image_url")
    val imageUrl: String,
    @field:Json(name = "product_name")
    val productName: String,
) {
    fun toModel() = Product(
        barcode = code,
        image_url = imageUrl,
        product_name = productName
    )
}
