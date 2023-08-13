package com.gones.foodinventorykotlin.ui.auth.register

import com.gones.foodinventorykotlin.common.UiText

data class RegisterState(
    val isLoading: Boolean = false,
    val email: String = "",
    val emailError: UiText? = null,
    val password: String = "",
    val passwordError: UiText? = null,
)
