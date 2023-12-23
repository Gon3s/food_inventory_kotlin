package com.gones.foodinventorykotlin.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.gones.foodinventorykotlin.ui._common.ScaffoldState
import com.gones.foodinventorykotlin.ui._common.component.ProductItem
import com.gones.foodinventorykotlin.ui._common.navigation.ScanRoute
import com.gones.foodinventorykotlin.ui._common.navigation.Screen
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@Composable
fun HomeScreen(
    scaffoldState: ScaffoldState,
    snackbarHostState: SnackbarHostState,
    drawerState: DrawerState,
    navController: NavController,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val state = viewModel.state.value
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        viewModel.getProducts()
    }

    val screen = scaffoldState.currentScreen as? Screen.Home
    LaunchedEffect(key1 = screen) {
        screen?.actions?.onEach { action ->
            Timber.d("DLOG: HomeScreen : ScanIcon : action : $action")
            when (action) {
                Screen.Home.Actions.ScanIcon -> {
                    navController.navigate(ScanRoute)
                }

                Screen.Home.Actions.ToggleDrawer -> {
                    if (drawerState.isClosed) {
                        drawerState.open()
                    } else {
                        drawerState.close()
                    }
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
