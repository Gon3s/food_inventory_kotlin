package com.gones.foodinventorykotlin.domain.repository

import com.gones.foodinventorykotlin.domain.entity.Product
import com.gones.foodinventorykotlin.domain.entity.ProductResult
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getProductByEanWS(barcode: String): ProductResult
    suspend fun getProducts(): Flow<List<Product>>
    suspend fun insertProduct(product: Product)
    suspend fun getProductsByEan(barcode: String): Flow<List<Product>>
    suspend fun getProductById(id: Int): Flow<Product>
    suspend fun updateProduct(product: Product)
    suspend fun deleteProduct(product: Product)
}