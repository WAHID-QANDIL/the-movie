package com.codescape.themovie.presentation.details.preview

import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import com.codescape.themovie.domain.model.Movie

class DetailsPreviewParametersProvider :
    CollectionPreviewParameterProvider<Movie>(
        listOf(
            Movie(
                id = 1,
                title = "Title",
                overview = "Overview",
                posterPath = "https://www.google.com",
                backdropPath = "https://www.google.com",
                releaseDate = "2023-01-01",
                voteAverage = 1.0,
                voteCount = 1,
                popularity = 1.0,
                adult = false,
                originalLanguage = "en",
                originalTitle = "Original Title",
                video = false,
                query = emptyMap()
            )
        )
    )
