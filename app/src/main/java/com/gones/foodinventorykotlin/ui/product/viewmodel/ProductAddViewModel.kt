package com.gones.foodinventorykotlin.ui.product.viewmodel;

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gones.foodinventorykotlin.domain.entity.InvalidProductException
import com.gones.foodinventorykotlin.domain.entity.Product
import com.gones.foodinventorykotlin.domain.resource.Resource
import com.gones.foodinventorykotlin.domain.usecase.ProductUseCase
import com.gones.foodinventorykotlin.ui.product.event.ProductAddEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProductAddViewModel(
    private val productUseCase: ProductUseCase,
    private val barcode: String
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _product = MutableStateFlow<Resource<Product>>(Resource.Progress())
    val product: StateFlow<Resource<Product>> = _product

    private lateinit var productToUpdate: Product

    init {
        Log.i("ProductAdd", "init")
    }

    fun getProduct() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                Log.i("ProductAdd", "launch")
                productUseCase.getProduct(barcode).catch { e ->
                    Log.e("ProductAdd", e.message.toString())
                    _product.value = (Resource.failure(e))
                }.collect { product ->
                    Log.i("ProductAdd", product.productName)
                    _product.value = (Resource.success(product))
                    productToUpdate = product
                }
            }
        }
    }

    fun onEvent(event: ProductAddEvent) {
        when (event) {
            is ProductAddEvent.EnteredName -> {
                productToUpdate.productName = event.name
            }
            is ProductAddEvent.EnteredBrands -> {
                productToUpdate.brands = event.brands
            }
            is ProductAddEvent.EnteredQuantity -> {
                productToUpdate.quantity = event.quantity
            }
            is ProductAddEvent.EnteredExpiryDate -> {
                productToUpdate.expiry_date = event.expiryDate
            }
            is ProductAddEvent.SaveProduct -> {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        productUseCase.addProduct(
                            productToUpdate
                        )
                        _eventFlow.emit(UiEvent.SaveNote)
                    } catch(e: InvalidProductException) {
                        Log.e("ProductAdd", e.message.toString())
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Couldn't save note"
                            )
                        )
                    } catch (e: Exception) {
                        Log.e("ProductAdd", e.message.toString())
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Couldn't save note"
                            )
                        )
                    }
                }
            }
        }
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String): UiEvent()
        object SaveNote: UiEvent()
    }
}
