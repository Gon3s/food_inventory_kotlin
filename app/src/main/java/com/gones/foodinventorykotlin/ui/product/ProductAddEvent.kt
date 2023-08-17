package com.gones.foodinventorykotlin.ui.product

sealed class ProductAddEvent {
    data class EnteredName(val name: String) : ProductAddEvent()
    data class SelectedCategory(val categoryId: Int) : ProductAddEvent()
    data class ExpandedCategory(val expanded: Boolean) : ProductAddEvent()
    data class EnteredNote(val note: String) : ProductAddEvent()
    data class EnteredQuantity(val quantity: Int) : ProductAddEvent()
    object DecreaseQuantity : ProductAddEvent()
    object IncreaseQuantity : ProductAddEvent()
    data class EnteredExpiryDate(val expiryDate: Long) : ProductAddEvent()
    object SaveProduct : ProductAddEvent()
    object Consume : ProductAddEvent()
}
