package com.gones.foodinventorykotlin.domain.repository

import com.gones.foodinventorykotlin.domain.entity.Product
import com.gones.foodinventorykotlin.domain.entity.ProductResult
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getProduct(barcode: String): ProductResult

    suspend fun getProducts(): Flow<List<Product>>

    suspend fun insertProduct(product: Product)
}