package com.gones.foodinventorykotlin.domain.repository

import com.gones.foodinventorykotlin.domain.entity.Product
import com.gones.foodinventorykotlin.domain.entity.ProductResult

interface ProductRepository {
    suspend fun getProduct(barcode: String): ProductResult

    suspend fun insertProduct(product: Product)
}