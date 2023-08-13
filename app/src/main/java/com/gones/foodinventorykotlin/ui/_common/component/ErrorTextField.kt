package com.gones.foodinventorykotlin.ui._common.component

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp

@Composable
fun ErrorTextField(
    error: String,
) {
    Text(
        text = error,
        color = MaterialTheme.colorScheme.error,
        fontSize = 12.sp,
    )
}