package com.gones.foodinventorykotlin.ui.manageCategories

import com.gones.foodinventorykotlin.common.UiText
import com.gones.foodinventorykotlin.domain.entity.Category

data class ManageCategoriesState(
    val categories: List<Category> = listOf(),
    val category: Category = Category(),
    val openDialog: Boolean = false,
    val nameError: UiText? = null,
)
