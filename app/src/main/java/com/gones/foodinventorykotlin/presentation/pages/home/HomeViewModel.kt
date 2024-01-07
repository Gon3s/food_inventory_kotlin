package com.gones.foodinventorykotlin.presentation.pages.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gones.foodinventorykotlin.domain.usecase.CategoryUseCase
import com.gones.foodinventorykotlin.domain.usecase.ProductUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus

class HomeViewModel(
    private val productUseCase: ProductUseCase,
    private val categoryUseCase: CategoryUseCase,
) : ViewModel() {
    var state = mutableStateOf(HomeState())
        private set

    fun getProducts() {
        state.value.let { value ->
            state.value = state.value.copy(isLoading = true)
            productUseCase.getProducts(value.categoryId)
                .onEach { products ->
                    state.value = state.value.copy(
                        products = products,
                        isLoading = false
                    )
                }.launchIn(viewModelScope + SupervisorJob())
        }
    }

    fun getCategories() {
        categoryUseCase.getAllCategories().onEach { categories ->
            state.value = state.value.copy(categories = categories)
        }.launchIn(viewModelScope + SupervisorJob())
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.CategorySelected -> {
                state.value = state.value.copy(categoryId = event.categoryId)
                getProducts()
            }

            is HomeEvent.ConsumeProduct -> {
                CoroutineScope(Dispatchers.IO).launch {
                    productUseCase.consumedProduct(event.product)
                    getProducts()
                }
            }
        }
    }
}