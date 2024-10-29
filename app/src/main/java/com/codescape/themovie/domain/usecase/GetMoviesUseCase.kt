package com.codescape.themovie.domain.usecase

import com.codescape.themovie.domain.repository.MovieRepository
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    operator fun invoke(query: Map<String, String>) = movieRepository.getMovies(query = query)
}
