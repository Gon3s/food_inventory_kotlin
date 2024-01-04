package com.gones.foodinventorykotlin.presentation.pages.home

import com.gones.foodinventorykotlin.domain.entity.Product

sealed class HomeEvent {
    data class CategorySelected(val categoryId: Int?) : HomeEvent()
    data class ConsumeProduct(val product: Product) : HomeEvent()
}