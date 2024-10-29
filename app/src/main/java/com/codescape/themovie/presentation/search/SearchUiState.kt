package com.codescape.themovie.presentation.search

import androidx.compose.ui.text.input.TextFieldValue
import com.codescape.themovie.domain.model.Movie

data class SearchUiState(
    val search: TextFieldValue = TextFieldValue(),
    val movies: List<Movie> = emptyList()
)
