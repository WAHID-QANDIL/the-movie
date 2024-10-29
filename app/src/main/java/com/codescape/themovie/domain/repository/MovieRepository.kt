package com.codescape.themovie.domain.repository

import androidx.paging.PagingData
import com.codescape.themovie.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieRepository {
    fun getMovies(query: Map<String, String>): Flow<PagingData<Movie>>

    fun getFavoriteMovies(): Flow<List<Movie>>

    fun searchMovies(query: String): Flow<List<Movie>>

    suspend fun saveFavoriteMovie(movie: Movie)

    suspend fun deleteFavoriteMovie(movie: Movie)

    fun isFavoriteMovie(movie: Movie): Flow<Boolean>
}
