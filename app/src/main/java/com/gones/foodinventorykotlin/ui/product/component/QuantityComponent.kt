package com.gones.foodinventorykotlin.ui.product.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.gones.foodinventorykotlin.R


@ExperimentalMaterial3Api
@Composable
fun QuantityComponent(
    value: Int,
    title: String,
    modifier: Modifier = Modifier,
    onValueChange: (Int) -> Unit,
    onDecreaseQuantity: () -> Unit,
    onIncreaseQuantity: () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FilledIconButton(
            onClick = onDecreaseQuantity,
            modifier = Modifier.size(36.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_minus),
                contentDescription = null
            )
        }
        OutlinedTextField(
            value = value.toString(),
            onValueChange = { onValueChange(it.toInt()) },
            label = { Text(text = title) },
            modifier = modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
            placeholder = { Text(text = title) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        FilledIconButton(
            onClick = onIncreaseQuantity,
            modifier = Modifier.size(36.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_add),
                contentDescription = null
            )
        }
    }
}