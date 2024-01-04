package com.gones.foodinventorykotlin.data

import com.gones.foodinventorykotlin.data.api.RemoteApi
import com.gones.foodinventorykotlin.data.api.network.createApiClient
import com.gones.foodinventorykotlin.data.api.network.initSupabaseClient
import com.gones.foodinventorykotlin.data.local.KeychainManager
import com.gones.foodinventorykotlin.data.model.User
import com.gones.foodinventorykotlin.data.repository.AuthenticationRepositoryImpl
import com.gones.foodinventorykotlin.data.repository.CategoryRepositoryImpl
import com.gones.foodinventorykotlin.data.repository.ProductRepositoryImpl
import com.gones.foodinventorykotlin.domain.repository.AuthenticationRepository
import com.gones.foodinventorykotlin.domain.repository.CategoryRepository
import com.gones.foodinventorykotlin.domain.repository.ProductRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module


val dataModule: Module = module {
    single { initSupabaseClient() }
    single { createApiClient().create(RemoteApi::class.java) }
    single { KeychainManager(androidContext(), User::class.java) }
}

val repositoryModule: Module = module {
    single {
        ProductRepositoryImpl(
            remoteApi = get(),
            supabaseClient = get()
        ) as ProductRepository
    }
    single {
        AuthenticationRepositoryImpl(
            supabaseClient = get(),
            userKeychainManager = get()
        ) as AuthenticationRepository
    }
    single {
        CategoryRepositoryImpl(
            supabaseClient = get()
        ) as CategoryRepository
    }
}