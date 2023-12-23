package com.gones.foodinventorykotlin.ui.drawer

import com.gones.foodinventorykotlin.domain.entity.Category

data class DrawerState(
    val categories: List<Category> = listOf(),
)