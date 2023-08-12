package com.gones.foodinventorykotlin.ui.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.gones.foodinventorykotlin.R
import com.gones.foodinventorykotlin.common.UiText
import com.gones.foodinventorykotlin.domain.usecase.AuthentificationUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch


class LoginViewModel(
    private val authentificationUseCase: AuthentificationUseCase,
) : ViewModel() {
    var state = mutableStateOf(LoginState())
        private set

    var eventFlow = MutableSharedFlow<UiEvent>()
        private set

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.EnteredEmail -> {
                state.value = state.value.copy(email = event.email)
            }

            is LoginEvent.EnteredPassword -> {
                state.value = state.value.copy(password = event.password)
            }

            is LoginEvent.Login -> {
                CoroutineScope(Dispatchers.IO).launch {
                    if (authentificationUseCase.login(
                            state.value.email,
                            state.value.password
                        )
                    ) {
                        eventFlow.emit(UiEvent.LoginOk)
                    } else {
                        eventFlow.emit(UiEvent.ShowSnackbar(UiText.StringResource(resId = R.string.an_error_occured)))
                    }
                }
            }
        }
    }

    sealed class UiEvent() {
        data class ShowSnackbar(val message: UiText) : UiEvent()
        object LoginOk : UiEvent()
    }
}