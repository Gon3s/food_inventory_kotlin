package com.gones.foodinventorykotlin.domain.usecase

import android.content.res.Resources
import com.gones.foodinventorykotlin.domain.entity.InvalidProductException
import com.gones.foodinventorykotlin.domain.entity.Product
import com.gones.foodinventorykotlin.domain.repository.ProductRepository
import com.gones.foodinventorykotlin.domain.resource.Resource
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class ProductUseCase (
    private val productRepository: ProductRepository
) {
    fun getProduct(barcode:String) = flow<Product> {
        productRepository.getProduct(barcode).product?.let { emit (it) }
    }

    suspend fun addProduct(product: Product) {
        if (product.productName.isBlank()) {
            throw InvalidProductException("The name of the product can't be empty.")
        }
        if (product.quantity!! < 1) {
            throw InvalidProductException("The quantity must be positive and not empty.")
        }
        productRepository.insertProduct(product)
    }
}