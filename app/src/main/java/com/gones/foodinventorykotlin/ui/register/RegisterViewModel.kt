package com.gones.foodinventorykotlin.ui.register

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.gones.foodinventorykotlin.R
import com.gones.foodinventorykotlin.common.UiText
import com.gones.foodinventorykotlin.domain.usecase.AuthentificationUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val authentificationUseCase: AuthentificationUseCase,
) : ViewModel(

) {
    private val _state = mutableStateOf(RegisterState())
    val state: State<RegisterState>
        get() = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow
        get() = _eventFlow.asSharedFlow()

    fun onEvent(event: RegisterEvent) {
        when (event) {
            is RegisterEvent.EnteredEmail -> {
                _state.value = _state.value.copy(email = event.email)
            }

            is RegisterEvent.EnteredPassword -> {
                _state.value = _state.value.copy(password = event.password)
            }

            is RegisterEvent.Register -> {
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
        }
    }

    sealed class UiEvent() {
        data class ShowSnackbar(val message: UiText) : UiEvent()
        object RegisterOk : UiEvent()
    }
}