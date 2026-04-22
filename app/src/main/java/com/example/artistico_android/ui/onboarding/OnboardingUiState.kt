package com.example.artistico_android.ui.onboarding

data class OnboardingUiState(
    /** Flipped true after markComplete() writes to DataStore — the Fragment consumes it and navigates. */
    val onboardingCompleted: Boolean = false
)
