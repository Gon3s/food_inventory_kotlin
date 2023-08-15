@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.gones.foodinventorykotlin.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
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
import com.gones.foodinventorykotlin.ui.home.HomeScreen
import com.gones.foodinventorykotlin.ui.manageCategories.ManageCategoriesScreen
import com.gones.foodinventorykotlin.ui.product.ProductScreen
import com.gones.foodinventorykotlin.ui.scan.ScanScreen
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalMaterial3Api
class MainActivity(
) : ComponentActivity() {
    private val viewModel by viewModel<MainViewModel>()

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
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val startDestination = viewModel.defaultRoute.collectAsState().value
                val scope = rememberCoroutineScope()

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = stringResource(
                                        id = R.string.hello_user,
                                        "USER NAME"
                                    ), // TODO: Replace with user name
                                )
                            }
                            // TODO: Add Categories list
                            Spacer(modifier = Modifier.fillMaxWidth())
                            NavigationDrawerItem(
                                icon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_category),
                                        contentDescription = null
                                    )
                                },
                                label = {
                                    Text(text = stringResource(id = R.string.manage_categories))
                                },
                                selected = false,
                                onClick = {
                                    navController.navigate(ManageCategriesRoute)
                                    scope.launch {
                                        drawerState.close()
                                    }
                                },
                                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                            )
                            NavigationDrawerItem(
                                icon = {
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_settings),
                                        contentDescription = null
                                    )
                                },
                                label = {
                                    Text(text = stringResource(id = R.string.settings))
                                },
                                selected = false,
                                onClick = { /*TODO*/ },
                                modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                            )
                        }
                    },
                    gesturesEnabled = true,
                ) {
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
            }
        }
    }
}