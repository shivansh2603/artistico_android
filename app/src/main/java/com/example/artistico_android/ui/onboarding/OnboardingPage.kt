package com.example.artistico_android.ui.onboarding

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.artistico_android.R

/**
 * One slide in the onboarding carousel.
 *
 * [ALL] is the single source of truth for the slide order — dots count, page count,
 * and "last page" detection all derive from this list's size, so adding or removing
 * a slide is a one-line change.
 */
data class OnboardingPage(
    @DrawableRes val imageRes: Int,
    @StringRes val titleRes: Int,
    @StringRes val descRes: Int
) {
    companion object {
        val ALL: List<OnboardingPage> = listOf(
            OnboardingPage(R.drawable.placeholder_image_1, R.string.onboarding_1_title, R.string.onboarding_1_desc),
            OnboardingPage(R.drawable.placeholder_image_2, R.string.onboarding_2_title, R.string.onboarding_2_desc),
            OnboardingPage(R.drawable.placeholder_image_3, R.string.onboarding_3_title, R.string.onboarding_3_desc),
            OnboardingPage(R.drawable.placeholder_image_4, R.string.onboarding_4_title, R.string.onboarding_4_desc),
            OnboardingPage(R.drawable.placeholder_image_5, R.string.onboarding_5_title, R.string.onboarding_5_desc),
        )
    }
}
