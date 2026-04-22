package com.example.artistico_android.domain.repo

import kotlinx.coroutines.flow.Flow

/**
 * Auth + session facade.
 *
 * This is UI-scaffolding only right now: the Fake impl checks a single hard-coded
 * admin credential and persists a tiny "is logged in" flag in DataStore. Swap in a
 * Firebase-backed implementation later without touching the UI layer.
 */
interface AuthRepository {

    /** Emits true once the user has a valid session. Survives process death via DataStore. */
    val isLoggedIn: Flow<Boolean>

    /** Snapshot read used on cold start to pick the nav graph's start destination. */
    suspend fun isLoggedInNow(): Boolean

    /** Returns true on success. Fake impl accepts only the hard-coded admin credentials. */
    suspend fun login(email: String, password: String): Boolean

    /** UI-only stub for now — always returns false so the caller can show a "coming soon" message. */
    suspend fun signUp(name: String, email: String, password: String): Boolean

    suspend fun logout()
}
