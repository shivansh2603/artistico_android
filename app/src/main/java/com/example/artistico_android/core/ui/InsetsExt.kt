package com.example.artistico_android.core.ui

import android.view.View
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

fun View.applySystemBarInsetsAsPadding(
    top: Boolean = false,
    bottom: Boolean = false,
    horizontal: Boolean = false
) {
    ViewCompat.setOnApplyWindowInsetsListener(this) { v, insets ->
        val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.ime())
        v.updatePadding(
            left = if (horizontal) bars.left else v.paddingLeft,
            right = if (horizontal) bars.right else v.paddingRight,
            top = if (top) bars.top else v.paddingTop,
            bottom = if (bottom) bars.bottom else v.paddingBottom
        )
        insets
    }
}
