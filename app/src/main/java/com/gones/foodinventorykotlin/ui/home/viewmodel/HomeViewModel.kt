package com.gones.foodinventorykotlin.ui.home.viewmodel

import androidx.lifecycle.ViewModel
import com.gones.foodinventorykotlin.domain.usecase.ProductUseCase

class HomeViewModel(
    private val productUseCase: ProductUseCase,
) : ViewModel() {

    suspend fun getProducts() = productUseCase.getProducts()
}