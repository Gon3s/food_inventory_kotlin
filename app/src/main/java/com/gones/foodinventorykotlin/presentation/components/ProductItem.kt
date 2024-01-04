package com.gones.foodinventorykotlin.presentation.components

import android.text.format.DateFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.gones.foodinventorykotlin.R
import com.gones.foodinventorykotlin.domain.entity.Product
import java.time.Instant
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

    Row(
        modifier = modifier
            .padding(8.dp),
        verticalAlignment = Alignment.Top
    ) {
        AsyncImage(
            model = ImageRequest.Builder(
                LocalContext.current
            ).data(product.image_url)
                .crossfade(true).build(),
            contentDescription = product.product_name,
            error = painterResource(R.drawable.splash),
            fallback = painterResource(R.drawable.splash),
            modifier = Modifier
                .size(size)
        )
        Column(
            verticalArrangement = Arrangement.Top,
        ) {
            Text(text = product.product_name ?: "", fontSize = 18.sp)
            Text(text = expiryDate, fontSize = 14.sp)
        }
    }
}

@Preview(
    showBackground = true,
    locale = "fr_FR",
    widthDp = 360
)
@Composable
fun ProductItemPreview() {
    ProductItem(
        product = Product(
            product_name = "Product name",
            image_url = "https://images.openfoodfacts.org/images/products/544/900/021/4911/front_fr.200.full.jpg",
            expiry_date = Instant.now().toEpochMilli(),
        )
    )
}