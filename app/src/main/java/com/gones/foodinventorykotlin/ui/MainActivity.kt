@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.gones.foodinventorykotlin.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.rememberNavController
import com.gones.foodinventorykotlin.ui.components.drawer.DrawerScreen
import com.gones.foodinventorykotlin.ui.components.scaffold.FoodInventoryScaffold
import com.gones.foodinventorykotlin.ui.theme.FoodInventoryTheme
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalMaterial3Api
class MainActivity(
) : ComponentActivity() {
    private val viewModel by viewModel<MainViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupSplashScreen()

        setContent {
            FoodInventoryTheme {
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
                            viewModel = viewModel,
                            navController = navController,
                            drawerState = drawerState,
                            snackbarHostState = snackbarHostState,
                        )
                    }
                } else {
                    FoodInventoryScaffold(
                        viewModel = viewModel,
                        navController = navController,
                        drawerState = drawerState,
                        snackbarHostState = snackbarHostState,
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
                    keepSplashScreenOn = it
                }
            }
        }

        installSplashScreen().setKeepOnScreenCondition {
            keepSplashScreenOn
        }
    }
}