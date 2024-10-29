package com.codescape.themovie.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.codescape.themovie.data.api.service.MovieApiService
import com.codescape.themovie.data.db.MoviesDatabase
import com.codescape.themovie.data.db.model.FavoriteMovieDb
import com.codescape.themovie.data.mediator.MovieRemoteMediator
import com.codescape.themovie.domain.model.Movie
import com.codescape.themovie.domain.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
class MovieRepositoryMediator @Inject constructor(
    private val database: MoviesDatabase,
    private val apiService: MovieApiService
) : MovieRepository {
    override fun getMovies(query: Map<String, String>): Flow<PagingData<Movie>> =
        Pager(
            config = PagingConfig(pageSize = 2),
            remoteMediator =
                MovieRemoteMediator(
                    query = query,
                    database = database,
                    apiService = apiService,
                    cacheTimeout = TimeUnit.HOURS.convert(1, TimeUnit.MILLISECONDS)
                )
        ) {
            database.getMovieDao().getMovies(query = query)
        }.flow.map { pagingData ->
            pagingData.map { movieDb ->
                movieDb.toDomain()
            }
        }

    override fun getFavoriteMovies(): Flow<List<Movie>> =
        database.getMovieDao().getFavoriteMovies().map { movieList ->
            movieList.map { movie ->
                movie.toDomain()
            }
        }

    override fun searchMovies(query: String): Flow<List<Movie>> =
        database.getMovieDao().searchByTitle(query = query).map { movieList ->
            movieList.map { movie ->
                movie.toDomain()
            }
        }

    override suspend fun saveFavoriteMovie(movie: Movie) {
        database.getFavoriteMovieDao().upsert(FavoriteMovieDb(movieId = movie.id))
    }

    override suspend fun deleteFavoriteMovie(movie: Movie) {
        database.getFavoriteMovieDao().delete(FavoriteMovieDb(movieId = movie.id))
    }

    override fun isFavoriteMovie(movie: Movie): Flow<Boolean> = database.getFavoriteMovieDao().isFavoriteMovie(id = movie.id)
}
