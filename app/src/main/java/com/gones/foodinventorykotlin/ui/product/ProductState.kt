package com.gones.foodinventorykotlin.ui.product

import com.gones.foodinventorykotlin.domain.entity.Product

data class ProductState(
    val product: Product? = null,
    val isLoading: Boolean = false,
    val hasError: Boolean = false,
    val errorMessage: String = "",

    val quantity: Int = 1,
    val type: ProductViewModel.TYPES = ProductViewModel.TYPES.CREATE,
)

data class OtherProductState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val hasError: Boolean = false,
    val errorMessage: String = "",
)
