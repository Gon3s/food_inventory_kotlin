package com.gones.foodinventorykotlin.presentation.components.drawer

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gones.foodinventorykotlin.R
import com.gones.foodinventorykotlin.presentation.navigation.HomeRoute
import com.gones.foodinventorykotlin.presentation.navigation.LoginRoute
import com.gones.foodinventorykotlin.presentation.navigation.ManageCategoriesRoute
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun DrawerScreen(
    navController: NavController,
    drawerState: DrawerState,
    snackbarHostState: SnackbarHostState,
    viewModel: DrawerViewModel = koinViewModel(),
    content: @Composable () -> Unit,
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is DrawerViewModel.UiEvent.ShowSnackbar -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            event.message.asString(context)
                        )
                    }
                }

                is DrawerViewModel.UiEvent.LogoutOk -> {
                    navController.navigate(LoginRoute) {
                        popUpTo(HomeRoute) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }

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
                            viewModel.getCurrentUser()?.pseudo ?: ""
                        ),
                    )
                }
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
                        navController.navigate(ManageCategoriesRoute) {
                            popUpTo(ManageCategoriesRoute)
                        }
                        scope.launch {
                            drawerState.close()
                        }
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )

                Spacer(modifier = Modifier.fillMaxWidth())
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
                NavigationDrawerItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_logout),
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(text = stringResource(id = R.string.logout))
                    },
                    selected = false,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                        }
                        viewModel.logout()
                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        },
        gesturesEnabled = true,
        content = content
    )
}