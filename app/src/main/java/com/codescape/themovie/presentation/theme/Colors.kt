package com.codescape.themovie.presentation.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class Colors(
    val component: Color,
    val background: Color,
    val text: Color,
    val textVariant: Color,
    val textLink: Color,
    val highlight: Color,
    val success: Color,
    val error: Color,
    val outline: Color,
    val onOutline: Color
)

val LocalColors =
    staticCompositionLocalOf {
        Colors(
            component = Color.Unspecified,
            background = Color.Unspecified,
            text = Color.Unspecified,
            textVariant = Color.Unspecified,
            textLink = Color.Unspecified,
            highlight = Color.Unspecified,
            success = Color.Unspecified,
            error = Color.Unspecified,
            outline = Color.Unspecified,
            onOutline = Color.Unspecified
        )
    }
