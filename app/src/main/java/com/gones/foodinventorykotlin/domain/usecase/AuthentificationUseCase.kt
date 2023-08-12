package com.gones.foodinventorykotlin.domain.usecase

import com.gones.foodinventorykotlin.domain.repository.AuthenticationRepository
import io.github.jan.supabase.gotrue.SessionStatus
import kotlinx.coroutines.flow.StateFlow

class AuthentificationUseCase(
    private val authenticationRepository: AuthenticationRepository,
) {
    fun getSessionStatus(): StateFlow<SessionStatus> = authenticationRepository.getSessionStatus()

    suspend fun register(email: String, password: String) =
        authenticationRepository.signUp(email, password)

    suspend fun login(email: String, password: String) =
        authenticationRepository.signIn(email, password)

    suspend fun logout() = authenticationRepository.logout()
}