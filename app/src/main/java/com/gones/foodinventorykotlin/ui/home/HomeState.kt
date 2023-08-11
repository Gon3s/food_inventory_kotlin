package com.gones.foodinventorykotlin.ui.home

import com.gones.foodinventorykotlin.domain.entity.Product

data class HomeState(
    val products: List<Product> = listOf(),
)
