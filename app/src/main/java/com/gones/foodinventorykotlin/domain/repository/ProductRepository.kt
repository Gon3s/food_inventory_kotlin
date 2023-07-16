package com.gones.foodinventorykotlin.domain.repository

import androidx.lifecycle.LiveData
import com.gones.foodinventorykotlin.domain.entity.Product
import com.gones.foodinventorykotlin.domain.entity.ProductResult

interface ProductRepository {
    suspend fun getProduct(barcode: String): ProductResult

    fun getProduct(): LiveData<List<Product>>

    suspend fun insertProduct(product: Product)
}