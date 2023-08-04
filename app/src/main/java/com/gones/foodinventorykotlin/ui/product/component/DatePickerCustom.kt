package com.gones.foodinventorykotlin.ui.product.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import com.gones.foodinventorykotlin.ui.product.ProductAddEvent
import com.gones.foodinventorykotlin.ui.product.ProductViewModel
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Locale

@ExperimentalMaterial3Api
@Composable
fun DatePickerCustom(
    title: String,
    modifier: Modifier = Modifier,
    date: Long? = null,
    viewModel: ProductViewModel = koinViewModel(),
) {
    Timber.d("DLOG: DatePickerCustom: date: $date")
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = date
    )
    val showDatePickerDialog = rememberSaveable {
        mutableStateOf(false)
    }

    if (showDatePickerDialog.value) {
        DatePickerDialog(
            onDismissRequest = {
                showDatePickerDialog.value = false
            },
            confirmButton = {
                TextButton(onClick = {
                    showDatePickerDialog.value = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePickerDialog.value = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
    Timber.d("DLOG: DatePickerCustom: datePickerState.selectedDateMillis: ${datePickerState.selectedDateMillis}")
    OutlinedTextField(
        value = datePickerState.selectedDateMillis?.let {
            SimpleDateFormat(
                "dd/MM/yyyy",
                Locale.getDefault()
            ).format(it)
        } ?: "No selected date",
        onValueChange = {
            datePickerState.selectedDateMillis?.let {
                viewModel.onEvent(ProductAddEvent.EnteredExpiryDate(it))
            }
        },
        label = { Text(text = title) },
        modifier = modifier.clickable {
            showDatePickerDialog.value = true
        },
        placeholder = { Text(text = title) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        enabled = false,
        trailingIcon = {
            Icon(
                Icons.Filled.DateRange,
                contentDescription = null,
            )
        }
    )
}