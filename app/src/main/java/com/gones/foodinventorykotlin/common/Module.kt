package com.gones.foodinventorykotlin.common

import com.gones.foodinventorykotlin.data.api.RemoteApi
import com.gones.foodinventorykotlin.data.network.createApiClient
import com.gones.foodinventorykotlin.data.network.initSupabaseClient
import com.gones.foodinventorykotlin.data.repository.AuthenticationRepositoryImpl
import com.gones.foodinventorykotlin.data.repository.CategoryRepositoryImpl
import com.gones.foodinventorykotlin.data.repository.ProductRepositoryImpl
import com.gones.foodinventorykotlin.domain.repository.AuthenticationRepository
import com.gones.foodinventorykotlin.domain.repository.CategoryRepository
import com.gones.foodinventorykotlin.domain.repository.ProductRepository
import com.gones.foodinventorykotlin.domain.usecase.AuthentificationUseCase
import com.gones.foodinventorykotlin.domain.usecase.CategoryUseCase
import com.gones.foodinventorykotlin.domain.usecase.ProductUseCase
import com.gones.foodinventorykotlin.domain.usecase.validations.ValidateEmail
import com.gones.foodinventorykotlin.domain.usecase.validations.ValidatePassword
import com.gones.foodinventorykotlin.ui.MainViewModel
import com.gones.foodinventorykotlin.ui.auth.login.LoginViewModel
import com.gones.foodinventorykotlin.ui.auth.register.RegisterViewModel
import com.gones.foodinventorykotlin.ui.home.HomeViewModel
import com.gones.foodinventorykotlin.ui.manageCategories.ManageCategoriesViewModel
import com.gones.foodinventorykotlin.ui.product.ProductViewModel
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
    viewModel { MainViewModel(get()) }
    viewModel { HomeViewModel(get(), get()) }
    viewModel { LoginViewModel(get(), get(), get()) }
    viewModel { RegisterViewModel(get(), get(), get()) }
    viewModel { (barcode: String?, id: String?) ->
        ProductViewModel(
            get(),
            get(),
            barcode = barcode,
            id = id
        )
    }
    viewModel { ManageCategoriesViewModel(get()) }
}

val useCaseModule: Module = module {
    single { ValidateEmail() }
    single { ValidatePassword() }
    single { ProductUseCase(productRepository = get()) }
    single { AuthentificationUseCase(authenticationRepository = get()) }
    single { CategoryUseCase(categoryRepository = get()) }
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
            supabaseClient = get()
        ) as AuthenticationRepository
    }
    single {
        CategoryRepositoryImpl(
            supabaseClient = get()
        ) as CategoryRepository
    }
}

val dataModule: Module = module {
    single { initSupabaseClient() }
    single { createApiClient().create(RemoteApi::class.java) }
}