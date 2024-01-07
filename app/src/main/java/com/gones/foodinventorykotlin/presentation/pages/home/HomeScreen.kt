package com.gones.foodinventorykotlin.presentation.pages.home

import android.content.res.Configuration
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gones.foodinventorykotlin.R
import com.gones.foodinventorykotlin.common.ExpirySections
import com.gones.foodinventorykotlin.domain.entity.Category
import com.gones.foodinventorykotlin.domain.entity.Product
import com.gones.foodinventorykotlin.presentation.components.ChipItem
import com.gones.foodinventorykotlin.presentation.components.ProductItem
import com.gones.foodinventorykotlin.presentation.components.scaffold.ScaffoldState
import com.gones.foodinventorykotlin.presentation.navigation.ManageCategoriesRoute
import com.gones.foodinventorykotlin.presentation.navigation.ScanRoute
import com.gones.foodinventorykotlin.presentation.navigation.Screen
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
            products = state.products,
            goToProductDetails = { productId ->
                navController.navigate("product?id=$productId")
            },
            onSwipe = { product ->
                viewModel.onEvent(HomeEvent.ConsumeProduct(product))
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

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ProductsList(
    products: Map<ExpirySections, List<Product>>,
    modifier: Modifier = Modifier,
    goToProductDetails: (Int) -> Unit = {},
    onSwipe: (Product) -> Unit = {},
) {
    val context = LocalContext.current
    val lazyState = rememberLazyListState()

    LazyColumn(
        state = lazyState
    ) {
        products.forEach { section ->
            stickyHeader {
                Text(
                    text = section.key.title.asString(context),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
            items(items = section.value, key = { product -> product.id }) { product ->
                val dismissState = rememberSwipeToDismissState(
                    confirmValueChange = {
                        if (it == SwipeToDismissValue.EndToStart) {
                            onSwipe(product)
                        }
                        false
                    },
                    positionalThreshold = { totalDistance ->
                        totalDistance * 0.5f
                    }
                )

                val animatedProgress = remember { Animatable(initialValue = 0.5f) }
                LaunchedEffect(Unit) {
                    animatedProgress.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(300, easing = LinearEasing)
                    )
                }

                SwipeToDismissBox(
                    state = dismissState,
                    backgroundContent = {
                        Box(
                            modifier = modifier
                                .fillMaxSize()
                                .background(color = MaterialTheme.colorScheme.error)
                                .padding(16.dp),
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onError,
                                modifier = modifier.align(Alignment.CenterEnd)
                            )
                        }
                    },
                    enableDismissFromStartToEnd = false,
                    modifier = modifier
                        .graphicsLayer(
                            scaleY = animatedProgress.value,
                            scaleX = animatedProgress.value
                        )
                        .fillMaxWidth()
                ) {
                    ProductItem(
                        product = product,
                        modifier = modifier
                            .fillParentMaxWidth()
                            .clickable {
                                goToProductDetails(product.id)
                            }
                    )
                }
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

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL)
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
