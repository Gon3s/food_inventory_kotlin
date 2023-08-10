package com.gones.foodinventorykotlin.ui.common

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import timber.log.Timber

const val HomeRoute = "home"
const val ScanRoute = "scan"
const val ProductRoute = "product?barcode={barcode}&id={id}"

class MenuItem(
    val title: String,
    val onClick: (() -> Unit),
    val icon: ImageVector,
    val contentDescription: String? = null,
)

sealed interface Screen {
    val route: String
    val isAppBarVisible: Boolean
    val navigationIcon: ImageVector?
    val navigationIconContentDescription: String?
    val onNavigationIconClick: (() -> Unit)?
    val title: String
    val actionsMenu: List<MenuItem>
    val floatingActionIcon: ImageVector?
        get() = null
    val floatingActionContentDescription: String?
        get() = null
    val floatingActionIconClick: (() -> Unit)?
        get() = null

    class Home : Screen {
        override val route: String = HomeRoute
        override val isAppBarVisible: Boolean = true
        override val navigationIcon: ImageVector? = null
        override val navigationIconContentDescription: String? = null
        override val onNavigationIconClick: (() -> Unit)? = null
        override val title: String = "Home" // TODO: string resource
        override val actionsMenu: List<MenuItem> = emptyList()
        override val floatingActionIcon: ImageVector = Icons.Filled.Add
        override val floatingActionIconClick: (() -> Unit) = {
            Timber.d("DLOG: floatingActionIconClick tryEmit")
            _actions.tryEmit(FloationActionIcons.ScanIcon)
        }

        enum class FloationActionIcons {
            ScanIcon
        }

        private val _actions = MutableSharedFlow<FloationActionIcons>(extraBufferCapacity = 1)
        val actions: Flow<FloationActionIcons> = _actions.asSharedFlow()
    }

    class Scan : Screen {
        override val route: String = ScanRoute
        override val isAppBarVisible: Boolean = true
        override val navigationIcon: ImageVector = Icons.Default.ArrowBack
        override val navigationIconContentDescription: String? = null
        override val onNavigationIconClick: (() -> Unit) = {
            Timber.d("DLOG: onNavigationIconClick tryEmit")
            _actions.tryEmit(AppBarIcons.NavigationIcon)
        }
        override val title: String = "Scan" // TODO: string resource
        override val actionsMenu: List<MenuItem> = emptyList()

        enum class AppBarIcons {
            NavigationIcon
        }

        private val _actions = MutableSharedFlow<AppBarIcons>(extraBufferCapacity = 1)
        val actions: Flow<AppBarIcons> = _actions.asSharedFlow()
    }

    class Product : Screen {
        override val route: String = ProductRoute
        override val isAppBarVisible: Boolean = true
        override val navigationIcon: ImageVector = Icons.Default.ArrowBack
        override val navigationIconContentDescription: String? = null
        override val onNavigationIconClick: (() -> Unit) = {
            _actions.tryEmit(AppBarIcons.NavigationIcon)
        }
        override val title: String = "Scan" // TODO: string resource
        override val actionsMenu: List<MenuItem> = listOf(
            MenuItem(
                title = "Save",
                onClick = {
                    _actions.tryEmit(AppBarIcons.Save)
                },
                icon = Icons.Default.Done
            )
        )

        enum class AppBarIcons {
            NavigationIcon,
            Save
        }

        private val _actions = MutableSharedFlow<AppBarIcons>(extraBufferCapacity = 1)
        val actions: Flow<AppBarIcons> = _actions.asSharedFlow()
    }
}


fun getScreen(route: String?): Screen? {
    Timber.d("DLOG: getScreen route: $route")
    return when (route) {
        HomeRoute -> Screen.Home()
        ScanRoute -> Screen.Scan()
        ProductRoute -> Screen.Product()
        else -> null
    }
}