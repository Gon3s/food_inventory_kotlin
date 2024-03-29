package com.gones.foodinventorykotlin.data.repository

import com.gones.foodinventorykotlin.domain.entity.Category
import com.gones.foodinventorykotlin.domain.repository.CategoryRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CategoryRepositoryImpl(
    private val supabaseClient: SupabaseClient,
) : CategoryRepository {
    override fun getAllCategories(): Flow<List<Category>> = flow {
        val categories = supabaseClient.postgrest["category"].select {
            filter {
                Category::user_id eq supabaseClient.auth.currentUserOrNull()?.id
            }
        }.decodeList<Category>()

        emit(categories)
    }

    override fun getCategoryByName(name: String): Flow<Category?> = flow {
        val category = supabaseClient.postgrest["category"].select {
            filter {
                Category::name eq name
            }
        }

        emit(category.decodeAsOrNull<Category>())
    }

    override suspend fun insertCategory(category: Category) {
        category.user_id = supabaseClient.auth.currentUserOrNull()?.id
        supabaseClient.postgrest["category"].insert(category)
    }

    override suspend fun updateCategory(category: Category) {
        supabaseClient.postgrest["category"].update({
            Category::name setTo category.name
        }) {
            filter {
                Category::id eq category.id
            }
        }
    }

    override suspend fun deleteCategory(category: Category) {
        // Todo: Error message when category is not empty
        // Todo: Add confirmation dialog

        supabaseClient.postgrest["category"].delete {
            filter {
                Category::id eq category.id
            }
        }
    }
}