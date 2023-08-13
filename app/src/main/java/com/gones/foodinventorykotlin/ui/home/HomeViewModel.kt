package com.gones.foodinventorykotlin.ui.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gones.foodinventorykotlin.R
import com.gones.foodinventorykotlin.common.UiText
import com.gones.foodinventorykotlin.domain.usecase.AuthentificationUseCase
import com.gones.foodinventorykotlin.domain.usecase.ProductUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class HomeViewModel(
    private val productUseCase: ProductUseCase,
    private val authentificationUseCase: AuthentificationUseCase,
) : ViewModel() {
    private val _state = mutableStateOf(HomeState())
    val state: State<HomeState> = _state

    var eventFlow = MutableSharedFlow<UiEvent>()
        private set

    private var getProductsJob: Job? = null

    fun getProducts() {
        getProductsJob?.cancel()
        getProductsJob = productUseCase.getProducts().onEach { products ->
            _state.value = state.value.copy(products = products)
        }.launchIn(viewModelScope)
    }

    fun logout() {
        CoroutineScope(Dispatchers.IO).launch {
            if (authentificationUseCase.logout()
            ) {
                eventFlow.emit(UiEvent.LoginOk)
            } else {
                eventFlow.emit(UiEvent.ShowSnackbar(UiText.StringResource(resId = R.string.an_error_occured)))
            }
        }
    }

    sealed class UiEvent() {
        data class ShowSnackbar(val message: UiText) : UiEvent()
        object LoginOk : UiEvent()
    }
}