package com.codescape.themovie.presentation.search.preview

import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import com.codescape.themovie.domain.model.Movie
import com.codescape.themovie.presentation.search.SearchUiState

class SearchPreviewParametersProvider :
    CollectionPreviewParameterProvider<SearchUiState>(
        listOf(
            SearchUiState(
                movies =
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
        )
    )
