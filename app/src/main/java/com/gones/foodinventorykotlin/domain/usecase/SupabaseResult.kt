package com.gones.foodinventorykotlin.domain.usecase

import com.gones.foodinventorykotlin.common.UiText

data class SupabaseResult(
    val successful: Boolean,
    val errorMessage: UiText? = null,
)
