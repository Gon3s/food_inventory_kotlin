package com.gones.foodinventorykotlin.ui.pages.auth.login

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
import kotlinx.coroutines.launch
import timber.log.Timber


class LoginViewModel(
    private val validateEmail: ValidateEmail,
    private val validatePassword: ValidatePassword,
    private val authentificationUseCase: AuthentificationUseCase,
) : ViewModel() {
    var state = mutableStateOf(LoginState())
        private set

    var eventFlow = MutableSharedFlow<UiEvent>()
        private set

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EmailChanged -> {
                val emailResult = validateEmail.execute(event.email)
                if (!emailResult.successful) {
                    state.value = state.value.copy(emailError = emailResult.errorMessage)
                } else {
                    state.value = state.value.copy(emailError = null)
                }
                state.value = state.value.copy(email = event.email)
            }

            is LoginEvent.PasswordChanged -> {
                val passwordResult = validatePassword.execute(event.password)
                if (!passwordResult.successful) {
                    state.value = state.value.copy(passwordError = passwordResult.errorMessage)
                } else {
                    state.value = state.value.copy(passwordError = null)
                }
                state.value = state.value.copy(password = event.password)
            }

            is LoginEvent.Login -> {
                submitData()
            }
        }
    }

    private fun submitData() {
        state.value = state.value.copy(isLoading = true)

        val emailResult = validateEmail.execute(state.value.email)
        val passwordResult = validatePassword.execute(state.value.password)

        val hasError = listOf(emailResult, passwordResult).any { !it.successful }

        if (hasError) {
            state.value = state.value.copy(
                isLoading = false,
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage,
            )
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            if (authentificationUseCase.login(
                    state.value.email,
                    state.value.password
                )
            ) {
                Timber.d("DLOG: LoginOk")
                eventFlow.emit(UiEvent.LoginOk)
            } else {
                Timber.d("DLOG: LoginError")
                eventFlow.emit(UiEvent.ShowSnackbar(UiText.StringResource(resId = R.string.an_error_occured)))
            }
        }
    }

    sealed class UiEvent() {
        data class ShowSnackbar(val message: UiText) : UiEvent()
        object LoginOk : UiEvent()
    }
}