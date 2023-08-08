package com.gones.foodinventorykotlin.ui.product

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gones.foodinventorykotlin.domain.entity.InvalidProductException
import com.gones.foodinventorykotlin.domain.entity.Product
import com.gones.foodinventorykotlin.domain.resource.Resource
import com.gones.foodinventorykotlin.domain.usecase.ProductUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

class ProductViewModel(
    private val productUseCase: ProductUseCase,
    private val barcode: String? = null,
    private val id: String? = null,
) : ViewModel() {
    enum class TYPES {
        CREATE, UPDATE
    }

    private lateinit var productToUpdate: Product

    private val _state = mutableStateOf(ProductState())
    val state: State<ProductState> = _state

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        getProduct()
    }

    private fun getProduct() {
        barcode?.let {
            _state.value = _state.value.copy(type = TYPES.CREATE)

            productUseCase.getProductByEanWS(barcode).catch { e ->
                _state.value = _state.value.copy(product = Resource.failure(e))
            }.onEach { product ->
                _state.value = _state.value.copy(product = Resource.success(product))
            }.launchIn(viewModelScope)

            productUseCase.getProductByEan(barcode).catch { e ->
                _state.value = _state.value.copy(products = Resource.failure(e))
            }.onEach { products ->
                _state.value = _state.value.copy(products = Resource.success(products))
            }.launchIn(viewModelScope)
        }

        id?.let {
            _state.value = _state.value.copy(type = TYPES.UPDATE)

            productUseCase.getProductById(id.toInt()).catch { e ->
                _state.value = _state.value.copy(product = Resource.failure(e))
            }.onEach { product ->
                _state.value = _state.value.copy(product = Resource.success(product))
            }.launchIn(viewModelScope)
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
                _state.value = _state.value.copy(quantity = event.quantity)
            }

            is ProductAddEvent.DecreaseQuantity -> {
                if (state.value.quantity < 1) return
                _state.value = state.value.copy(quantity = state.value.quantity - 1)
            }

            is ProductAddEvent.IncreaseQuantity -> {
                _state.value = state.value.copy(quantity = state.value.quantity + 1)
            }

            is ProductAddEvent.EnteredExpiryDate -> {
                productToUpdate.expiry_date = event.expiryDate
            }

            is ProductAddEvent.Consume -> {
                CoroutineScope(Dispatchers.IO).launch {
                    productUseCase.consumedProduct(productToUpdate)

                    _eventFlow.emit(UiEvent.ProductUpdated)
                }
            }

            is ProductAddEvent.SaveProduct -> {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        when (_state.value.type) {
                            TYPES.CREATE -> {
                                Timber.d("DLOG: addProduct - productToUpdate: $productToUpdate")
                                productUseCase.addProduct(
                                    productToUpdate,
                                    _state.value.quantity
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
