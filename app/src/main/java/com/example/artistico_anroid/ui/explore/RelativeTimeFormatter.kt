package com.example.artistico_anroid.ui.explore

import android.content.Context
import com.example.artistico_anroid.core.common.TimeFormat
import java.time.Instant

object RelativeTimeFormatter {
    fun format(context: Context, instant: Instant): String =
        TimeFormat.relativeFromNow(instant).asString(context)
}
