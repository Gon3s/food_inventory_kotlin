package com.gones.foodinventorykotlin.domain

import com.gones.foodinventorykotlin.domain.usecase.AuthentificationUseCase
import com.gones.foodinventorykotlin.domain.usecase.CategoryUseCase
import com.gones.foodinventorykotlin.domain.usecase.ProductUseCase
import com.gones.foodinventorykotlin.domain.usecase.validations.ValidateEmail
import com.gones.foodinventorykotlin.domain.usecase.validations.ValidatePassword
import org.koin.core.module.Module
import org.koin.dsl.module


val useCaseModule: Module = module {
    single { ValidateEmail() }
    single { ValidatePassword() }
    single { ProductUseCase(productRepository = get()) }
    single { AuthentificationUseCase(authenticationRepository = get()) }
    single { CategoryUseCase(categoryRepository = get()) }
}