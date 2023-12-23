package com.gones.foodinventorykotlin.data.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    var id: String = "",
    var email: String = "",
    var first_name: String = "",
    var last_name: String = "",
    var pseudo: String = ""
)