package com.codescape.themovie.presentation.shared

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.runtime.compositionLocalOf

val LocalAnimatedVisibilityScope =
    compositionLocalOf<AnimatedVisibilityScope?> {
        null
    }
