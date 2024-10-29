package com.codescape.themovie.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.codescape.themovie.domain.model.Movie
import com.codescape.themovie.domain.usecase.GetFavoriteMoviesUseCase
import com.codescape.themovie.domain.usecase.GetMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@OptIn(ExperimentalCoroutinesApi::class)
@FlowPreview
@HiltViewModel
class HomeViewModel @Inject constructor(
    getMoviesUseCase: GetMoviesUseCase,
    getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase
) : ViewModel() {
    val uiState = MutableStateFlow(HomeUiState())

    private val movies =
        getMoviesUseCase(emptyMap())
            .cachedIn(viewModelScope)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5.seconds),
                initialValue = PagingData.empty()
            )

    private val favoriteMovies: StateFlow<List<Movie>> =
        getFavoriteMoviesUseCase()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5.seconds),
                initialValue = listOf<Movie>()
            )

    init {
        favoriteMovies
            .onEach { favoriteMovies ->
                uiState.update {
                    it.copy(
                        movies = movies,
                        favoriteMovies =
                            it.favoriteMovies.apply {
                                clear()
                                addAll(favoriteMovies)
                            }
                    )
                }
            }.launchIn(viewModelScope)
    }
}
