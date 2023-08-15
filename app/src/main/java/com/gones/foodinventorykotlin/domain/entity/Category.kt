package com.gones.foodinventorykotlin.domain.entity

import kotlinx.datetime.Instant
import kotlinx.datetime.serializers.InstantIso8601Serializer
import kotlinx.serialization.Serializable

@Serializable
data class Category(
    var id: Int,
    var name: String,
    @Serializable(with = InstantIso8601Serializer::class)
    var created_at: Instant? = null,
    var user_id: String? = null,
)
