package com.gones.foodinventorykotlin.ui.common.component

import android.text.format.DateFormat
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.gones.foodinventorykotlin.R
import com.gones.foodinventorykotlin.domain.entity.Product
import java.util.Date

@Composable
fun ProductItem(
    product: Product,
    modifier: Modifier = Modifier,
    size: Dp = 90.dp,
) {
    val dateFormat = DateFormat.getDateFormat(LocalContext.current)
    val expiryDate = product.expiry_date?.let {
        dateFormat.format(Date(it))
    } ?: run {
        stringResource(R.string.no_expiry_date)
    }

    Box(modifier = modifier) {
        Row {
            AsyncImage(
                model = ImageRequest.Builder(
                    LocalContext.current
                ).data(product.image_url)
                    .crossfade(true).build(),
                contentDescription = product.product_name,
                modifier = Modifier
                    .padding(8.dp)
                    .size(size)
            )
            Column {
                product.brands?.let { Text(text = it, fontSize = 16.sp) }
                Text(text = product.product_name, fontSize = 18.sp)
                Text(text = expiryDate, fontSize = 16.sp)
            }
        }
    }
}