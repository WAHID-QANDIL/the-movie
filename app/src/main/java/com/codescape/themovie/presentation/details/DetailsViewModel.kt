package com.codescape.themovie.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codescape.themovie.domain.model.Movie
import com.codescape.themovie.domain.usecase.DeleteFavoriteMovieUseCase
import com.codescape.themovie.domain.usecase.GetIsFavoriteMovieUseCase
import com.codescape.themovie.domain.usecase.SaveFavoriteMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
@FlowPreview
@HiltViewModel
class DetailsViewModel @Inject constructor(
    getIsFavoriteMovieUseCase: GetIsFavoriteMovieUseCase,
    private val saveFavoriteMovieUseCase: SaveFavoriteMovieUseCase,
    private val deleteFavoriteMovieUseCase: DeleteFavoriteMovieUseCase
) : ViewModel() {
    private val movie = MutableStateFlow<Movie?>(null)

    val isFavoriteMovie =
        movie
            .flatMapLatest { movie ->
                if (movie != null) {
                    getIsFavoriteMovieUseCase(movie)
                } else {
                    flowOf(false)
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5.seconds),
                initialValue = false
            )

    fun updateMovie(movie: Movie) {
        this.movie.value = movie
    }

    fun saveFavoriteMovie(movie: Movie) {
        viewModelScope.launch {
            saveFavoriteMovieUseCase(movie)
        }
    }

    fun deleteFavoriteMovie(movie: Movie) {
        viewModelScope.launch {
            deleteFavoriteMovieUseCase(movie)
        }
    }
}
