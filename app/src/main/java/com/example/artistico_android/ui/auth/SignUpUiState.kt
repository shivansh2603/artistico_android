package com.example.artistico_android.ui.auth

data class SignUpUiState(
    val isSubmitting: Boolean = false,
    val nameError: Int? = null,
    val emailError: Int? = null,
    val passwordError: Int? = null,
    val confirmPasswordError: Int? = null,
    val formError: Int? = null,
    /** Sign-up is UI-only for now — we emit this one-shot flag so the fragment shows a "coming soon" toast. */
    val signUpPreviewShown: Boolean = false
)
