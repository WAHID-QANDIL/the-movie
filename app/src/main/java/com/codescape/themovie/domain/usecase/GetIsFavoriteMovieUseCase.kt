package com.codescape.themovie.domain.usecase

import com.codescape.themovie.domain.model.Movie
import com.codescape.themovie.domain.repository.MovieRepository
import javax.inject.Inject

class GetIsFavoriteMovieUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    operator fun invoke(movie: Movie) = movieRepository.isFavoriteMovie(movie)
}
