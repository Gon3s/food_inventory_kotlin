package com.gones.foodinventorykotlin.presentation

import com.gones.foodinventorykotlin.presentation.components.drawer.DrawerViewModel
import com.gones.foodinventorykotlin.presentation.pages.auth.login.LoginViewModel
import com.gones.foodinventorykotlin.presentation.pages.auth.register.RegisterViewModel
import com.gones.foodinventorykotlin.presentation.pages.home.HomeViewModel
import com.gones.foodinventorykotlin.presentation.pages.manageCategories.ManageCategoriesViewModel
import com.gones.foodinventorykotlin.presentation.pages.product.ProductViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

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
    viewModel { DrawerViewModel(get()) }
}