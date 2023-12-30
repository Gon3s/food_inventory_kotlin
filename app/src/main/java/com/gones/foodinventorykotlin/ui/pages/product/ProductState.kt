package com.gones.foodinventorykotlin.ui.pages.product

import com.gones.foodinventorykotlin.common.UiText
import com.gones.foodinventorykotlin.domain.entity.Category
import com.gones.foodinventorykotlin.domain.entity.Product

data class ProductState(
    val product: Product = Product(),
    val isLoading: Boolean = false,
    val hasError: Boolean = false,
    val errorMessage: UiText = UiText.DynamicString(""),

    val quantity: Int = 1,
    val type: ProductViewModel.TYPES = ProductViewModel.TYPES.CREATE,

    val categories: List<Category> = emptyList(),
    val categoryExpanded: Boolean = false,

    val nameError: UiText = UiText.DynamicString(""),
)

data class OtherProductState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val hasError: Boolean = false,
    val errorMessage: UiText = UiText.DynamicString(""),
)