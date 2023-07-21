package com.gones.foodinventorykotlin.domain.usecase

import androidx.lifecycle.LiveData
import com.gones.foodinventorykotlin.domain.entity.InvalidProductException
import com.gones.foodinventorykotlin.domain.entity.Product
import com.gones.foodinventorykotlin.domain.repository.ProductRepository
import kotlinx.coroutines.flow.flow

class ProductUseCase(
    private val productRepository: ProductRepository,
) {
    fun getProduct(barcode: String) = flow<Product> {
        productRepository.getProduct(barcode).product?.let { emit(it) }
    }

    suspend fun addProduct(product: Product, quantity: Int) {
        if (product.productName.isBlank()) {
            throw InvalidProductException("The name of the product can't be empty.")
        }
        if (quantity < 1) {
            throw InvalidProductException("The quantity must be positive and not empty.")
        }
        for (i in 1..quantity) {
            productRepository.insertProduct(product)
        }
    }

    fun getProducts(): LiveData<List<Product>> {
        return productRepository.getProduct()
    }
}