package com.gones.foodinventorykotlin.ui.home

import android.text.format.DateFormat
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.gones.foodinventorykotlin.R
import com.gones.foodinventorykotlin.domain.entity.Product
import com.gones.foodinventorykotlin.ui.common.AppBarState
import com.gones.foodinventorykotlin.ui.common.ScanRoute
import com.gones.foodinventorykotlin.ui.common.Screen
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber
import java.util.Date

@Composable
fun HomeScreen(
    appBarState: AppBarState,
    navController: NavController,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val state = viewModel.state.value

    val screen = appBarState.currentScreen as? Screen.Home
    LaunchedEffect(key1 = screen) {
        screen?.actions?.onEach { action ->
            Timber.d("DLOG: HomeScreen : ScanIcon : action : $action")
            when (action) {
                Screen.Home.FloationActionIcons.ScanIcon -> {
                    navController.navigate(ScanRoute)
                }
            }
        }?.launchIn(this)
    }

    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(state.products.size) { index ->
            val product = state.products[index]
            ProductItem(
                product = product,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        Timber.d("DLOG : ProductScreen : ProductItem : product : $product")
                        navController.navigate("product?id=${product.id}")
                    }
            )
        }
    }
}

@Composable
fun ProductItem(
    product: Product,
    modifier: Modifier = Modifier,
) {
    val dateFormat = DateFormat.getDateFormat(LocalContext.current)
    val expiryDate = product.expiry_date?.let {
        dateFormat.format(Date(it))
    } ?: run {
        stringResource(R.string.no_expiry_date)
    }

    Box(modifier = modifier) {
        Row {
            AsyncImage(
                model = ImageRequest.Builder(
                    LocalContext.current
                ).data(product.imageUrl)
                    .crossfade(true).build(),
                contentDescription = product.productName,
                modifier = Modifier
                    .padding(8.dp)
                    .size(90.dp)
            )
            Column {
                product.brands?.let { Text(text = it, fontSize = 16.sp) }
                Text(text = product.productName, fontSize = 18.sp)
                Text(text = expiryDate, fontSize = 16.sp)
            }
        }
    }
}