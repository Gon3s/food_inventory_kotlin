package com.gones.foodinventorykotlin.ui.pages.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gones.foodinventorykotlin.R
import com.gones.foodinventorykotlin.ui.components.ChipItem
import com.gones.foodinventorykotlin.ui.components.ProductItem
import com.gones.foodinventorykotlin.ui.components.scaffold.ScaffoldState
import com.gones.foodinventorykotlin.ui.navigation.ManageCategoriesRoute
import com.gones.foodinventorykotlin.ui.navigation.ScanRoute
import com.gones.foodinventorykotlin.ui.navigation.Screen
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    scaffoldState: ScaffoldState,
    drawerState: DrawerState,
    navController: NavController,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val state = viewModel.state.value
    val context = LocalContext.current

    LaunchedEffect(key1 = Unit) {
        viewModel.getProducts()
        viewModel.getCategories()
    }

    val screen = scaffoldState.currentScreen as? Screen.Home
    LaunchedEffect(key1 = screen) {
        screen?.actions?.onEach { action ->
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

    val lazyRowListState = rememberLazyListState()
    val lazyColumnListState = rememberLazyListState()

    Column {
        LazyRow(
            state = lazyRowListState,
            modifier = Modifier
                .padding(start = 8.dp)
                .fillMaxWidth()
        ) {
            item {
                ChipItem(
                    label = stringResource(id = R.string.all),
                    selected = state.categoryId == null,
                    onClick = {
                        viewModel.onEvent(HomeEvent.CategorySelected(null))
                    }
                )
            }
            items(items = state.categories, key = { category -> category.id }) { category ->
                ChipItem(
                    label = category.name,
                    selected = state.categoryId == category.id,
                    onClick = {
                        if (state.categoryId == category.id) {
                            viewModel.onEvent(HomeEvent.CategorySelected(null))
                        } else {
                            viewModel.onEvent(HomeEvent.CategorySelected(category.id))
                        }
                    }
                )
            }
            item {
                ChipItem(label = stringResource(id = R.string.add), onClick = {
                    navController.navigate(ManageCategoriesRoute)
                })
            }
        }

        LazyColumn(
            state = lazyColumnListState,
            modifier = Modifier
                .fillMaxSize()
        ) {
            state.products.forEach { section ->
                stickyHeader {
                    Text(
                        text = section.key,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.secondary)
                            .padding(horizontal = 16.dp, vertical = 4.dp)
                    )
                }
                items(items = section.value, key = { product -> product.id }) { product ->
                    ProductItem(
                        product = product,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                navController.navigate("product?id=${product.id}")
                            }
                    )
                    HorizontalDivider()
                }
            }


        }
    }
}
