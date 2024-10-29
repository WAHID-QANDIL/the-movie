package com.codescape.themovie.presentation.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object TheMovieTheme {
    val colors: Colors
        @Composable
        get() = LocalColors.current
    val typography: Typography
        @Composable
        get() = LocalTypography.current
    val elevations: Elevations
        @Composable
        get() = LocalElevations.current
    val shapes: Shapes
        @Composable
        get() = LocalShapes.current
}

@Composable
fun TheMovieTheme(content: @Composable () -> Unit) {
    val colors =
        Colors(
            component = Color(0xFF141414),
            background = Color(0xFF222222),
            text = Color(0xFFFFFFFF),
            textVariant = Color(0xFF757575),
            textLink = Color(0xFFE50914),
            highlight = Color(0xFFE50914),
            success = Color(0xFF13BC24),
            error = Color(0xFFF82D2D),
            outline = Color(0xFF333333),
            onOutline = Color(0xFF666666)
        )
    val typography =
        Typography(
            titleVeryLarge =
                TextStyle(
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.W700,
                    fontSize = 18.sp,
                    lineHeight = 21.09.sp
                ),
            titleLarge =
                TextStyle(
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.W700,
                    fontSize = 16.sp,
                    lineHeight = 18.75.sp
                ),
            titleMedium =
                TextStyle(
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.W700,
                    fontSize = 14.sp,
                    lineHeight = 16.41.sp
                ),
            titleSmall =
                TextStyle(
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.W700,
                    fontSize = 12.sp,
                    lineHeight = 14.06.sp
                ),
            bodyLarge =
                TextStyle(
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.W400,
                    fontSize = 16.sp,
                    lineHeight = 18.75.sp
                ),
            bodyMedium =
                TextStyle(
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.W400,
                    fontSize = 14.sp,
                    lineHeight = 16.41.sp
                ),
            bodySmall =
                TextStyle(
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.W400,
                    fontSize = 12.sp,
                    lineHeight = 14.06.sp
                )
        )
    val elevations =
        Elevations(
            default = 2.dp,
            pressed = 4.dp
        )
    val shapes =
        Shapes(
            large = RoundedCornerShape(24.dp),
            medium = RoundedCornerShape(16.dp),
            small = RoundedCornerShape(8.dp)
        )
    CompositionLocalProvider(
        LocalColors provides colors,
        LocalTypography provides typography,
        LocalElevations provides elevations,
        LocalShapes provides shapes,
        content = content
    )
}
