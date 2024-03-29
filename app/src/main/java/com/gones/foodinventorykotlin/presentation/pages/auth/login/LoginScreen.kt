package com.gones.foodinventorykotlin.presentation.pages.auth.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.gones.foodinventorykotlin.R
import com.gones.foodinventorykotlin.common.UiText
import com.gones.foodinventorykotlin.presentation.components.OutlinePasswordTextFieldCustom
import com.gones.foodinventorykotlin.presentation.components.OutlineTextFieldCustom
import com.gones.foodinventorykotlin.presentation.navigation.HomeRoute
import com.gones.foodinventorykotlin.presentation.navigation.LoginRoute
import com.gones.foodinventorykotlin.presentation.navigation.RegisterRoute
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@ExperimentalMaterial3Api
@Composable
fun LoginScreen(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    viewModel: LoginViewModel = koinViewModel(),
) {
    val state = viewModel.state.value
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(key1 = true) {
        viewModel.eventFlow.collectLatest { event ->
            when (event) {
                is LoginViewModel.UiEvent.ShowSnackbar -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            event.message.asString(context)
                        )
                    }
                }

                is LoginViewModel.UiEvent.LoginOk -> {
                    snackbarHostState.showSnackbar(
                        UiText.StringResource(R.string.login_ok).asString(context)
                    )
                    navController.navigate(HomeRoute) {
                        popUpTo(LoginRoute) {
                            inclusive = true
                        }
                    }
                }
            }
        }
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
                text = stringResource(id = R.string.login_title),
                fontSize = 42.sp,
                fontWeight = FontWeight.Black
            )
            Text(
                text = stringResource(id = R.string.login_subtitle),
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Column {
            OutlineTextFieldCustom(
                value = state.email,
                title = stringResource(id = R.string.email),
                error = state.emailError?.asString(context),
                onValueChange = {
                    viewModel.onEvent(LoginEvent.EmailChanged(it))
                },
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
            OutlinePasswordTextFieldCustom(
                value = state.password,
                title = stringResource(id = R.string.password),
                error = state.passwordError?.asString(context),
                onValueChange = {
                    viewModel.onEvent(LoginEvent.PasswordChanged(it))
                },
                modifier = Modifier.fillMaxWidth(),
                imeAction = ImeAction.Send,
                keyboardActions = KeyboardActions(
                    onSend = {
                        viewModel.onEvent(LoginEvent.Login)
                    }
                )
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(id = R.string.register),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    modifier = Modifier.clickable {
                        navController.navigate(RegisterRoute)
                    }
                )
                Text(
                    text = stringResource(id = R.string.forgot_password),
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
            }
        }

        Button(
            onClick = {
                viewModel.onEvent(LoginEvent.Login)
            },
            enabled = !state.isLoading,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            if (state.isLoading)
                Text(text = stringResource(id = R.string.loading))
            else
                Text(text = stringResource(id = R.string.login))
        }
    }
}
