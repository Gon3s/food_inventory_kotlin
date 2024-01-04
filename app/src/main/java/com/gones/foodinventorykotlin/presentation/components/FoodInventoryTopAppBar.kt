package com.gones.foodinventorykotlin.presentation.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.gones.foodinventorykotlin.presentation.components.scaffold.ScaffoldState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FoodInventoryTopAppBar(
    scaffoldState: ScaffoldState,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        navigationIcon = {
            val icon = scaffoldState.navigationIcon
            val callback = scaffoldState.onNavigationIconClick

            if (icon != null) {
                IconButton(
                    onClick = {
                        callback?.invoke()
                    }
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = scaffoldState.navigationIconContentDescription,
                    )
                }
            }
        },
        title = {
            val title = scaffoldState.title
            if (title != null) {
                Text(
                    text = stringResource(id = title),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        },
        actions = {
            val items = scaffoldState.actions
            if (items.isNotEmpty()) {
                items.forEach { item ->
                    IconButton(
                        onClick = {
                            item.onClick()
                        }
                    ) {
                        Icon(painter = painterResource(id = item.icon), contentDescription = null)
                    }
                }
            }
        },
        modifier = modifier
    )
}