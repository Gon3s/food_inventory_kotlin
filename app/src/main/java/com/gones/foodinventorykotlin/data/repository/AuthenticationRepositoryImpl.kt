package com.gones.foodinventorykotlin.data.repository

import com.gones.foodinventorykotlin.domain.repository.AuthenticationRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.SessionStatus
import io.github.jan.supabase.gotrue.gotrue
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.gotrue.user.UserInfo
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber

class AuthenticationRepositoryImpl(
    private val supabaseClient: SupabaseClient,
) : AuthenticationRepository {
    override suspend fun signIn(email: String, password: String): Boolean {
        return try {
            supabaseClient.gotrue.loginWith(Email) {
                this.email = email
                this.password = password
            }
            val user: UserInfo =
                supabaseClient.gotrue.retrieveUserForCurrentSession(updateSession = true)

            true
        } catch (e: Exception) {
            Timber.e("DLOG: signIn: ${e.message}")
            false
        }
    }

    override suspend fun signUp(email: String, password: String): Boolean {
        return try {
            supabaseClient.gotrue.signUpWith(Email) {
                this.email = email
                this.password = password
            }
            true
        } catch (e: Exception) {
            Timber.e("DLOG: signUp: ${e.message}")
            false
        }
    }

    override suspend fun logout(): Boolean {
        return try {
            supabaseClient.gotrue.logout()
            true
        } catch (e: Exception) {
            Timber.e("DLOG: signUp: ${e.message}")
            false
        }
    }

    override fun getSessionStatus(): StateFlow<SessionStatus> {
        return supabaseClient.gotrue.sessionStatus
    }
}