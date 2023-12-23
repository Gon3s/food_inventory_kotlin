package com.gones.foodinventorykotlin.ui._common.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.ui.graphics.vector.ImageVector
import com.gones.foodinventorykotlin.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

const val SplashRoute = "splash"
const val HomeRoute = "home"
const val ScanRoute = "scan"
const val ProductRoute = "product?barcode={barcode}&id={id}"
const val LoginRoute = "login"
const val RegisterRoute = "register"
const val ManageCategriesRoute = "manage_categories"

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

    class Splash : Screen {
        override val route: String = SplashRoute
        override val isAppBarVisible: Boolean = false
    }

    class Home : Screen {
        override val route: String = HomeRoute
        override val isAppBarVisible: Boolean = true
        override val navigationIcon: ImageVector = Icons.Default.Menu
        override val onNavigationIconClick: (() -> Unit) = {
            _actions.tryEmit(Actions.ToggleDrawer)
        }
        override val title: Int = R.string.app_name
        override val floatingActionIcon: ImageVector = Icons.Filled.Add
        override val floatingActionIconClick: (() -> Unit) = {
            _actions.tryEmit(Actions.ScanIcon)
        }

        enum class Actions {
            ScanIcon,
            ToggleDrawer
        }

        private val _actions = MutableSharedFlow<Actions>(extraBufferCapacity = 1)
        val actions: Flow<Actions> = _actions.asSharedFlow()
    }

    class Scan : Screen {
        override val route: String = ScanRoute
        override val isAppBarVisible: Boolean = true
        override val navigationIcon: ImageVector = Icons.AutoMirrored.Filled.ArrowBack
        override val onNavigationIconClick: (() -> Unit) = {
            _actions.tryEmit(Actions.NavigationIcon)
        }
        override val title: Int = R.string.scan_product

        enum class Actions {
            NavigationIcon
        }

        private val _actions = MutableSharedFlow<Actions>(extraBufferCapacity = 1)
        val actions: Flow<Actions> = _actions.asSharedFlow()
    }

    class Product : Screen {
        override val route: String = ProductRoute
        override val isAppBarVisible: Boolean = true
        override val navigationIcon: ImageVector = Icons.AutoMirrored.Filled.ArrowBack
        override val onNavigationIconClick: (() -> Unit) = {
            _actions.tryEmit(Actions.NavigationIcon)
        }
        override val title: Int? = null
        override val actionsMenu: List<MenuItem> = listOf(
            MenuItem(
                onClick = {
                    _actions.tryEmit(Actions.Save)
                },
                icon = R.drawable.ic_done
            )
        )

        enum class Actions {
            NavigationIcon,
            Save
        }

        private val _actions = MutableSharedFlow<Actions>(extraBufferCapacity = 1)
        val actions: Flow<Actions> = _actions.asSharedFlow()
    }

    class SignUp : Screen {
        override val route: String = RegisterRoute
        override val isAppBarVisible: Boolean = true
        override val title: Int = R.string.sign_up
        override val navigationIcon: ImageVector = Icons.AutoMirrored.Filled.ArrowBack
        override val onNavigationIconClick: (() -> Unit) = {
            _actions.tryEmit(Actions.NavigationIcon)
        }

        enum class Actions {
            NavigationIcon,
        }

        private val _actions = MutableSharedFlow<Actions>(extraBufferCapacity = 1)
        val actions: Flow<Actions> = _actions.asSharedFlow()
    }

    class Login : Screen {
        override val route: String = LoginRoute
        override val isAppBarVisible: Boolean = false
    }

    class ManageCategories : Screen {
        override val route: String = ManageCategriesRoute
        override val isAppBarVisible: Boolean = true
        override val title: Int = R.string.manage_categories
        override val navigationIcon: ImageVector = Icons.Default.Menu
        override val onNavigationIconClick: (() -> Unit) = {
            _actions.tryEmit(Actions.NavigationIcon)
        }
        override val actionsMenu: List<MenuItem> = listOf(
            MenuItem(
                onClick = {
                    _actions.tryEmit(Actions.Add)
                },
                icon = R.drawable.ic_add
            )
        )

        enum class Actions {
            NavigationIcon,
            Add
        }

        private val _actions = MutableSharedFlow<Actions>(extraBufferCapacity = 1)
        val actions: Flow<Actions> = _actions.asSharedFlow()
    }
}

fun getScreen(route: String?): Screen? {
    return when (route) {
        SplashRoute -> Screen.Splash()
        HomeRoute -> Screen.Home()
        ScanRoute -> Screen.Scan()
        ProductRoute -> Screen.Product()
        LoginRoute -> Screen.Login()
        RegisterRoute -> Screen.SignUp()
        ManageCategriesRoute -> Screen.ManageCategories()
        else -> null
    }
}