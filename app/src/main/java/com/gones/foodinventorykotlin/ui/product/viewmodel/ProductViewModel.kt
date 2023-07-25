package com.gones.foodinventorykotlin.ui.product.viewmodel;

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
import timber.log.Timber

class ProductViewModel(
    private val productUseCase: ProductUseCase,
    private val barcode: String? = null,
    private val id: String? = null,
) : ViewModel() {

    enum class TYPES {
        CREATE, UPDATE
    }

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    private val _product = MutableStateFlow<Resource<Product>>(Resource.Progress())
    val product: StateFlow<Resource<Product>> = _product

    private val _products = MutableStateFlow<Resource<List<Product>>>(Resource.Progress())
    val products: StateFlow<Resource<List<Product>>> = _products

    private lateinit var productToUpdate: Product
    private var quantity: Int = 1
    var type: TYPES = (id?.let { TYPES.UPDATE } ?: TYPES.CREATE)

    fun getProduct() {
        Timber.d("DLOG: getProduct - barcode: $barcode, id: $id")
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                barcode?.let {
                    Timber.d("DLOG: getProductByEanWS: $barcode")
                    productUseCase.getProductByEanWS(barcode).catch { e ->
                        _product.value = (Resource.failure(e))
                    }.collect { product ->
                        _product.value = (Resource.success(product))
                        productToUpdate = product
                    }

                    productUseCase.getProductByEan(barcode).catch { e ->
                        _product.value = (Resource.failure(e))
                    }.collect { products ->
                        _products.value = (Resource.success(products))
                    }
                }

                id?.let {
                    Timber.d("DLOG: getProductById: $id")
                    productUseCase.getProductById(id.toInt()).catch { e ->
                        _product.value = (Resource.failure(e))
                    }.collect { product ->
                        _product.value = (Resource.success(product))
                        productToUpdate = product
                    }
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
                quantity = event.quantity
            }

            is ProductAddEvent.EnteredExpiryDate -> {
                productToUpdate.expiry_date = event.expiryDate
            }

            is ProductAddEvent.SaveProduct -> {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        when (type) {
                            TYPES.CREATE -> {
                                Timber.d("DLOG: addProduct - productToUpdate: $productToUpdate")
                                productUseCase.addProduct(
                                    productToUpdate,
                                    quantity
                                )

                                _eventFlow.emit(UiEvent.ProductCreated)
                            }

                            TYPES.UPDATE -> {
                                Timber.d("DLOG: updateProduct - productToUpdate: $productToUpdate")

                                productUseCase.updateProduct(
                                    productToUpdate
                                )

                                _eventFlow.emit(UiEvent.ProductUpdated)
                            }
                        }
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
    }

    sealed class UiEvent {
        data class ShowSnackbar(val message: String) : UiEvent()
        object ProductCreated : UiEvent()
        object ProductUpdated : UiEvent()
    }
}
