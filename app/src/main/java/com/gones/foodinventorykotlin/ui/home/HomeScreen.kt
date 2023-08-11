package com.gones.foodinventorykotlin.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.gones.foodinventorykotlin.ui.common.AppBarState
import com.gones.foodinventorykotlin.ui.common.ScanRoute
import com.gones.foodinventorykotlin.ui.common.Screen
import com.gones.foodinventorykotlin.ui.common.component.ProductItem
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@Composable
fun HomeScreen(
    appBarState: AppBarState,
    navController: NavController,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val state = viewModel.state.value

    LaunchedEffect(key1 = Unit) {
        viewModel.getProducts()
    }

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
        modifier = Modifier
            .fillMaxSize()
    ) {
        items(items = state.products, key = { product -> product.id }) { product ->
            ProductItem(
                product = product,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        Timber.d("DLOG : ProductScreen : ProductItem : product : $product")
                        navController.navigate("product?id=${product.id}")
                    }
            )
            HorizontalDivider()
        }
    }
}
