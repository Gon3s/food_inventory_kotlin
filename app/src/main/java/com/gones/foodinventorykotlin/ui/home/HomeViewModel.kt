package com.gones.foodinventorykotlin.ui.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gones.foodinventorykotlin.domain.usecase.ProductUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class HomeViewModel(
    private val productUseCase: ProductUseCase,
) : ViewModel() {
    var state = mutableStateOf(HomeState())
        private set

    private var getProductsJob: Job? = null

    fun getProducts() {
        getProductsJob?.cancel()
        getProductsJob = productUseCase.getProducts().onEach { products ->
            state.value = state.value.copy(products = products)
        }.launchIn(viewModelScope)
    }
}