package com.gones.foodinventorykotlin.data.api

import com.gones.foodinventorykotlin.data.model.ProductResultResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface RemoteApi {
    @GET("product/{barcode}?fields=code,product_name,image_url")
    suspend fun getProduct(@Path("barcode") barcode: String): ProductResultResponse
}