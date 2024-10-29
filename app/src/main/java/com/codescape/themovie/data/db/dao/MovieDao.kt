package com.codescape.themovie.data.db.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.codescape.themovie.data.db.model.MovieDb
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.Instant

@Dao
abstract class MovieDao : BaseDao<MovieDb>() {
    @Transaction
    @Query(
        """
        SELECT *
        FROM movie
        WHERE `query` = :query
        """
    )
    abstract fun getMovies(query: Map<String, String>): PagingSource<Int, MovieDb>

    @Query(
        """
        SELECT *
        FROM movie
                 LEFT JOIN favorite_movie ON movie.id = favorite_movie.movie_id
        WHERE favorite_movie.movie_id IS NOT NULL
        """
    )
    abstract fun getFavoriteMovies(): Flow<List<MovieDb>>

    @Query(
        """
        SELECT *
        FROM movie
        WHERE title LIKE '%' || :query || '%'
        """
    )
    abstract fun searchByTitle(query: String): Flow<List<MovieDb>>


    @Query(
        """
        SELECT updated
        FROM movie
        ORDER BY updated ASC
        LIMIT 1
        """
    )
    abstract suspend fun lastUpdated(): Instant?

    @Query(
        """
        DELETE
        FROM movie
        WHERE `query` = :query
        """
    )
    abstract suspend fun deleteByQuery(query: Map<String, String>)
}
