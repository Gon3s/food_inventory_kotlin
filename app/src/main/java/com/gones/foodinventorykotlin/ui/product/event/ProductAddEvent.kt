package com.gones.foodinventorykotlin.ui.product.event

import com.gones.foodinventorykotlin.domain.entity.Product

sealed class ProductAddEvent {
    object LoadProduct: ProductAddEvent()
    data class EnteredName(val name: String): ProductAddEvent()
    data class EnteredBrands(val brands: String): ProductAddEvent()
    data class EnteredQuantity(val quantity: Int): ProductAddEvent()
    data class EnteredExpiryDate(val expiryDate: Long): ProductAddEvent()
    object SaveProduct : ProductAddEvent()
}
