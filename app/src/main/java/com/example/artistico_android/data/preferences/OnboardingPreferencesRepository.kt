package com.example.artistico_android.data.preferences

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.artistico_android.domain.repo.OnboardingRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * DataStore-backed persistence of a single boolean flag.
 *
 * Uses its own Preferences file ("artistico_onboarding") rather than sharing with
 * auth — these two concerns have different lifetimes (onboarding is permanent per
 * install; auth session clears on logout) and different ownership, so coupling
 * them would invite weird bugs later.
 */
@Singleton
class OnboardingPreferencesRepository @Inject constructor(
    @ApplicationContext private val context: Context
) : OnboardingRepository {

    private val dataStore = context.onboardingDataStore

    override val hasCompletedOnboarding: Flow<Boolean> =
        dataStore.data.map { prefs -> prefs[KEY_COMPLETED] ?: false }

    override suspend fun hasCompletedOnboardingNow(): Boolean = hasCompletedOnboarding.first()

    override suspend fun markComplete() {
        dataStore.edit { it[KEY_COMPLETED] = true }
    }

    private companion object {
        val KEY_COMPLETED = booleanPreferencesKey("onboarding_completed")
    }
}

private val Context.onboardingDataStore by preferencesDataStore(name = "artistico_onboarding")
