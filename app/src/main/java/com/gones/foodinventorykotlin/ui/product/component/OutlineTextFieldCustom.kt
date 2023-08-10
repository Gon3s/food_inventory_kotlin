package com.gones.foodinventorykotlin.ui.product.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@ExperimentalMaterial3Api
@Composable
fun OutlineTextFieldCustom(
    value: String,
    title: String,
    modifier: Modifier = Modifier,
    error: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(text = title) },
        modifier = modifier
            .padding(vertical = 8.dp)
            .fillMaxWidth(),
        placeholder = { Text(text = title) },
        isError = !error.isNullOrEmpty(),
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType)
    )

    if (!error.isNullOrEmpty()) {
        Text(
            text = error,
            color = Color.Red
        )
    }
}