package com.gones.foodinventorykotlin.ui.pages.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.DrawerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gones.foodinventorykotlin.R
import com.gones.foodinventorykotlin.common.ExpirySections
import com.gones.foodinventorykotlin.domain.entity.Category
import com.gones.foodinventorykotlin.domain.entity.Product
import com.gones.foodinventorykotlin.ui.components.ChipItem
import com.gones.foodinventorykotlin.ui.components.ProductItem
import com.gones.foodinventorykotlin.ui.components.scaffold.ScaffoldState
import com.gones.foodinventorykotlin.ui.navigation.ManageCategoriesRoute
import com.gones.foodinventorykotlin.ui.navigation.ScanRoute
import com.gones.foodinventorykotlin.ui.navigation.Screen
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.Instant
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

    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        CategoriesFilter(
            categoryId = state.categoryId,
            categories = state.categories,
            goToManageCategories = {
                navController.navigate(ManageCategoriesRoute)
            },
            onClickCategory = { categoryId ->
                viewModel.onEvent(HomeEvent.CategorySelected(categoryId))
            }
        )

        ProductsList(
            products = state.products, goToProductDetails = { productId ->
                navController.navigate("product?id=$productId")
            }
        )
    }
}

@Composable
fun CategoriesFilter(
    categoryId: Int? = null,
    categories: List<Category> = listOf(),
    goToManageCategories: () -> Unit = {},
    onClickCategory: (Int?) -> Unit = {},
) {
    LazyRow {
        item {
            ChipItem(
                label = stringResource(id = R.string.all),
                selected = categoryId == null,
                onClick = {
                    onClickCategory(null)
                }
            )
        }
        items(items = categories, key = { category -> category.id }) { category ->
            ChipItem(
                label = category.name,
                selected = categoryId == category.id,
                onClick = {
                    if (categoryId == category.id) {
                        onClickCategory(null)
                    } else {
                        onClickCategory(category.id)
                    }
                }
            )
        }
        item {
            ChipItem(label = stringResource(id = R.string.add), onClick = {
                goToManageCategories()
            })
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductsList(
    products: Map<ExpirySections, List<Product>>,
    goToProductDetails: (Int) -> Unit = {}
) {
    val context = LocalContext.current

    LazyColumn {
        products.forEach { section ->
            stickyHeader {
                Text(
                    text = section.key.title.asString(context),
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.primary)
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
            items(items = section.value, key = { product -> product.id }) { product ->
                ProductItem(
                    product = product,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            goToProductDetails(product.id)
                        }
                )
                HorizontalDivider()
            }
        }
    }
}

@Preview
@Composable
fun CategoriesFilterPreview() {
    CategoriesFilter(
        categoryId = 2,
        categories = listOf(
            Category(id = 1, name = "Fruits"),
            Category(id = 2, name = "Vegetables"),
            Category(id = 3, name = "Meat"),
            Category(id = 4, name = "Fish"),
            Category(id = 5, name = "Dairy"),
            Category(id = 6, name = "Bakery"),
            Category(id = 7, name = "Drinks"),
            Category(id = 8, name = "Frozen"),
            Category(id = 9, name = "Other"),
        )
    )
}

@Preview
@Composable
fun ProductsListPreview() {
    ProductsList(
        products = mapOf(
            ExpirySections.Expired to listOf(
                Product(
                    id = 1,
                    barcode = "123456789",
                    image_url = "https://fastly.picsum.photos/id/447/200/300.jpg?hmac=WubV-ZWbMgXijt9RLYedmkiaSer2IFiVD7xek928gC8",
                    product_name = "Banana",
                    expiry_date = Instant.parse("2021-09-01T00:00:00Z").toEpochMilliseconds(),
                ),
            )
        )
    )
}
