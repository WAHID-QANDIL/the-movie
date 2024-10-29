package com.codescape.themovie.presentation.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp

@Immutable
data class Elevations(
    val default: Dp,
    val pressed: Dp
)

val LocalElevations =
    staticCompositionLocalOf {
        Elevations(
            default = Dp.Unspecified,
            pressed = Dp.Unspecified
        )
    }
