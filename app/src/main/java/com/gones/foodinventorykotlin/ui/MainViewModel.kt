package com.gones.foodinventorykotlin.ui

import androidx.lifecycle.ViewModel
import com.gones.foodinventorykotlin.domain.usecase.AuthentificationUseCase
import com.gones.foodinventorykotlin.ui._common.navigation.HomeRoute
import com.gones.foodinventorykotlin.ui._common.navigation.LoginRoute
import io.github.jan.supabase.gotrue.SessionStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainViewModel(
    private val authentificationUseCase: AuthentificationUseCase,
) : ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    var defaultRoute: String = HomeRoute

    init {
        CoroutineScope(Dispatchers.IO).launch {
            authentificationUseCase.getSessionStatus().collectLatest { sessionStatus ->
                defaultRoute = when (sessionStatus) {
                    is SessionStatus.Authenticated -> {
                        HomeRoute
                    }

                    else -> {
                        LoginRoute
                    }
                }
                _isLoading.value = false
            }
        }
    }
}