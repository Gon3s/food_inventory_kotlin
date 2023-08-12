package com.gones.foodinventorykotlin.ui.register

sealed class RegisterEvent {
    data class EnteredEmail(val email: String) : RegisterEvent()
    data class EnteredPassword(val password: String) : RegisterEvent()
    object Register : RegisterEvent()
}
