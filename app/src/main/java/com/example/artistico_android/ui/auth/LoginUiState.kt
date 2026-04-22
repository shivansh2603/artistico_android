package com.example.artistico_android.ui.auth

/**
 * Single snapshot of the login screen's transient state.
 *
 * [emailError] / [passwordError] are string-resource ids so the Fragment
 * doesn't need to know the validation rules — the ViewModel hands it a res
 * id (or null to clear the error).
 */
data class LoginUiState(
    val isSubmitting: Boolean = false,
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val formError: Int? = null,
    val loginSucceeded: Boolean = false
)
