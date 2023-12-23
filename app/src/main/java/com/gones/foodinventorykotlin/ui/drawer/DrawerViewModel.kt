package com.gones.foodinventorykotlin.ui.drawer

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gones.foodinventorykotlin.R
import com.gones.foodinventorykotlin.common.UiText
import com.gones.foodinventorykotlin.domain.usecase.AuthentificationUseCase
import com.gones.foodinventorykotlin.domain.usecase.CategoryUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

class DrawerViewModel(
    private val categoryUseCase: CategoryUseCase,
    private val authentificationUseCase: AuthentificationUseCase
) : ViewModel() {
    var state = mutableStateOf(DrawerState())
        private set

    var eventFlow = MutableSharedFlow<UiEvent>()
        private set

    private var getCategoriesJob: Job? = null

    fun getCategories() {
        Timber.d("DLOG: getCategories")
        getCategoriesJob?.cancel()
        getCategoriesJob = categoryUseCase.getAllCategories().onEach { categories ->
            state.value = state.value.copy(categories = categories)
        }.launchIn(viewModelScope)
    }

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
        object LogoutOk : UiEvent()
    }
}