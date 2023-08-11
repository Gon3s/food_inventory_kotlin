package com.gones.foodinventorykotlin.ui.common.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import com.gones.foodinventorykotlin.ui.common.AppBarState
import timber.log.Timber


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodInventoryFloatingButton(
    appBarState: AppBarState,
) {
    appBarState.floatingActionIcon?.let { floatingActionIcon ->
        val callback = appBarState.floatingActionIconClick
        FloatingActionButton(
            onClick = {
                Timber.d("DLOG: FoodInventoryFloatingButton: onClick")
                callback?.invoke()
            }
        ) {
            Icon(
                floatingActionIcon,
                contentDescription = appBarState.floatingActionContentDescription
            )
        }
    }

}