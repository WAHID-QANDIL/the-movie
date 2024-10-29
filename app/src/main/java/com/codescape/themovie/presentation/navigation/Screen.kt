package com.codescape.themovie.presentation.navigation

import com.codescape.themovie.domain.model.Movie
import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable
    data object Home : Screen()

    @Serializable
    data object Search : Screen()

    @Serializable
    data class Details(
        val movie: Movie,
        val origin: String
    ) : Screen()
}
