package com.gones.foodinventorykotlin.domain.entity

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@Entity(tableName = "product")
data class Product(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int = 0,
    @ColumnInfo(name = "barcode")
    val barcode: String,
    @ColumnInfo(name = "brands")
    var brands: String,
    @ColumnInfo(name = "categories")
    var categories: String,
    @ColumnInfo(name = "image_url")
    val imageUrl: String,
    @ColumnInfo(name = "product_name")
    var productName: String,
    @ColumnInfo(name = "expiry_date")
    var expiry_date: Long = 0L,
    var quantity: Int? = null,
    var expiries_dates: String = "",
) : Parcelable

class InvalidProductException(message: String) : Exception(message)