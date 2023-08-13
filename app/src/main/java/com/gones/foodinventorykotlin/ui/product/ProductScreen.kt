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
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.gones.foodinventorykotlin.R
import com.gones.foodinventorykotlin.ui._common.AppBarState
import com.gones.foodinventorykotlin.ui._common.component.OutlineTextFieldCustom
import com.gones.foodinventorykotlin.ui._common.component.ProductItem
import com.gones.foodinventorykotlin.ui._common.navigation.Screen
import com.gones.foodinventorykotlin.ui.product.component.DatePickerCustom
import com.gones.foodinventorykotlin.ui.product.component.QuantityComponent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.compose.getViewModel
import org.koin.core.parameter.parametersOf
import java.text.SimpleDateFormat
import java.util.Locale


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@ExperimentalMaterial3Api
@Composable
fun ProductScreen(
    appBarState: AppBarState,
    barcode: String? = null,
    id: String? = null,
    snackbarHostState: SnackbarHostState,
    navController: NavController,
) {
    val viewModel = getViewModel<ProductViewModel>(
        parameters = {
            parametersOf(barcode, id)
        }
    )
    val state = viewModel.state.value
    val otherProductsState = viewModel.otherProductsState.value
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is ProductViewModel.UiEvent.ShowSnackbar -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            event.message.asString(context)
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

    val screen = appBarState.currentScreen as? Screen.Product
    LaunchedEffect(key1 = screen) {
        screen?.actions?.onEach { action ->
            when (action) {
                Screen.Product.AppBarIcons.NavigationIcon -> {
                    navController.popBackStack()
                }

                Screen.Product.AppBarIcons.Save -> {
                    viewModel.onEvent(ProductAddEvent.SaveProduct)
                }
            }
        }?.launchIn(this)
    }

    if (state.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    if (state.hasError) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = state.errorMessage.asString(context))
        }
        return
    }

    val product = state.product

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        item {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(
                        LocalContext.current
                    ).data(product.image_url)
                        .crossfade(true).build(),
                    contentDescription = product.product_name,
                    modifier = Modifier
                        .padding(8.dp)
                        .size(160.dp),
                )
                product.barcode?.let {
                    Text(
                        it,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }

            OutlineTextFieldCustom(
                value = product.product_name ?: "",
                title = stringResource(id = R.string.productName),
                onValueChange = {
                    viewModel.onEvent(ProductAddEvent.EnteredName(it))
                },
                error = state.nameError.asString(context),
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

            OutlineTextFieldCustom(
                value = product.note ?: "",
                title = stringResource(id = R.string.addProductNote),
                singleLine = false,
                onValueChange = {
                    viewModel.onEvent(ProductAddEvent.EnteredNote(it))
                },
                error = state.nameError.asString(context),
            )

            if (state.type == ProductViewModel.TYPES.CREATE) {
                if (otherProductsState.isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(192.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }

                    return@item
                }

                if (otherProductsState.hasError) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(192.dp),
                        contentAlignment = Alignment.Center
                    ) { Text(text = otherProductsState.errorMessage.asString(context)) }

                }
                val products = otherProductsState.products
                if (products.isNotEmpty()) {
                    Text(
                        text = stringResource(id = R.string.productsAlreadySaved),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    LazyColumn {
                        items(products.size) { index ->
                            ProductItem(
                                product = products[index],
                                size = 60.dp
                            )
                        }
                    }
                }
            }

            if (state.type == ProductViewModel.TYPES.UPDATE) {
                Box(
                    modifier = Modifier.padding(vertical = 16.dp),
                ) {
                    if (product.consumed && product.consumed_at != null) {
                        Text(
                            text = stringResource(
                                id = R.string.consumedAt,
                                SimpleDateFormat(
                                    "dd/MM/yyyy",
                                    Locale.getDefault()
                                ).format(product.consumed_at?.epochSeconds),
                            ),
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    } else {
                        Button(
                            onClick = { viewModel.onEvent(ProductAddEvent.Consume) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(text = stringResource(id = R.string.retire))
                        }
                    }
                }
            }
        }
    }
}


