package com.gones.foodinventorykotlin.domain.usecase

import com.gones.foodinventorykotlin.R
import com.gones.foodinventorykotlin.common.ExpirySections
import com.gones.foodinventorykotlin.common.UiText
import com.gones.foodinventorykotlin.domain.entity.Product
import com.gones.foodinventorykotlin.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ProductUseCase(
    private val productRepository: ProductRepository,
) {
    fun getProductByEanWS(barcode: String) = flow {
        productRepository.getProductByEanWS(barcode).product?.let { emit(it) }
    }

    suspend fun addProduct(product: Product, quantity: Int): SupabaseResult {
        if (product.product_name?.isBlank() == true) {
            return SupabaseResult(
                successful = false,
                errorMessage = UiText.StringResource(R.string.product_name_is_required)
            )
        }

        if (quantity < 1) {
            return SupabaseResult(
                successful = false,
                errorMessage = UiText.StringResource(R.string.quantity_must_be_positive)
            )
        }

        for (i in 1..quantity) {
            productRepository.insertProduct(product)
        }

        return SupabaseResult(successful = true)
    }

    suspend fun updateProduct(product: Product): SupabaseResult {
        if (product.product_name?.isBlank() == true) {
            return SupabaseResult(
                successful = false,
                errorMessage = UiText.StringResource(R.string.product_name_is_required)
            )
        }

        try {
            productRepository.updateProduct(product)
        } catch (e: Exception) {
            return SupabaseResult(
                successful = false,
                errorMessage = UiText.StringResource(R.string.an_error_occured)
            )
        }

        return SupabaseResult(successful = true)
    }

    fun getProducts(
        categoryId: Int? = null
    ): Flow<Map<ExpirySections, List<Product>>> =
        productRepository.getProducts()

    fun getProductByEan(barcode: String): Flow<List<Product>> =
        productRepository.getProductsByEan(barcode)

    fun getProductById(id: Int): Flow<Product> = productRepository.getProductById(id)

    suspend fun consumedProduct(product: Product) {
        productRepository.updateProduct(product)
    }
}