package com.gones.foodinventorykotlin.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Stable
class AppBarState(
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

    val isVisible: Boolean
        @Composable get() = currentScreen?.isAppBarVisible == true

    val navigationIcon: ImageVector?
        @Composable get() = currentScreen?.navigationIcon

    val navigationIconContentDescription: String?
        @Composable get() = currentScreen?.navigationIconContentDescription

    val onNavigationIconClick: (() -> Unit)?
        @Composable get() = currentScreen?.onNavigationIconClick

    val title: String
        @Composable get() = currentScreen?.title.orEmpty()

    val actions: List<MenuItem>
        @Composable get() = currentScreen?.actionsMenu.orEmpty()

    val isFloationButtonVisible: Boolean
        @Composable get() = currentScreen?.floatingActionIcon != null

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
) = remember { AppBarState(navController, scope) }