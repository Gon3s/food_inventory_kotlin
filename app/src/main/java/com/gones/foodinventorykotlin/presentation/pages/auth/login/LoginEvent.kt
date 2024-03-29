package com.gones.foodinventorykotlin.presentation.pages.auth.login

sealed class LoginEvent {
    data class EmailChanged(val email: String) : LoginEvent()
    data class PasswordChanged(val password: String) : LoginEvent()
    object Login : LoginEvent()
}