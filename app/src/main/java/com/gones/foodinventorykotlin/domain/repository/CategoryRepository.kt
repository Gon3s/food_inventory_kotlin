package com.gones.foodinventorykotlin.domain.repository

import com.gones.foodinventorykotlin.domain.entity.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    suspend fun getAllCategories(): Flow<List<Category>>
    suspend fun insertCategory(category: Category)
    suspend fun updateCategory(category: Category)
    suspend fun deleteCategory(category: Category)
}