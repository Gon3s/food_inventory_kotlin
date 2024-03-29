package com.gones.foodinventorykotlin.presentation.components.scaffold

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import com.gones.foodinventorykotlin.presentation.navigation.MenuItem
import com.gones.foodinventorykotlin.presentation.navigation.Screen
import com.gones.foodinventorykotlin.presentation.navigation.getScreen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Stable
class ScaffoldState(
    navController: NavController,
    scope: CoroutineScope,
) {
    init {
        navController.currentBackStackEntryFlow
            .distinctUntilChanged()
            .onEach { backStackEntry ->
                val route = backStackEntry.destination.route
                currentScreen = getScreen(route)
            }
            .launchIn(scope)
    }

    var currentScreen by mutableStateOf<Screen?>(null)
        private set

    val isAppBarVisible: Boolean
        @Composable get() = currentScreen?.isAppBarVisible == true

    val navigationIcon: ImageVector?
        @Composable get() = currentScreen?.navigationIcon

    val navigationIconContentDescription: String?
        @Composable get() = currentScreen?.navigationIconContentDescription

    val onNavigationIconClick: (() -> Unit)?
        @Composable get() = currentScreen?.onNavigationIconClick

    val title: Int?
        @Composable get() = currentScreen?.title

    val actions: List<MenuItem>
        @Composable get() = currentScreen?.actionsMenu.orEmpty()

    val floatingActionIcon: ImageVector?
        @Composable get() = currentScreen?.floatingActionIcon

    val floatingActionContentDescription: String?
        @Composable get() = currentScreen?.floatingActionContentDescription

    val floatingActionIconClick: (() -> Unit)?
        @Composable get() = currentScreen?.floatingActionIconClick
}

@Composable
fun rememberAppBarState(
    navController: NavController,
    scope: CoroutineScope = rememberCoroutineScope(),
) = remember { ScaffoldState(navController, scope) }