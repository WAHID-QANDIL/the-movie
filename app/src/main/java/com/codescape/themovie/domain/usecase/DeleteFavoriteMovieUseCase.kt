package com.codescape.themovie.domain.usecase

import com.codescape.themovie.domain.model.Movie
import com.codescape.themovie.domain.repository.MovieRepository
import javax.inject.Inject

class DeleteFavoriteMovieUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(movie: Movie) = movieRepository.deleteFavoriteMovie(movie)
}
