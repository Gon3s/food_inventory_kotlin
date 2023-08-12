package com.gones.foodinventorykotlin.domain.repository

import io.github.jan.supabase.gotrue.SessionStatus
import kotlinx.coroutines.flow.StateFlow

interface AuthenticationRepository {
    suspend fun signIn(email: String, password: String): Boolean
    suspend fun signUp(email: String, password: String): Boolean
    suspend fun logout(): Boolean
    fun getSessionStatus(): StateFlow<SessionStatus>
}