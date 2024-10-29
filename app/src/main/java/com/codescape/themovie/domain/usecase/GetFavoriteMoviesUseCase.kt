package com.codescape.themovie.domain.usecase

import com.codescape.themovie.domain.repository.MovieRepository
import javax.inject.Inject

class GetFavoriteMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    operator fun invoke() = movieRepository.getFavoriteMovies()
}
