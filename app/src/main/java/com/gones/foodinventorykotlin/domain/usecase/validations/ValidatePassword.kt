package com.gones.foodinventorykotlin.domain.usecase.validations

import com.gones.foodinventorykotlin.R
import com.gones.foodinventorykotlin.common.UiText

class ValidatePassword {
    fun execute(password: String): ValidationResult {
        if (password.length < 6) {
            return ValidationResult(
                successful = false,
                errorMessage = UiText.StringResource(R.string.password_too_short, 6)
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}