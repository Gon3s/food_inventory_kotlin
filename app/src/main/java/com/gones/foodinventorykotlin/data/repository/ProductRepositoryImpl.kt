package com.gones.foodinventorykotlin.data.repository

import com.gones.foodinventorykotlin.data.api.RemoteApi
import com.gones.foodinventorykotlin.domain.entity.Product
import com.gones.foodinventorykotlin.domain.entity.ProductResult
import com.gones.foodinventorykotlin.domain.repository.ProductRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.postgrest.query.PostgrestResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import timber.log.Timber

class ProductRepositoryImpl(
    private val remoteApi: RemoteApi,
    private val supabaseClient: SupabaseClient,
) : ProductRepository {
    override suspend fun getProductByEanWS(barcode: String): ProductResult {
        val productResultResponse = remoteApi.getProduct(barcode)
        return productResultResponse.toModel()
    }

    override fun getProducts(): Flow<List<Product>> = flow {
        val products = withContext(Dispatchers.IO) {
            supabaseClient.postgrest["product"].select {
                Product::consumed eq false
                order(Product::expiry_date.name, Order.DESCENDING)
            }
                .decodeList<Product>()
        }
        emit(products)
    }

    override suspend fun insertProduct(product: Product) {
        supabaseClient.postgrest["product"].insert(product)
    }

    override fun getProductsByEan(barcode: String): Flow<List<Product>> = flow {
        val product = withContext(Dispatchers.IO) {
            supabaseClient.postgrest["product"].select {
                eq("barcode", barcode)
            }.decodeList<Product>()
        }
        emit(product)
    }

    override fun getProductById(id: Int): Flow<Product> = flow {
        val product = withContext(Dispatchers.IO) {
            supabaseClient.postgrest["product"].select() {
                eq("id", id)
            }.decodeSingle<Product>()
        }
        emit(product)
    }

    override suspend fun updateProduct(product: Product) {
        try {
            Timber.d("DLOG: updateProduct: $product")
            val result: PostgrestResult = supabaseClient.postgrest["product"].update(
                {
                    Product::brands setTo product.brands
                    Product::categories setTo product.categories
                    Product::expiry_date setTo product.expiry_date
                    Product::consumed setTo product.consumed
                    Product::consumed_at setTo product.consumed_at
                    Product::note setTo product.note

                }
            ) {
                Product::id eq product.id
            }
            Timber.d("DLOG: updateProduct: ${result.body}")
        } catch (e: Exception) {
            Timber.e("DLOG: updateProduct: ${e.message}")
        }
    }

    override suspend fun deleteProduct(product: Product) {
        supabaseClient.postgrest["product"].delete {
            eq("id", product.id)
        }
    }
}