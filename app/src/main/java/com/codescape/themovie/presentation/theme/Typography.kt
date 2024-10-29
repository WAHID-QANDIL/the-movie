package com.codescape.themovie.presentation.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle

@Immutable
data class Typography(
    val titleVeryLarge: TextStyle,
    val titleLarge: TextStyle,
    val titleMedium: TextStyle,
    val titleSmall: TextStyle,
    val bodyLarge: TextStyle,
    val bodyMedium: TextStyle,
    val bodySmall: TextStyle
)

val LocalTypography =
    staticCompositionLocalOf {
        Typography(
            titleVeryLarge = TextStyle.Default,
            titleLarge = TextStyle.Default,
            titleMedium = TextStyle.Default,
            titleSmall = TextStyle.Default,
            bodyLarge = TextStyle.Default,
            bodyMedium = TextStyle.Default,
            bodySmall = TextStyle.Default
        )
    }
