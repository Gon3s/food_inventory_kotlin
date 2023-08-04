package com.gones.foodinventorykotlin.ui.product

import com.gones.foodinventorykotlin.domain.entity.Product
import com.gones.foodinventorykotlin.domain.resource.Resource

data class ProductState(
    val product: Resource<Product> = Resource.loading(),
    val products: Resource<List<Product>> = Resource.loading(),
    val quantity: Int = 1,
    val type: ProductViewModel.TYPES = ProductViewModel.TYPES.CREATE,
)
