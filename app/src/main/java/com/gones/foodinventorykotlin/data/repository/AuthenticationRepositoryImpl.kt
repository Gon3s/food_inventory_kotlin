package com.gones.foodinventorykotlin.data.repository

import com.gones.foodinventorykotlin.data.local.KeychainManager
import com.gones.foodinventorykotlin.data.model.User
import com.gones.foodinventorykotlin.domain.repository.AuthenticationRepository
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.SessionStatus
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber

class AuthenticationRepositoryImpl(
    private val supabaseClient: SupabaseClient,
    private val userKeychainManager: KeychainManager<User>
) : AuthenticationRepository {
    override suspend fun signIn(email: String, password: String): Boolean {
        return try {
            Timber.d("DLOG: signIn: $email")
            supabaseClient.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }

            true
        } catch (e: Exception) {
            Timber.e("DLOG: signIn: ${e.message}")
            false
        }
    }

    override suspend fun signUp(email: String, password: String): Boolean {
        return try {
            Timber.d("DLOG: signUp: $email")
            supabaseClient.auth.signUpWith(Email) {
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
            supabaseClient.auth.signOut()

            userKeychainManager.removeDataFromKeychain<User>()

            true
        } catch (e: Exception) {
            Timber.e("DLOG: signUp: ${e.message}")
            false
        }
    }

    override fun getSessionStatus(): StateFlow<SessionStatus> {
        return supabaseClient.auth.sessionStatus
    }

    override fun getCurrentUser(): User? {
        return userKeychainManager.getDataFromKeychain<User>()?.let { user ->
            Timber.d("DLOG: getCurrentUser - ${user.id}")
            user
        }
    }

    override suspend fun setCurrentUser() {
        supabaseClient.auth.currentUserOrNull()?.let { user ->
            val data = supabaseClient.postgrest
                .from("profiles")
                .select(
                    Columns.list("id", "first_name", "last_name", "pseudo")
                )

            data.decodeSingleOrNull<Map<String, String>>()?.let { decodeData ->
                setUserDataToKeychain(user.email.toString(), decodeData)
            } ?: throw Exception("User not found")
        }
    }

    override suspend fun removeCurrentUser() {
        userKeychainManager.removeDataFromKeychain<User>()
    }

    private fun setUserDataToKeychain(email: String, data: Map<String, Any>) {
        val user = User(
            id = data["id"] as String,
            email = email,
            first_name = data["first_name"] as String,
            last_name = data["last_name"] as String,
            pseudo = data["pseudo"] as String,
        )

        Timber.d("DLOG: setUserDataToKeychain - ${user.id}")
        userKeychainManager.saveDataToKeychain(user)
    }
}