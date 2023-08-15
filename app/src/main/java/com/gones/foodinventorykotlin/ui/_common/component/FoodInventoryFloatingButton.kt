package com.gones.foodinventorykotlin.ui._common.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import com.gones.foodinventorykotlin.ui._common.ScaffoldState
import timber.log.Timber


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodInventoryFloatingButton(
    scaffoldState: ScaffoldState,
) {
    scaffoldState.floatingActionIcon?.let { floatingActionIcon ->
        val callback = scaffoldState.floatingActionIconClick
        FloatingActionButton(
            onClick = {
                Timber.d("DLOG: FoodInventoryFloatingButton: onClick")
                callback?.invoke()
            }
        ) {
            Icon(
                floatingActionIcon,
                contentDescription = scaffoldState.floatingActionContentDescription
            )
        }
    }

}