package com.gones.foodinventorykotlin.ui.common.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.gones.foodinventorykotlin.ui.common.AppBarState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodInventoryTopAppBar(
    appBarState: AppBarState,
    modifier: Modifier = Modifier,
) {
    var menuExpanded by remember { mutableStateOf(false) }

    TopAppBar(
        navigationIcon = {
            val icon = appBarState.navigationIcon
            val callback = appBarState.onNavigationIconClick

            if (icon != null) {
                IconButton(
                    onClick = {
                        callback?.invoke()
                    }
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = appBarState.navigationIconContentDescription,
                    )
                }
            }
        },
        title = {
            val title = appBarState.title
            if (title != null) {
                Text(
                    text = stringResource(id = title),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        },
        actions = {
            val items = appBarState.actions
            if (items.isNotEmpty()) {
                items.forEach { item ->
                    IconButton(
                        onClick = {
                            item.onClick()
                        }
                    ) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.contentDescription,
                        )
                    }
                }
            }
        },
        modifier = modifier
    )
}