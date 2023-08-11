@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.gones.foodinventorykotlin.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gones.foodinventorykotlin.ui.common.component.FoodInventoryFloatingButton
import com.gones.foodinventorykotlin.ui.common.component.FoodInventoryTopAppBar
import com.gones.foodinventorykotlin.ui.common.rememberAppBarState
import com.gones.foodinventorykotlin.ui.home.HomeScreen
import com.gones.foodinventorykotlin.ui.login.LoginScreen
import com.gones.foodinventorykotlin.ui.navigation.HomeRoute
import com.gones.foodinventorykotlin.ui.navigation.LoginRoute
import com.gones.foodinventorykotlin.ui.navigation.ProductRoute
import com.gones.foodinventorykotlin.ui.navigation.ScanRoute
import com.gones.foodinventorykotlin.ui.navigation.SignupRoute
import com.gones.foodinventorykotlin.ui.product.ProductScreen
import com.gones.foodinventorykotlin.ui.register.RegisterScreen
import com.gones.foodinventorykotlin.ui.scan.ScanScreen
import com.gones.foodinventorykotlin.ui.theme.FoodInventoryTheme

@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.isLoading.value
            }
        }

        setContent {
            FoodInventoryTheme(
                darkTheme = false,
                dynamicColor = false
            ) {
                val navController = rememberNavController()
                val appBarState = rememberAppBarState(navController = navController)
                val snackbarHostState = remember { SnackbarHostState() }

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    contentColor = Color.White,
                ) {
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
                            startDestination = LoginRoute,
                            Modifier.padding(paddingValues)
                        ) {
                            composable(HomeRoute) {
                                HomeScreen(
                                    appBarState = appBarState,
                                    navController = navController
                                )
                            }
                            composable(LoginRoute) {
                                LoginScreen(
                                    appBarState = appBarState,
                                    navController = navController
                                )
                            }
                            composable(SignupRoute) {
                                RegisterScreen(
                                    appBarState = appBarState,
                                    navController = navController
                                )
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
                                    snackbarHostState = snackbarHostState,
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
}