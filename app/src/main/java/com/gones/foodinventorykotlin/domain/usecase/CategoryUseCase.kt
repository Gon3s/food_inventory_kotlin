package com.gones.foodinventorykotlin.domain.usecase

import com.gones.foodinventorykotlin.R
import com.gones.foodinventorykotlin.common.UiText
import com.gones.foodinventorykotlin.domain.entity.Category
import com.gones.foodinventorykotlin.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

class CategoryUseCase(
    private val categoryRepository: CategoryRepository,
) {
    fun getAllCategories(): Flow<List<Category>> = categoryRepository.getAllCategories()

    suspend fun addCategory(category: Category): SupabaseResult {
        if (category.name.isBlank()) {
            return SupabaseResult(
                successful = false,
                errorMessage = UiText.StringResource(R.string.category_name_is_required)
            )
        }

        val existingCategory = categoryRepository.getCategoryByName(category.name).firstOrNull()
        if (existingCategory != null) {
            return SupabaseResult(
                successful = false,
                errorMessage = UiText.StringResource(R.string.category_already_exists)
            )
        }

        try {
            categoryRepository.insertCategory(category)
        } catch (e: Exception) {
            return SupabaseResult(
                successful = false,
                errorMessage = UiText.StringResource(R.string.an_error_occured)
            )
        }
        return SupabaseResult(successful = true)
    }

    suspend fun updateCategory(category: Category): SupabaseResult {
        if (category.name.isBlank()) {
            return SupabaseResult(
                successful = false,
                errorMessage = UiText.StringResource(R.string.category_name_is_required)
            )
        }

        try {
            categoryRepository.updateCategory(category)
        } catch (e: Exception) {
            return SupabaseResult(
                successful = false,
                errorMessage = UiText.StringResource(R.string.an_error_occured)
            )
        }
        return SupabaseResult(successful = true)
    }

    suspend fun deleteCategory(category: Category): SupabaseResult {
        try {
            categoryRepository.deleteCategory(category)
        } catch (e: Exception) {
            return SupabaseResult(
                successful = false,
                errorMessage = UiText.StringResource(R.string.an_error_occured)
            )
        }
        return SupabaseResult(successful = true)
    }
}