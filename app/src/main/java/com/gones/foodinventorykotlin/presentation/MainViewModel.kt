package com.gones.foodinventorykotlin.presentation

import androidx.lifecycle.ViewModel
import com.gones.foodinventorykotlin.domain.usecase.AuthentificationUseCase
import com.gones.foodinventorykotlin.presentation.navigation.HomeRoute
import com.gones.foodinventorykotlin.presentation.navigation.LoginRoute
import com.gones.foodinventorykotlin.presentation.navigation.SplashRoute
import io.github.jan.supabase.gotrue.SessionStatus
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import timber.log.Timber

class MainViewModel(
    private val authentificationUseCase: AuthentificationUseCase,
) : ViewModel() {
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _isConnected = MutableStateFlow(false)
    val isConnected = _isConnected.asStateFlow()

    var defaultRoute = MutableStateFlow(SplashRoute)
        private set

    init {
        Timber.d("DLOG: init MainViewModel")
        CoroutineScope(Dispatchers.IO).launch {
            authentificationUseCase.getSessionStatus().collect { sessionStatus ->
                Timber.d("DLOG: sessionStatus: ${sessionStatus.let { it::class.simpleName }}")
                defaultRoute.value = when (sessionStatus) {
                    is SessionStatus.Authenticated -> {
                        Timber.d("DLOG: Authenticated")

                        runBlocking {
                            Timber.d("DLOG: setCurrentUser")
                            authentificationUseCase.setCurrentUser()
                        }

                        _isConnected.value = true
                        Timber.d("DLOG: HomeRoute")
                        HomeRoute
                    }

                    is SessionStatus.LoadingFromStorage -> {
                        Timber.e("DLOG: LoadingFromStorage")
                        return@collect
                    }

                    else -> {
                        runBlocking {
                            Timber.d("DLOG: removeCurrentUser")
                            authentificationUseCase.removeCurrentUser()
                        }

                        _isConnected.value = false
                        Timber.d("DLOG: LoginRoute")
                        LoginRoute
                    }
                }
                _isLoading.value = false
            }
        }
    }
}