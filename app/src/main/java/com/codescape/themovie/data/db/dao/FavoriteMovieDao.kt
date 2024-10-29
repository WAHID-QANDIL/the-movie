package com.codescape.themovie.data.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.codescape.themovie.data.db.model.FavoriteMovieDb
import kotlinx.coroutines.flow.Flow

@Dao
abstract class FavoriteMovieDao : BaseDao<FavoriteMovieDb>() {
    @Query(
        """
        SELECT EXISTS(SELECT 1
                      FROM favorite_movie
                      WHERE movie_id = :id)
        """
    )
    abstract fun isFavoriteMovie(id: Int): Flow<Boolean>
}
