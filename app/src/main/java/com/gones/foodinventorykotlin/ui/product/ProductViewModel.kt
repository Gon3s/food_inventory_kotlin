package com.gones.foodinventorykotlin.ui.product

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gones.foodinventorykotlin.domain.entity.InvalidProductException
import com.gones.foodinventorykotlin.domain.usecase.ProductUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import timber.log.Timber

class ProductViewModel(
    private val productUseCase: ProductUseCase,
    private val barcode: String? = null,
    private val id: String? = null,
) : ViewModel() {
    enum class TYPES {
        CREATE, UPDATE
    }

    private val _state = mutableStateOf(ProductState())
    val state: State<ProductState> = _state

    private val _otherProductsState = mutableStateOf(OtherProductState())
    val otherProductsState: State<OtherProductState> = _otherProductsState

    private val _eventFlow = MutableSharedFlow<UiEvent>()
    val eventFlow = _eventFlow.asSharedFlow()

    init {
        getProduct()
    }

    private fun getProduct() {
        _state.value = _state.value.copy(isLoading = true)

        barcode?.let {
            _state.value = _state.value.copy(type = TYPES.CREATE)
            _otherProductsState.value = _otherProductsState.value.copy(isLoading = true)

            productUseCase.getProductByEanWS(barcode).catch { e ->
                _state.value = _state.value.copy(
                    hasError = true,
                    errorMessage = e.message ?: "An error occurred",
                    isLoading = false,
                )
            }.onEach { product ->
                _state.value =
                    _state.value.copy(
                        product = product, hasError = false,
                        errorMessage = "", isLoading = false
                    )
            }.launchIn(viewModelScope)

            productUseCase.getProductByEan(barcode).catch { e ->
                _otherProductsState.value = _otherProductsState.value.copy(
                    hasError = true,
                    errorMessage = e.message ?: "An error occurred",
                    isLoading = false,
                )
            }.onEach { products ->
                _otherProductsState.value = _otherProductsState.value.copy(
                    products = products,
                    hasError = false,
                    errorMessage = "",
                    isLoading = false
                )
            }.launchIn(viewModelScope)
        }

        id?.let {
            _state.value = _state.value.copy(type = TYPES.UPDATE)

            productUseCase.getProductById(id.toInt()).catch { e ->
                _state.value = _state.value.copy(
                    hasError = true,
                    errorMessage = e.message ?: "An error occurred",
                    isLoading = false,
                )
            }.onEach { product ->
                _state.value =
                    _state.value.copy(
                        product = product, hasError = false,
                        errorMessage = "", isLoading = false
                    )
            }.launchIn(viewModelScope)
        }
    }

    fun onEvent(event: ProductAddEvent) {
        when (event) {
            is ProductAddEvent.EnteredName -> {
                _state.value.product?.let {
                    _state.value = _state.value.copy(product = it.copy(product_name = event.name))
                }
            }

            is ProductAddEvent.EnteredBrands -> {
                _state.value.product?.let {
                    _state.value = _state.value.copy(product = it.copy(brands = event.brands))
                }
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
                _state.value.product?.let {
                    _state.value =
                        _state.value.copy(product = it.copy(expiry_date = event.expiryDate))
                }
            }

            is ProductAddEvent.Consume -> {
                CoroutineScope(Dispatchers.IO).launch {
                    _state.value.product?.let { product ->
                        _state.value = _state.value.copy(isLoading = true)
                        _state.value = _state.value.copy(
                            product = product.copy(
                                consumed = true,
                                consumed_at = Clock.System.now()
                            )
                        )
                        productUseCase.consumedProduct(product)
                        _state.value = _state.value.copy(isLoading = false)
                    }
                }
            }

            is ProductAddEvent.SaveProduct -> {
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        _state.value.product?.let { product ->
                            when (_state.value.type) {
                                TYPES.CREATE -> {
                                    productUseCase.addProduct(
                                        product,
                                        _state.value.quantity
                                    )

                                    _eventFlow.emit(UiEvent.ProductCreated)
                                }

                                TYPES.UPDATE -> {
                                    productUseCase.updateProduct(
                                        product
                                    )

                                    _eventFlow.emit(UiEvent.ProductUpdated)
                                }
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
