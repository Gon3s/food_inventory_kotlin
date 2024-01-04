package com.gones.foodinventorykotlin.presentation.pages.manageCategories

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.gones.foodinventorykotlin.R
import com.gones.foodinventorykotlin.presentation.components.OutlineTextFieldCustom
import com.gones.foodinventorykotlin.presentation.components.scaffold.ScaffoldState
import com.gones.foodinventorykotlin.presentation.navigation.Screen
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageCategoriesScreen(
    scaffoldState: ScaffoldState,
    snackbarHostState: SnackbarHostState,
    navController: NavController,
    viewModel: ManageCategoriesViewModel = koinViewModel(),
) {
    val context = LocalContext.current
    val state = viewModel.state.value

    LaunchedEffect(key1 = Unit) {
        viewModel.getCategories()
    }

    LaunchedEffect(key1 = viewModel.eventFlow) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is ManageCategoriesViewModel.UiEvent.CategoryCreated -> {
                    viewModel.getCategories()
                    viewModel.onEvent(ManageCategoriesEvent.CloseDialog)
                }

                is ManageCategoriesViewModel.UiEvent.ShowSnackbar -> {
                    snackbarHostState.showSnackbar(event.message.asString(context))
                }
            }
        }
    }

    val screen = scaffoldState.currentScreen as? Screen.ManageCategories
    LaunchedEffect(key1 = screen) {
        screen?.actions?.onEach { action ->
            when (action) {
                Screen.ManageCategories.Actions.NavigationIcon -> {
                    navController.popBackStack()
                }

                Screen.ManageCategories.Actions.Add -> {
                    viewModel.onEvent(ManageCategoriesEvent.OpenDialog)
                }
            }
        }?.launchIn(this)
    }

    if (state.isLoading) {
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = stringResource(id = R.string.loading),
                modifier = Modifier.align(Alignment.Center)
            )
        }
    } else {
        if (state.categories.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize()) {
                Text(
                    text = stringResource(id = R.string.no_categories),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(
                    items = state.categories, key = { category ->
                        category.id
                    }
                ) {
                    ListItem(headlineContent = { Text(text = it.name) }, trailingContent = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_close),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable {
                                    viewModel.onEvent(ManageCategoriesEvent.DeleteCategory(it))
                                },
                        )
                    })
                }
            }
        }
    }


    if (state.openDialog) {
        AlertDialog(onDismissRequest = { viewModel.onEvent(ManageCategoriesEvent.CloseDialog) }) {
            Surface(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight(),
                shape = MaterialTheme.shapes.large,
                tonalElevation = AlertDialogDefaults.TonalElevation
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    OutlineTextFieldCustom(
                        value = state.category.name,
                        title = stringResource(id = R.string.category_name),
                        onValueChange = {
                            viewModel.onEvent(ManageCategoriesEvent.EnteredName(it))
                        },
                        error = state.nameError?.asString(context) ?: "",
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    TextButton(
                        onClick = {
                            viewModel.onEvent(ManageCategoriesEvent.SaveCategory)
                        },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text(stringResource(id = R.string.add))
                    }
                }
            }
        }
    }
}