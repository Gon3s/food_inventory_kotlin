package com.gones.foodinventorykotlin.ui.updateProduct.viewmodel;

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gones.foodinventorykotlin.domain.entity.Product
import com.gones.foodinventorykotlin.domain.resource.Resource
import com.gones.foodinventorykotlin.domain.usecase.ProductUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class UpdateProductViewModel(
    private val productUseCase: ProductUseCase,
    private val id: Int,
) : ViewModel() {

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _product = MutableStateFlow<Resource<Product>>(Resource.Progress())
    val product: StateFlow<Resource<Product>> = _product

    fun getProductById() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                productUseCase.getProductById(id).catch { e ->
                    _product.value = (Resource.failure(e))
                }.collect { product ->
                    _product.value = (Resource.success(product))
                }
            }
        }
    }

    /*fun onEvent(event: ProductAddEvent) {
        when (event) {
            is ProductAddEvent.EnteredName -> {
                productToUpdate.productName = event.name
            }

            is ProductAddEvent.EnteredBrands -> {
                productToUpdate.brands = event.brands
            }

            is ProductAddEvent.EnteredQuantity -> {
                quantity = event.quantity
            }

            is ProductAddEvent.EnteredExpiryDate -> {
                Timber.d("DLOG: EnteredExpiryDate: ${event.expiryDate}")
                productToUpdate.expiry_date = event.expiryDate
            }

            is ProductAddEvent.SaveProduct -> {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        productUseCase.addProduct(
                            productToUpdate,
                            quantity
                        )
                        _eventFlow.emit(UiEvent.SaveNote)
                    } catch (e: InvalidProductException) {
                        Timber.e("DLOG: SaveProduct - InvalidProductException: ${e.message}")
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Couldn't save note"
                            )
                        )
                    } catch (e: Exception) {
                        Timber.e("DLOG: SaveProduct - Exception: ${e.message}")
                        _eventFlow.emit(
                            UiEvent.ShowSnackbar(
                                message = e.message ?: "Couldn't save note"
                            )
                        )
                    }
                }
            }
        }
    }*/

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object SaveNote : UiEvent()
    }
}
