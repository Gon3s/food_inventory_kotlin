package com.gones.foodinventorykotlin.data.repository

import com.gones.foodinventorykotlin.data.api.RemoteApi
import com.gones.foodinventorykotlin.domain.entity.Product
import com.gones.foodinventorykotlin.domain.entity.ProductResult
import com.gones.foodinventorykotlin.domain.repository.ProductRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class ProductRepositoryImpl(
    private val remoteApi: RemoteApi,
    private val supabaseClient: SupabaseClient,
) : ProductRepository {
    override suspend fun getProduct(barcode: String): ProductResult {
        val productResultResponse = remoteApi.getProduct(barcode)
        return productResultResponse.toModel()
    }

    override suspend fun getProducts(): Flow<List<Product>> = flow {
        val products = withContext(Dispatchers.IO) {
            supabaseClient.postgrest["product"].select().decodeList<Product>()
        }
        emit(products)
    }

    override suspend fun insertProduct(product: Product) {
        supabaseClient.postgrest["product"].insert(product)
    }
}