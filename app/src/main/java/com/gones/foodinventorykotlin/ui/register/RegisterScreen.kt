package com.gones.foodinventorykotlin.ui.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.gones.foodinventorykotlin.R
import com.gones.foodinventorykotlin.common.UiText
import com.gones.foodinventorykotlin.ui.common.AppBarState
import com.gones.foodinventorykotlin.ui.common.component.OutlinePasswordTextFieldCustom
import com.gones.foodinventorykotlin.ui.common.component.OutlineTextFieldCustom
import com.gones.foodinventorykotlin.ui.navigation.LoginRoute
import com.gones.foodinventorykotlin.ui.navigation.RegisterRoute
import com.gones.foodinventorykotlin.ui.navigation.Screen
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    appBarState: AppBarState,
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    viewModel: RegisterViewModel = koinViewModel(),
) {
    val state = viewModel.state.value
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is RegisterViewModel.UiEvent.ShowSnackbar -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            event.message.asString(context)
                        )
                    }
                }

                is RegisterViewModel.UiEvent.RegisterOk -> {
                    snackbarHostState.showSnackbar(
                        UiText.StringResource(R.string.register_ok).asString(context)
                    )
                    navController.navigate(LoginRoute) {
                        popUpTo(RegisterRoute) {
                            inclusive = true
                        }
                    }
                }
            }
        }
    }

    val screen = appBarState.currentScreen as? Screen.SignUp
    LaunchedEffect(key1 = screen) {
        screen?.actions?.onEach { action ->
            when (action) {
                Screen.SignUp.AppBarIcons.NavigationIcon -> {
                    navController.popBackStack()
                }
            }
        }?.launchIn(this)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 64.dp)
        ) {
            Text(
                text = stringResource(id = R.string.register_title),
                fontSize = 42.sp,
                fontWeight = FontWeight.Black,
            )
        }

        Column {
            OutlineTextFieldCustom(
                value = state.email,
                title = stringResource(id = R.string.email),
                onValueChange = {
                    viewModel.onEvent(RegisterEvent.EnteredEmail(it))
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Email
            )
            OutlinePasswordTextFieldCustom(
                value = state.password,
                title = stringResource(id = R.string.password),
                onValueChange = {
                    viewModel.onEvent(RegisterEvent.EnteredPassword(it))
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Button(
            onClick = {
                viewModel.onEvent(RegisterEvent.Register)
            },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.register))
        }
    }
}