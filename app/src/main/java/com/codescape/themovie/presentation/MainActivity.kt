package com.codescape.themovie.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.codescape.themovie.presentation.navigation.AppNavigation
import com.codescape.themovie.presentation.theme.TheMovieTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle =
                SystemBarStyle.dark(Color(0xFF222222).toArgb()),
            navigationBarStyle =
                SystemBarStyle.dark(Color(0xFF222222).toArgb())
        )
        setContent {
            TheMovieTheme {
                AppNavigation()
            }
        }
    }
}
