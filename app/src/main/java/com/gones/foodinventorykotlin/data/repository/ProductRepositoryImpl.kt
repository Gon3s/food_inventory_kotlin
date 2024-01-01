package com.gones.foodinventorykotlin.data.repository

import com.gones.foodinventorykotlin.common.ExpirySections
import com.gones.foodinventorykotlin.data.api.RemoteApi
import com.gones.foodinventorykotlin.domain.entity.Product
import com.gones.foodinventorykotlin.domain.entity.ProductResult
import com.gones.foodinventorykotlin.domain.repository.ProductRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Order
import io.github.jan.supabase.postgrest.result.PostgrestResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

class ProductRepositoryImpl(
    private val remoteApi: RemoteApi,
    private val supabaseClient: SupabaseClient,
) : ProductRepository {
    override suspend fun getProductByEanWS(barcode: String): ProductResult {
        val productResultResponse = remoteApi.getProduct(barcode)

        return if (productResultResponse.product != null) {
            productResultResponse.toModel()

        } else {
            ProductResult(
                status = productResultResponse.status,
                product = Product(
                    barcode = barcode,
                ),
            )
        }
    }

    override fun getProducts(categoryId: Int?): Flow<Map<ExpirySections, List<Product>>> =
        flow {
            val products = withContext(Dispatchers.IO) {
                supabaseClient.postgrest["product"].select {
                    filter {
                        Product::consumed eq false
                        Product::user_id eq supabaseClient.auth.currentUserOrNull()?.id
                        if (categoryId != null) {
                            Product::category_id eq categoryId
                        }
                    }
                    order(
                        Product::expiry_date.name, Order.ASCENDING
                    )
                }
                    .decodeList<Product>()
            }

            val groupedProducts = products.groupBy { product ->
                product.expiry_date?.let { expiryDate ->
                    val temporalExpiryDate = LocalDateTime.ofInstant(
                        Instant.ofEpochMilli(expiryDate),
                        ZoneId.systemDefault()
                    )
                    val daysToExpiry = ChronoUnit.DAYS.between(LocalDate.now(), temporalExpiryDate)
                    when {
                        daysToExpiry < 0 -> ExpirySections.Expired
                        daysToExpiry in 0..3 -> ExpirySections.ExpiredIn3Days
                        daysToExpiry in 4..15 -> ExpirySections.ExpireIn15Days
                        daysToExpiry in 16..30 -> ExpirySections.ExpireIn1Month
                        daysToExpiry in 31..90 -> ExpirySections.ExpireIn3Month
                        else -> ExpirySections.ExpireIn3MonthPlus
                    }
                } ?: ExpirySections.NoExpiry
            }

            emit(groupedProducts)
        }

    override suspend fun insertProduct(product: Product) {
        product.user_id = supabaseClient.auth.currentUserOrNull()?.id
        supabaseClient.postgrest["product"].insert(product)
    }

    override fun getProductsByEan(barcode: String): Flow<List<Product>> = flow {
        val products = withContext(Dispatchers.IO) {
            supabaseClient.postgrest["product"].select {
                filter {
                    Product::consumed eq false
                    eq("barcode", barcode)
                    Product::user_id eq supabaseClient.auth.currentUserOrNull()?.id
                }
            }.decodeList<Product>()
        }
        emit(products)
    }

    override fun getProductById(id: Int): Flow<Product> = flow {
        val product = withContext(Dispatchers.IO) {
            supabaseClient.postgrest["product"].select() {
                filter {
                    eq("id", id)
                }
            }.decodeSingle<Product>()
        }
        emit(product)
    }

    override suspend fun updateProduct(product: Product) {
        try {
            Timber.d("DLOG: updateProduct: $product")
            val result: PostgrestResult = supabaseClient.postgrest["product"].update(
                {
                    Product::product_name setTo product.product_name
                    Product::expiry_date setTo product.expiry_date
                    Product::consumed setTo product.consumed
                    Product::consumed_at setTo product.consumed_at
                    Product::note setTo product.note
                    Product::category_id setTo product.category_id
                }
            ) {
                filter {
                    Product::id eq product.id
                }
            }
            Timber.d("DLOG: updateProduct: ${result.data}")
        } catch (e: Exception) {
            Timber.e("DLOG: updateProduct: ${e.message}")
        }
    }

    override suspend fun deleteProduct(product: Product) {
        supabaseClient.postgrest["product"].delete {
            filter {
                eq("id", product.id)
            }
        }
    }
}