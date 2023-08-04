package com.gones.foodinventorykotlin.ui.product

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.gones.foodinventorykotlin.R
import com.gones.foodinventorykotlin.domain.resource.Resource
import com.gones.foodinventorykotlin.ui.home.ProductItem
import com.gones.foodinventorykotlin.ui.product.component.DatePickerCustom
import com.gones.foodinventorykotlin.ui.product.component.OutlineTextFieldCustom
import com.gones.foodinventorykotlin.ui.product.component.QuantityComponent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@Composable
fun ProductScreen(
    barcode: String? = null,
    id: String? = null,
    navController: NavController,
) {
    val viewModel = getViewModel<ProductViewModel>(
        parameters = {
            parametersOf(barcode, id)
        }
    )
    val state = viewModel.state.value
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is ProductViewModel.UiEvent.ShowSnackbar -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            event.message
                        )
                    }
                }

                is ProductViewModel.UiEvent.ProductCreated -> {
                    navController.navigateUp()
                }

                is ProductViewModel.UiEvent.ProductUpdated -> {
                    navController.navigateUp()
                }
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { _ ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            when (val productState = state.product) {
                is Resource.Success -> {
                    val product = productState.data
                    AsyncImage(
                        model = ImageRequest.Builder(
                            LocalContext.current
                        ).data(product.imageUrl)
                            .crossfade(true).build(),
                        contentDescription = product.productName,
                        modifier = Modifier
                            .padding(8.dp)
                            .size(160.dp),
                        alignment = Alignment.Center,
                    )
                    OutlineTextFieldCustom(
                        value = product.productName,
                        title = stringResource(id = R.string.productName),
                        onValueChange = {
                            viewModel.onEvent(ProductAddEvent.EnteredName(it))
                        },
                    )
                    OutlineTextFieldCustom(
                        value = product.brands ?: "",
                        title = stringResource(id = R.string.brands),
                        onValueChange = {
                            viewModel.onEvent(ProductAddEvent.EnteredBrands(it))
                        },
                    )
                    if (state.type == ProductViewModel.TYPES.CREATE) {
                        QuantityComponent(
                            value = viewModel.state.value.quantity,
                            title = stringResource(id = R.string.productQuantity),
                            onValueChange = {
                                viewModel.onEvent(ProductAddEvent.EnteredQuantity(it))
                            },
                            onDecreaseQuantity = {
                                viewModel.onEvent(ProductAddEvent.DecreaseQuantity)
                            },
                            onIncreaseQuantity = {
                                viewModel.onEvent(ProductAddEvent.IncreaseQuantity)
                            },
                        )
                    }
                    DatePickerCustom(
                        title = stringResource(id = R.string.expirationDate),
                        date = product.expiry_date,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                    if (state.type == ProductViewModel.TYPES.CREATE) {
                        when (val productsState = state.products) {
                            is Resource.Success -> {
                                val products = productsState.data
                                LazyColumn {
                                    items(products.size) { index ->
                                        ProductItem(
                                            product = products[index]
                                        )
                                    }
                                }
                            }

                            is Resource.Failure -> {
                                Text(text = productsState.throwable.localizedMessage ?: "Error")
                            }

                            is Resource.Progress -> {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }
                    }

                    if (state.type == ProductViewModel.TYPES.UPDATE) {
                        if (product.consumed) {
                            Text(
                                text = stringResource(
                                    id = R.string.consumedAt,
                                    SimpleDateFormat(
                                        "dd/MM/yyyy",
                                        Locale.getDefault()
                                    ).format(product.expiry_date),
                                ),
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        } else {
                            Button(onClick = { viewModel.onEvent(ProductAddEvent.Consume) }) {
                                Text(text = stringResource(id = R.string.retire))
                            }
                        }
                    }
                }

                is Resource.Failure -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = productState.throwable.localizedMessage ?: "Error")
                    }
                }

                is Resource.Progress -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}


