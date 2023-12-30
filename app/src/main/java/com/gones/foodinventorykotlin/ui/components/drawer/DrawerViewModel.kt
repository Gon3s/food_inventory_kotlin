package com.gones.foodinventorykotlin.ui.components.drawer

import androidx.lifecycle.ViewModel
import com.gones.foodinventorykotlin.R
import com.gones.foodinventorykotlin.common.UiText
import com.gones.foodinventorykotlin.domain.usecase.AuthentificationUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class DrawerViewModel(
    private val authentificationUseCase: AuthentificationUseCase
) : ViewModel() {
    var eventFlow = MutableSharedFlow<UiEvent>()
        private set

    fun logout() {
        CoroutineScope(Dispatchers.IO).launch {
            if (authentificationUseCase.logout()
            ) {
                eventFlow.emit(UiEvent.LogoutOk)
            } else {
                eventFlow.emit(UiEvent.ShowSnackbar(UiText.StringResource(resId = R.string.an_error_occured)))
            }
        }
    }

    fun getCurrentUser() = authentificationUseCase.getCurrentUser()

    sealed class UiEvent() {
        data class ShowSnackbar(val message: UiText) : UiEvent()
        data object LogoutOk : UiEvent()
    }
}