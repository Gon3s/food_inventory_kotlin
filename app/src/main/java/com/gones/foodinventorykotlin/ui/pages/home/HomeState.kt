package com.gones.foodinventorykotlin.ui.pages.home

import com.gones.foodinventorykotlin.common.ExpirySections
import com.gones.foodinventorykotlin.domain.entity.Category
import com.gones.foodinventorykotlin.domain.entity.Product

data class HomeState(
    val isRefreshing: Boolean = false,
    val products: Map<ExpirySections, List<Product>> = mapOf(),
    val categories: List<Category> = listOf(),
    val categoryId: Int? = null,
)
