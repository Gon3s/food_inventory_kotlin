package com.gones.foodinventorykotlin.ui.pages.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gones.foodinventorykotlin.domain.usecase.CategoryUseCase
import com.gones.foodinventorykotlin.domain.usecase.ProductUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class HomeViewModel(
    private val productUseCase: ProductUseCase,
    private val categoryUseCase: CategoryUseCase,
) : ViewModel() {
    var state = mutableStateOf(HomeState())
        private set

    private var getProductsJob: Job? = null
    private var getCategoriesJob: Job? = null

    fun getProducts() {
        getProductsJob?.cancel()
        getProductsJob = productUseCase.getProducts(state.value.categoryId).onEach { products ->
            state.value = state.value.copy(products = products)
        }.launchIn(viewModelScope)
    }

    fun getCategories() {
        getCategoriesJob?.cancel()
        getCategoriesJob = categoryUseCase.getAllCategories().onEach { categories ->
            state.value = state.value.copy(categories = categories)
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.CategorySelected -> {
                state.value = state.value.copy(categoryId = event.categoryId)
                getProducts()
            }
        }
    }
}