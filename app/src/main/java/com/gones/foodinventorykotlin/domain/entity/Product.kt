package com.gones.foodinventorykotlin.domain.entity

import kotlinx.datetime.Instant
import kotlinx.datetime.serializers.InstantIso8601Serializer
import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val id: Int = 0,
    val barcode: String,
    var brands: String,
    var categories: String,
    val imageUrl: String,
    var productName: String,
    var expiry_date: Long = 0L,
    @Serializable(with = InstantIso8601Serializer::class)
    var created_at: Instant? = null,
    var consumed: Boolean = false,
    @Serializable(with = InstantIso8601Serializer::class)
    var consumed_at: Instant? = null,
    var note: String? = null,
)

class InvalidProductException(message: String) : Exception(message)