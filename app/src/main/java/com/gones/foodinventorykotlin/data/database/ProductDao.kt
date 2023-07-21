package com.gones.foodinventorykotlin.data.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.gones.foodinventorykotlin.domain.entity.Product

@Dao
interface ProductDao {
    @Query("SELECT *, COUNT(*) as quantity, GROUP_CONCAT(expiry_date) as expiries_dates FROM product GROUP BY barcode")
    fun getProducts(): LiveData<List<Product>>

    @Query("SELECT * FROM product WHERE barcode = :barcode LIMIT 1")
    fun getProductByBarcode(barcode: String): Product?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertProduct(product: Product)

    @Delete
    fun deleteProduct(product: Product)
}