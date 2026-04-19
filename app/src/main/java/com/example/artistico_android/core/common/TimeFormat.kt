package com.example.artistico_android.core.common

import com.example.artistico_android.R
import java.time.Duration
import java.time.Instant

object TimeFormat {

    fun relativeFromNow(instant: Instant, now: Instant = Instant.now()): UiText {
        val seconds = Duration.between(instant, now).seconds.coerceAtLeast(0)
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        val months = days / 30

        return when {
            seconds < 60 -> UiText.of(R.string.time_just_now)
            minutes < 60 -> UiText.of(R.string.time_ago_minutes, minutes.toInt())
            hours < 24 -> UiText.of(R.string.time_ago_hours, hours.toInt())
            days < 30 -> UiText.of(R.string.time_ago_days, days.toInt())
            else -> UiText.of(R.string.time_ago_months, months.toInt())
        }
    }
}
