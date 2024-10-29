package com.codescape.themovie.presentation.home

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.paging.PagingData
import com.codescape.themovie.domain.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

data class HomeUiState(
    val movies: Flow<PagingData<Movie>> = emptyFlow(),
    val favoriteMovies: SnapshotStateList<Movie> = mutableStateListOf<Movie>(),
    val isLoading: Boolean = false,
    val error: String? = null
)
