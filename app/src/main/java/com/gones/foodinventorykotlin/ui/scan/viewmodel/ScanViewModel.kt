package com.gones.foodinventorykotlin.ui.scan.viewmodel

import androidx.lifecycle.*
import com.gones.foodinventorykotlin.domain.entity.Product
import com.gones.foodinventorykotlin.domain.resource.Resource
import com.gones.foodinventorykotlin.domain.usecase.ProductUseCase
import com.gones.foodinventorykotlin.ui.common.extension.contextIO

class ScanViewModel(
    private val productUseCase: ProductUseCase
) : ViewModel() {

    private val getProductParams = MutableLiveData<GetProductParams>();

    /*val getProductResult: LiveData<Resource<Product>> = getProductParams.switchMap {
            params -> productUseCase.getProduct(params.barcode).asLiveData(contextIO())
    }
*/
    fun getProduct(barcode: String) {
        getProductParams.value = GetProductParams(barcode)
    }

    private class GetProductParams(val barcode: String)
}