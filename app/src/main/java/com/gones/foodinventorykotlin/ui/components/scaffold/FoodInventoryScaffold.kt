package com.gones.foodinventorykotlin.ui.components.scaffold

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DrawerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.gones.foodinventorykotlin.R
import com.gones.foodinventorykotlin.ui.MainViewModel
import com.gones.foodinventorykotlin.ui.components.FoodInventoryFloatingButton
import com.gones.foodinventorykotlin.ui.components.FoodInventoryTopAppBar
import com.gones.foodinventorykotlin.ui.navigation.HomeRoute
import com.gones.foodinventorykotlin.ui.navigation.LoginRoute
import com.gones.foodinventorykotlin.ui.navigation.ManageCategoriesRoute
import com.gones.foodinventorykotlin.ui.navigation.ProductRoute
import com.gones.foodinventorykotlin.ui.navigation.RegisterRoute
import com.gones.foodinventorykotlin.ui.navigation.ScanRoute
import com.gones.foodinventorykotlin.ui.navigation.SplashRoute
import com.gones.foodinventorykotlin.ui.pages.auth.login.LoginScreen
import com.gones.foodinventorykotlin.ui.pages.auth.register.RegisterScreen
import com.gones.foodinventorykotlin.ui.pages.home.HomeScreen
import com.gones.foodinventorykotlin.ui.pages.manageCategories.ManageCategoriesScreen
import com.gones.foodinventorykotlin.ui.pages.product.ProductScreen
import com.gones.foodinventorykotlin.ui.pages.scan.ScanScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodInventoryScaffold(
    viewModel: MainViewModel,
    navController: NavHostController,
    drawerState: DrawerState,
    snackbarHostState: SnackbarHostState,
) {
    val appBarState = rememberAppBarState(navController = navController)
    val startDestination = viewModel.defaultRoute.collectAsState().value

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            if (appBarState.isAppBarVisible) {
                FoodInventoryTopAppBar(
                    scaffoldState = appBarState,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        floatingActionButton = {
            FoodInventoryFloatingButton(scaffoldState = appBarState)
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = startDestination,
            Modifier.padding(paddingValues),
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                    animationSpec = tween(700)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                    animationSpec = tween(700)
                )
            },
            popEnterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                    animationSpec = tween(700)
                )
            },
            popExitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                    animationSpec = tween(700)
                )
            }
        ) {
            composable(SplashRoute,
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                        animationSpec = tween(700)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
                        animationSpec = tween(700)
                    )
                },
                popEnterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                        animationSpec = tween(700)
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
                        animationSpec = tween(700)
                    )
                }) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.splash),
                        contentDescription = "",
                        modifier = Modifier.size(200.dp)
                    )
                }
            }
            composable(HomeRoute) {
                HomeScreen(
                    scaffoldState = appBarState,
                    drawerState = drawerState,
                    navController = navController
                )
            }
            composable(LoginRoute) {
                LoginScreen(
                    snackbarHostState = snackbarHostState,
                    navController = navController
                )
            }
            composable(RegisterRoute) {
                RegisterScreen(
                    scaffoldState = appBarState,
                    snackbarHostState = snackbarHostState,
                    navController = navController
                )
            }
            composable(ScanRoute) {
                ScanScreen(
                    scaffoldState = appBarState,
                    navController = navController
                )
            }
            composable(
                ProductRoute, arguments = listOf(
                    navArgument("barcode") {
                        type = NavType.StringType
                        defaultValue = null
                        nullable = true
                    },
                    navArgument("id") {
                        type = NavType.StringType
                        defaultValue = null
                        nullable = true
                    },
                )
            ) {
                ProductScreen(
                    scaffoldState = appBarState,
                    snackbarHostState = snackbarHostState,
                    navController = navController,
                    barcode = it.arguments?.getString("barcode"),
                    id = it.arguments?.getString("id")
                )
            }
            composable(ManageCategoriesRoute) {
                ManageCategoriesScreen(
                    scaffoldState = appBarState,
                    snackbarHostState = snackbarHostState,
                    navController = navController
                )
            }
        }
    }
}