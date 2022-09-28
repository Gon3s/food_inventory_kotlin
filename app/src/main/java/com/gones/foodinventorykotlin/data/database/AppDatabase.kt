package com.gones.foodinventorykotlin.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gones.foodinventorykotlin.data.model.ProductResponse
import com.gones.foodinventorykotlin.domain.entity.Product

@Database(entities = [Product::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}