package com.gones.foodinventorykotlin.ui._common.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.graphics.vector.ImageVector
import com.gones.foodinventorykotlin.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

const val HomeRoute = "home"
const val ScanRoute = "scan"
const val ProductRoute = "product?barcode={barcode}&id={id}"
const val LoginRoute = "login"
const val RegisterRoute = "register"

class MenuItem(
    val onClick: (() -> Unit),
    val icon: Int,
    val contentDescription: String? = null,
)

sealed interface Screen {
    val route: String
    val isAppBarVisible: Boolean
    val navigationIcon: ImageVector?
        get() = null
    val navigationIconContentDescription: String?
        get() = null
    val onNavigationIconClick: (() -> Unit)?
        get() = null
    val title: Int?
        get() = null
    val actionsMenu: List<MenuItem>
        get() = emptyList()
    val floatingActionIcon: ImageVector?
        get() = null
    val floatingActionContentDescription: String?
        get() = null
    val floatingActionIconClick: (() -> Unit)?
        get() = null

    class Home : Screen {
        override val route: String = HomeRoute
        override val isAppBarVisible: Boolean = true
        override val title: Int = R.string.app_name
        override val floatingActionIcon: ImageVector = Icons.Filled.Add
        override val floatingActionIconClick: (() -> Unit) = {
            _actions.tryEmit(AppBarIcons.ScanIcon)
        }
        override val actionsMenu: List<MenuItem> = listOf(
            MenuItem(
                onClick = {
                    _actions.tryEmit(AppBarIcons.Logout)
                },
                icon = R.drawable.ic_logout
            )
        )

        enum class AppBarIcons {
            ScanIcon,
            Logout
        }

        private val _actions = MutableSharedFlow<AppBarIcons>(extraBufferCapacity = 1)
        val actions: Flow<AppBarIcons> = _actions.asSharedFlow()
    }

    class Scan : Screen {
        override val route: String = ScanRoute
        override val isAppBarVisible: Boolean = true
        override val navigationIcon: ImageVector = Icons.Default.ArrowBack
        override val onNavigationIconClick: (() -> Unit) = {
            _actions.tryEmit(AppBarIcons.NavigationIcon)
        }
        override val title: Int = R.string.scan_product

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
        override val onNavigationIconClick: (() -> Unit) = {
            _actions.tryEmit(AppBarIcons.NavigationIcon)
        }
        override val title: Int? = null
        override val actionsMenu: List<MenuItem> = listOf(
            MenuItem(
                onClick = {
                    _actions.tryEmit(AppBarIcons.Save)
                },
                icon = R.drawable.ic_done
            )
        )

        enum class AppBarIcons {
            NavigationIcon,
            Save
        }

        private val _actions = MutableSharedFlow<AppBarIcons>(extraBufferCapacity = 1)
        val actions: Flow<AppBarIcons> = _actions.asSharedFlow()
    }

    class SignUp : Screen {
        override val route: String = RegisterRoute
        override val isAppBarVisible: Boolean = true
        override val title: Int = R.string.sign_up
        override val navigationIcon: ImageVector = Icons.Default.ArrowBack
        override val onNavigationIconClick: (() -> Unit) = {
            _actions.tryEmit(AppBarIcons.NavigationIcon)
        }

        enum class AppBarIcons {
            NavigationIcon,
        }

        private val _actions = MutableSharedFlow<AppBarIcons>(extraBufferCapacity = 1)
        val actions: Flow<AppBarIcons> = _actions.asSharedFlow()
    }

    class Login : Screen {
        override val route: String = LoginRoute
        override val isAppBarVisible: Boolean = false
    }
}

fun getScreen(route: String?): Screen? {
    return when (route) {
        HomeRoute -> Screen.Home()
        ScanRoute -> Screen.Scan()
        ProductRoute -> Screen.Product()
        LoginRoute -> Screen.Login()
        RegisterRoute -> Screen.SignUp()
        else -> null
    }
}