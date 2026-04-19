package com.example.artistico_android.domain.model

import java.time.Instant

data class Challenge(
    val id: String,
    val title: String,
    val description: String,
    val coverRes: Int,
    val startsAt: Instant,
    val endsAt: Instant,
    val hasSubmission: Boolean = false
)

data class PreviousWeek(
    val id: String,
    val weekNumber: Int,
    val theme: String,
    val dateRangeLabel: String,
    val coverRes: Int
)
