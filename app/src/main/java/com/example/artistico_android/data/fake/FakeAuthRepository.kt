package com.example.artistico_android.data.fake

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.artistico_android.domain.repo.AuthRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * UI-scaffolding auth: one hard-coded admin credential, session flag in DataStore.
 *
 * Replace with a real Firebase Auth / backend-backed implementation later — no UI
 * layer changes will be required (the interface stays the same).
 */
@Singleton
class FakeAuthRepository @Inject constructor(
    @ApplicationContext private val context: Context
) : AuthRepository {

    private val dataStore = context.authDataStore

    override val isLoggedIn: Flow<Boolean> =
        dataStore.data.map { prefs -> prefs[KEY_LOGGED_IN] ?: false }

    override suspend fun isLoggedInNow(): Boolean = isLoggedIn.first()

    override suspend fun login(email: String, password: String): Boolean {
        val ok = email.trim().equals(ADMIN_EMAIL, ignoreCase = true) && password == ADMIN_PASSWORD
        if (ok) {
            dataStore.edit { it[KEY_LOGGED_IN] = true }
        }
        return ok
    }

    override suspend fun signUp(name: String, email: String, password: String): Boolean {
        // UI-only for now; never actually creates an account.
        return false
    }

    override suspend fun logout() {
        dataStore.edit { it[KEY_LOGGED_IN] = false }
    }

    companion object {
        // Hard-coded admin account for UI preview only.
        // TODO: remove once real auth is wired.
        const val ADMIN_EMAIL = "shivansh@artistico.com"
        const val ADMIN_PASSWORD = "prasad"
        const val ADMIN_DISPLAY_NAME = "Shivansh Prasad"

        private val KEY_LOGGED_IN = booleanPreferencesKey("is_logged_in")
    }
}

private val Context.authDataStore by preferencesDataStore(name = "artistico_auth")
