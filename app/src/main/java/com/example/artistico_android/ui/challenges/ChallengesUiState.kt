package com.example.artistico_android.ui.challenges

import com.example.artistico_android.domain.model.Challenge
import com.example.artistico_android.domain.model.PreviousWeek

data class ChallengesUiState(
    val isLoading: Boolean = true,
    val active: Challenge? = null,
    val previousWeeks: List<PreviousWeek> = emptyList(),
    val timeRemainingMs: Long = 0L
)
