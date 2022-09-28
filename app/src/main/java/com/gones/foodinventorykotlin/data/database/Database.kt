package com.gones.foodinventorykotlin.data.database

import android.content.Context
import androidx.room.Room

fun createDatabase(appContext: Context): AppDatabase = Room.databaseBuilder(appContext, AppDatabase::class.java, "food-inventory").build()

fun createProductDao(database: AppDatabase) = database.productDao()