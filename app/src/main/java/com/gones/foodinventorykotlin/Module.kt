package com.gones.foodinventorykotlin

import com.gones.foodinventorykotlin.data.dataModule
import com.gones.foodinventorykotlin.data.repositoryModule
import com.gones.foodinventorykotlin.domain.useCaseModule
import com.gones.foodinventorykotlin.presentation.viewModelModule

val appModules by lazy {
    listOf(
        viewModelModule,
        useCaseModule,
        repositoryModule,
        dataModule
    )
}
