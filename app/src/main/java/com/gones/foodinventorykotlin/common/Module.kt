package com.gones.foodinventorykotlin.common

import com.gones.foodinventorykotlin.data.api.RemoteApi
import com.gones.foodinventorykotlin.data.network.createApiClient
import com.gones.foodinventorykotlin.data.network.initSupabaseClient
import com.gones.foodinventorykotlin.data.repository.ProductRepositoryImpl
import com.gones.foodinventorykotlin.domain.repository.ProductRepository
import com.gones.foodinventorykotlin.domain.usecase.ProductUseCase
import com.gones.foodinventorykotlin.ui.home.HomeViewModel
import com.gones.foodinventorykotlin.ui.product.viewmodel.ProductViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

val appModules by lazy {
    listOf(
        viewModelModule,
        useCaseModule,
        repositoryModule,
        dataModule
    )
}

val viewModelModule: Module = module {
    viewModel { HomeViewModel(get()) }
    viewModel { (barcode: String?, id: String?) ->
        ProductViewModel(
            get(),
            barcode = barcode,
            id = id
        )
    }
}

val useCaseModule: Module = module {
    single { ProductUseCase(productRepository = get()) }
}

val repositoryModule: Module = module {
    single {
        ProductRepositoryImpl(
            remoteApi = get(),
            supabaseClient = get()
        ) as ProductRepository
    }
}

val dataModule: Module = module {
    single { initSupabaseClient() }
    single { createApiClient().create(RemoteApi::class.java) }
}