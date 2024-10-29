package com.codescape.themovie.presentation.home.preview

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.tooling.preview.datasource.CollectionPreviewParameterProvider
import androidx.paging.PagingData
import com.codescape.themovie.domain.model.Movie
import com.codescape.themovie.presentation.home.HomeUiState
import kotlinx.coroutines.flow.flowOf

class HomePreviewParametersProvider :
    CollectionPreviewParameterProvider<HomeUiState>(
        listOf(
            HomeUiState(
                movies =
                    flowOf(
                        PagingData.from(
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
                    ),
                favoriteMovies =
                    mutableStateListOf(
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
