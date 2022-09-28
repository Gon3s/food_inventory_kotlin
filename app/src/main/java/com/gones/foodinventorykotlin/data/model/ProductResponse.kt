package com.gones.foodinventorykotlin.data.model

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.gones.foodinventorykotlin.domain.entity.Product
import com.squareup.moshi.Json

@Keep
data class ProductResponse(
    @field:Json(name = "id")
    var id: Int,
    @field:Json(name = "brands")
    val brands: String,
    @field:Json(name = "categories")
    val categories: String,
    @field:Json(name = "code")
    val code: String,
    @field:Json(name = "image_url")
    val imageUrl: String,
    @field:Json(name = "product_name")
    val productName: String
) {
    fun toModel() = Product(
        brands = brands,
        categories = categories,
        barcode = code,
        imageUrl = imageUrl,
        productName = productName
    )
}
