package com.gones.foodinventorykotlin.ui.manageCategories

import com.gones.foodinventorykotlin.domain.entity.Category

sealed class ManageCategoriesEvent {
    data class EnteredName(val name: String) : ManageCategoriesEvent()
    object SaveCategory : ManageCategoriesEvent()
    data class DeleteCategory(val category: Category) : ManageCategoriesEvent()
    object CloseDialog : ManageCategoriesEvent()
    object OpenDialog : ManageCategoriesEvent()
}
