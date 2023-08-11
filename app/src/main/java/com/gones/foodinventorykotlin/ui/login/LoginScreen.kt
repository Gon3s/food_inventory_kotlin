package com.gones.foodinventorykotlin.ui.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.gones.foodinventorykotlin.R
import com.gones.foodinventorykotlin.ui.common.AppBarState
import com.gones.foodinventorykotlin.ui.common.component.OutlineTextFieldCustom
import com.gones.foodinventorykotlin.ui.navigation.SignupRoute
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    appBarState: AppBarState,
    navController: NavHostController,
    viewModel: LoginViewModel = koinViewModel(),
) {
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
                color = MaterialTheme.colorScheme.secondary
            )
        }

        Column {
            OutlineTextFieldCustom(
                value = "",
                title = stringResource(id = R.string.email),
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Email
            )
            OutlineTextFieldCustom(
                value = "",
                title = stringResource(id = R.string.password),
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Password
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
                        navController.navigate(SignupRoute)
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
            onClick = { /*TODO*/ },
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.login))
        }
    }
}
