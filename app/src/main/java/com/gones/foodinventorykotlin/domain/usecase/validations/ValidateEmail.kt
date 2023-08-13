package com.gones.foodinventorykotlin.domain.usecase.validations

import android.util.Patterns
import com.gones.foodinventorykotlin.R
import com.gones.foodinventorykotlin.common.UiText

class ValidateEmail {
    fun execute(email: String): ValidationResult {
        if (email.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = UiText.StringResource(R.string.email_not_blank)
            )
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return ValidationResult(
                successful = false,
                errorMessage = UiText.StringResource(R.string.email_invalid)
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}