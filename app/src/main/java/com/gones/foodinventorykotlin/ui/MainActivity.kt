@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.gones.foodinventorykotlin.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gones.foodinventorykotlin.ui.common.FoodInventoryFloatingButton
import com.gones.foodinventorykotlin.ui.common.FoodInventoryTopAppBar
import com.gones.foodinventorykotlin.ui.common.HomeRoute
import com.gones.foodinventorykotlin.ui.common.ProductRoute
import com.gones.foodinventorykotlin.ui.common.ScanRoute
import com.gones.foodinventorykotlin.ui.common.rememberAppBarState
import com.gones.foodinventorykotlin.ui.home.HomeScreen
import com.gones.foodinventorykotlin.ui.product.ProductScreen
import com.gones.foodinventorykotlin.ui.scan.ScanScreen
import com.gones.foodinventorykotlin.ui.theme.FoodInventoryTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            FoodInventoryTheme {
                val navController = rememberNavController()
                val appBarState = rememberAppBarState(navController = navController)
                val snackbarHostState = remember { SnackbarHostState() }

                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    topBar = {
                        if (appBarState.isVisible) {
                            FoodInventoryTopAppBar(
                                appBarState = appBarState,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }
                    },
                    floatingActionButton = {
                        FoodInventoryFloatingButton(appBarState = appBarState)
                    },
                ) { paddingValues ->
                    NavHost(
                        navController = navController,
                        startDestination = HomeRoute,
                        Modifier.padding(paddingValues)
                    ) {
                        composable(HomeRoute) {
                            HomeScreen(appBarState = appBarState, navController = navController)
                        }
                        composable(ScanRoute) {
                            ScanScreen(appBarState = appBarState, navController = navController)
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
                                appBarState = appBarState,
                                navController = navController,
                                barcode = it.arguments?.getString("barcode"),
                                id = it.arguments?.getString("id")
                            )
                        }
                    }
                }
            }
        }
    }
}