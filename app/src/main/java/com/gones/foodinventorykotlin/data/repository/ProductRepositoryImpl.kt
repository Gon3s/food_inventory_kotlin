package com.gones.foodinventorykotlin.data.repository

import com.gones.foodinventorykotlin.data.api.RemoteApi
import com.gones.foodinventorykotlin.data.database.ProductDao
import com.gones.foodinventorykotlin.domain.entity.Product
import com.gones.foodinventorykotlin.domain.entity.ProductResult
import com.gones.foodinventorykotlin.domain.repository.ProductRepository

class ProductRepositoryImpl(
    private val remoteApi: RemoteApi,
    private val productDao: ProductDao
) : ProductRepository {
    override suspend fun getProduct(barcode: String): ProductResult {
        val productResultResponse = remoteApi.getProduct(barcode)
        return productResultResponse.toModel()
    }

    override suspend fun insertProduct(product: Product) {
        productDao.insertProduct(product)
    }
}