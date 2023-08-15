package com.gones.foodinventorykotlin.data.repository

import com.gones.foodinventorykotlin.domain.entity.Category
import com.gones.foodinventorykotlin.domain.repository.CategoryRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CategoryRepositoryImpl(
    private val supabaseClient: SupabaseClient,
) : CategoryRepository {
    override suspend fun getAllCategories(): Flow<List<Category>> = flow {
        val categories = supabaseClient.postgrest["category"].select {
            Category::user_id eq supabaseClient.gotrue.currentUserOrNull()?.id
        }.decodeList<Category>()

        emit(categories)
    }

    override suspend fun insertCategory(category: Category) {
        category.user_id = supabaseClient.gotrue.currentUserOrNull()?.id
        supabaseClient.postgrest["category"].insert(category)
    }

    override suspend fun updateCategory(category: Category) {
        supabaseClient.postgrest["category"].update({
            Category::name setTo category.name
        }) {
            Category::id eq category.id
        }
    }

    override suspend fun deleteCategory(category: Category) {
        supabaseClient.postgrest["category"].delete {
            Category::id eq category.id
        }
    }
}