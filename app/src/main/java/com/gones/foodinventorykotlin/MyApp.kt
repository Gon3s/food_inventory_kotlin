package com.gones.foodinventorykotlin

import android.app.Application
import com.gones.foodinventorykotlin.common.appModules
import org.koin.core.context.startKoin
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(applicationContext)
            modules(appModules)
        }
    }
}