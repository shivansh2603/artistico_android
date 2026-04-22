package com.example.artistico_android.domain.repo

import kotlinx.coroutines.flow.Flow

/**
 * Persists the "user has seen the onboarding carousel" flag so returning users
 * skip straight to login (or Explore if they already have a session).
 *
 * Lives in DataStore so it survives process death and app restarts.
 */
interface OnboardingRepository {

    val hasCompletedOnboarding: Flow<Boolean>

    /** Snapshot read used on cold start to pick the nav graph's start destination. */
    suspend fun hasCompletedOnboardingNow(): Boolean

    suspend fun markComplete()
}
