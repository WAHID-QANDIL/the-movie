package com.codescape.themovie.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "favorite_movie",
    indices = [
        Index("movie_id")
    ],
    primaryKeys = ["movie_id" ]
)
data class FavoriteMovieDb(
    @ColumnInfo(name = "movie_id")
    val movieId: Int
)
