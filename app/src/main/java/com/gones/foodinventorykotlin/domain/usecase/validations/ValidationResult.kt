package com.gones.foodinventorykotlin.domain.usecase.validations

import com.gones.foodinventorykotlin.common.UiText

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: UiText? = null,
)
