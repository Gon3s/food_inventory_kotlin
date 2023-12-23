@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.gones.foodinventorykotlin.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gones.foodinventorykotlin.R
import com.gones.foodinventorykotlin.ui._common.component.FoodInventoryFloatingButton
import com.gones.foodinventorykotlin.ui._common.component.FoodInventoryTopAppBar
import com.gones.foodinventorykotlin.ui._common.navigation.HomeRoute
import com.gones.foodinventorykotlin.ui._common.navigation.LoginRoute
import com.gones.foodinventorykotlin.ui._common.navigation.ManageCategriesRoute
import com.gones.foodinventorykotlin.ui._common.navigation.ProductRoute
import com.gones.foodinventorykotlin.ui._common.navigation.RegisterRoute
import com.gones.foodinventorykotlin.ui._common.navigation.ScanRoute
import com.gones.foodinventorykotlin.ui._common.navigation.SplashRoute
import com.gones.foodinventorykotlin.ui._common.rememberAppBarState
import com.gones.foodinventorykotlin.ui._common.theme.FoodInventoryTheme
import com.gones.foodinventorykotlin.ui.auth.login.LoginScreen
import com.gones.foodinventorykotlin.ui.auth.register.RegisterScreen
import com.gones.foodinventorykotlin.ui.drawer.DrawerScreen
import com.gones.foodinventorykotlin.ui.home.HomeScreen
import com.gones.foodinventorykotlin.ui.manageCategories.ManageCategoriesScreen
import com.gones.foodinventorykotlin.ui.product.ProductScreen
import com.gones.foodinventorykotlin.ui.scan.ScanScreen
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

@ExperimentalMaterial3Api
class MainActivity(
) : ComponentActivity() {
    private val viewModel by viewModel<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupSplashScreen()

        setContent {
            Timber.d("DLOG: setContent")
            FoodInventoryTheme(
                darkTheme = false,
                dynamicColor = false
            ) {
                val navController = rememberNavController()
                val snackbarHostState = remember { SnackbarHostState() }
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

                val isConnected = viewModel.isConnected.collectAsState().value

                if (isConnected) {
                    DrawerScreen(
                        navController = navController,
                        drawerState = drawerState,
                        snackbarHostState = snackbarHostState,
                    ) {
                        FoodInventoryScaffold(
                            navController = navController,
                            drawerState = drawerState,
                            snackbarHostState = snackbarHostState,
                        )
                    }
                } else {
                    FoodInventoryScaffold(
                        navController = navController,
                        drawerState = drawerState,
                        snackbarHostState = snackbarHostState,
                    )
                }
            }
        }
    }

    @Composable
    fun FoodInventoryScaffold(
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
                Modifier.padding(paddingValues)
            ) {
                composable(SplashRoute) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center,
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_splash),
                            contentDescription = "",
                            modifier = Modifier.size(200.dp),
                        )
                    }
                }
                composable(HomeRoute) {
                    HomeScreen(
                        scaffoldState = appBarState,
                        snackbarHostState = snackbarHostState,
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
                composable(ManageCategriesRoute) {
                    ManageCategoriesScreen(
                        scaffoldState = appBarState,
                        snackbarHostState = snackbarHostState,
                        navController = navController
                    )
                }
            }
        }
    }

    private fun setupSplashScreen() {
        var keepSplashScreenOn = true
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoading.collect {
                    Timber.d("DLOG: isLoading: $it")
                    keepSplashScreenOn = it
                }
            }
        }

        installSplashScreen().setKeepOnScreenCondition {
            keepSplashScreenOn
        }
    }
}