package com.gones.foodinventorykotlin.ui.pages.home

import com.gones.foodinventorykotlin.domain.entity.Category
import com.gones.foodinventorykotlin.domain.entity.Product

data class HomeState(
    val products: List<Product> = listOf(),
    val categories: List<Category> = listOf(),
    val categoryId: Int? = null,
)
