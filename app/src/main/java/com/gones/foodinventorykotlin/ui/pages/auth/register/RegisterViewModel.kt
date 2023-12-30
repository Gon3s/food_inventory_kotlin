package com.gones.foodinventorykotlin.ui.pages.auth.register

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.gones.foodinventorykotlin.R
import com.gones.foodinventorykotlin.common.UiText
import com.gones.foodinventorykotlin.domain.usecase.AuthentificationUseCase
import com.gones.foodinventorykotlin.domain.usecase.validations.ValidateEmail
import com.gones.foodinventorykotlin.domain.usecase.validations.ValidatePassword
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val validateEmail: ValidateEmail,
    private val validatePassword: ValidatePassword,
    private val authentificationUseCase: AuthentificationUseCase,
) : ViewModel() {
    private val _state = mutableStateOf(RegisterState())
    val state: State<RegisterState>
        get() = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow
        get() = _eventFlow.asSharedFlow()

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.EnteredEmail -> {
                val emailResult = validateEmail.execute(event.email)
                if (!emailResult.successful) {
                    _state.value = _state.value.copy(emailError = emailResult.errorMessage)
                } else {
                    _state.value = _state.value.copy(emailError = null)
                }
                _state.value = _state.value.copy(email = event.email)
            }

            is RegisterEvent.EnteredPassword -> {
                val passwordResult = validatePassword.execute(event.password)
                if (!passwordResult.successful) {
                    _state.value = _state.value.copy(passwordError = passwordResult.errorMessage)
                } else {
                    _state.value = _state.value.copy(passwordError = null)
                }
                _state.value = _state.value.copy(password = event.password)
            }

            is RegisterEvent.Register -> {
                submitData()
            }
        }
    }

    private fun submitData() {
        _state.value = _state.value.copy(isLoading = true)

        val emailResult = validateEmail.execute(state.value.email)
        val passwordResult = validatePassword.execute(state.value.password)

        val hasError = listOf(emailResult, passwordResult).any { !it.successful }

        if (hasError) {
            _state.value = _state.value.copy(
                isLoading = false,
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage,
            )
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            if (authentificationUseCase.register(
                    _state.value.email,
                    _state.value.password
                )
            ) {
                _eventFlow.emit(UiEvent.RegisterOk)
            } else {
                _eventFlow.emit(UiEvent.ShowSnackbar(UiText.StringResource(resId = R.string.an_error_occured)))
            }
        }
    }

    sealed class UiEvent() {
        data class ShowSnackbar(val message: UiText) : UiEvent()
        object RegisterOk : UiEvent()
    }
}