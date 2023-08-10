package com.gones.foodinventorykotlin.domain.entity

import kotlinx.datetime.Instant
import kotlinx.datetime.serializers.InstantIso8601Serializer
import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Int = 0,
    val barcode: String? = null,
    var brands: String? = null,
    var categories: String? = null,
    val image_url: String? = null,
    var product_name: String,
    var expiry_date: Long? = null,
    @Serializable(with = InstantIso8601Serializer::class)
    var created_at: Instant? = null,
    var consumed: Boolean = false,
    @Serializable(with = InstantIso8601Serializer::class)
    var consumed_at: Instant? = null,
    var note: String? = null,
)

class InvalidProductException(message: String) : Exception(message)