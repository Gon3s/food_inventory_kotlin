package com.gones.foodinventorykotlin.ui.pages.auth.login

import com.gones.foodinventorykotlin.common.UiText

data class LoginState(
    val isLoading: Boolean = false,
    val email: String = "",
    val emailError: UiText? = null,
    val password: String = "",
    val passwordError: UiText? = null,
)