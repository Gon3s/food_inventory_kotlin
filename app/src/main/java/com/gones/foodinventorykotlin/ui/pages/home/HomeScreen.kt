package com.gones.foodinventorykotlin.ui.pages.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.DrawerState
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gones.foodinventorykotlin.ui.components.ProductItem
import com.gones.foodinventorykotlin.ui.components.scaffold.ScaffoldState
import com.gones.foodinventorykotlin.ui.navigation.ScanRoute
import com.gones.foodinventorykotlin.ui.navigation.Screen
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    scaffoldState: ScaffoldState,
    drawerState: DrawerState,
    navController: NavController,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val state = viewModel.state.value

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

    Column {
        LazyRow(
            modifier = Modifier
                .padding(start = 8.dp)
                .fillMaxWidth()
        ) {
            items(items = state.categories, key = { category -> category.id }) { category ->
                Box(
                    modifier = Modifier.padding(
                        start = 4.dp,
                        end = 4.dp,
                    )
                ) {
                    FilterChip(
                        onClick = {
                            if (state.categoryId == category.id) {
                                viewModel.onEvent(HomeEvent.CategorySelected(null))
                            } else {
                                viewModel.onEvent(HomeEvent.CategorySelected(category.id))
                            }
                        },
                        label = {
                            Text(text = category.name)
                        },
                        selected = state.categoryId == category.id,
                        leadingIcon = if (state.categoryId == category.id) {
                            {
                                Icon(
                                    imageVector = Icons.Filled.Done,
                                    contentDescription = "Done icon",
                                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                                )
                            }
                        } else {
                            null
                        },
                    )
                }
            }
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
                            navController.navigate("product?id=${product.id}")
                        }
                )
                HorizontalDivider()
            }
        }
    }
}
