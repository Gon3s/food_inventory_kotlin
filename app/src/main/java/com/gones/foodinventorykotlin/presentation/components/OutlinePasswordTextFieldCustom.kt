package com.gones.foodinventorykotlin.presentation.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.gones.foodinventorykotlin.R

@ExperimentalMaterial3Api
@Composable
fun OutlinePasswordTextFieldCustom(
    value: String,
    title: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    error: String? = null,
    onValueChange: (String) -> Unit,
    keyboardActions: KeyboardActions = KeyboardActions(),
    imeAction: ImeAction = ImeAction.Default,
) {
    var passwordVisibility: Boolean by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = title) },
        singleLine = singleLine,
        modifier = modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        placeholder = { Text(text = title) },
        isError = !error.isNullOrEmpty(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        ),
        keyboardActions = keyboardActions,
        trailingIcon = {
            IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                Icon(
                    painterResource(id = if (passwordVisibility) R.drawable.ic_visibility else R.drawable.ic_visibility_off),
                    contentDescription = null,
                )
            }
        },
        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
    )

    if (!error.isNullOrEmpty()) {
        ErrorTextField(
            error = error,
        )
    }
}