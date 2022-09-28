package com.gones.foodinventorykotlin.common

import com.gones.foodinventorykotlin.data.api.LocalApi
import com.gones.foodinventorykotlin.data.api.LocalApiImpl
import com.gones.foodinventorykotlin.data.api.RemoteApi
import com.gones.foodinventorykotlin.data.database.createDatabase
import com.gones.foodinventorykotlin.data.database.createProductDao
import com.gones.foodinventorykotlin.data.repository.ProductRepositoryImpl
import com.gones.foodinventorykotlin.domain.repository.ProductRepository
import com.gones.foodinventorykotlin.domain.usecase.ProductUseCase
import com.gones.foodinventorykotlin.ui.product.viewmodel.ProductAddViewModel
import com.gones.foodinventorykotlin.ui.scan.viewmodel.ScanViewModel
import createApiClient
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
    viewModel { ScanViewModel(get()) }
    viewModel { (barcode: String) -> ProductAddViewModel(get(), barcode) }
}

val useCaseModule: Module = module {
    single { ProductUseCase(productRepository = get()) }
}

val repositoryModule: Module = module {
    single { ProductRepositoryImpl(remoteApi = get(), productDao = get()) as ProductRepository }
}

val dataModule: Module = module {
    single { createApiClient().create(RemoteApi::class.java) }
    single<LocalApi> { LocalApiImpl(appContext = get()) }
    single { createDatabase(appContext = get()) }
    single { createProductDao(database = get()) }
}