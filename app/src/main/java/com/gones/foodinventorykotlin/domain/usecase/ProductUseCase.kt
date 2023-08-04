package com.gones.foodinventorykotlin.domain.usecase

import com.gones.foodinventorykotlin.domain.entity.InvalidProductException
import com.gones.foodinventorykotlin.domain.entity.Product
import com.gones.foodinventorykotlin.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.datetime.Clock

class ProductUseCase(
    private val productRepository: ProductRepository,
) {
    fun getProductByEanWS(barcode: String) = flow {
        productRepository.getProductByEanWS(barcode).product?.let { emit(it) }
    }

    suspend fun addProduct(product: Product, quantity: Int) {
        if (product.productName?.isBlank() == true) {
            throw InvalidProductException("The name of the product can't be empty.")
        }
        if (quantity < 1) {
            throw InvalidProductException("The quantity must be positive and not empty.")
        }
        for (i in 1..quantity) {
            productRepository.insertProduct(product)
        }
    }

    suspend fun updateProduct(product: Product) {
        if (product.productName?.isBlank() == true) {
            throw InvalidProductException("The name of the product can't be empty.")
        }
        productRepository.updateProduct(product)
    }

    fun getProducts(): Flow<List<Product>> = productRepository.getProducts()
    fun getProductByEan(barcode: String): Flow<List<Product>> =
        productRepository.getProductsByEan(barcode)

    fun getProductById(id: Int): Flow<Product> = productRepository.getProductById(id)

    suspend fun consumedProduct(product: Product) {
        product.consumed = true
        product.consumed_at = Clock.System.now()
        productRepository.updateProduct(product)
    }
}