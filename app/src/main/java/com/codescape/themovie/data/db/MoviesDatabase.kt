package com.codescape.themovie.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.codescape.themovie.data.db.converter.LocalDateConverter
import com.codescape.themovie.data.db.converter.InstantConverter
import com.codescape.themovie.data.db.converter.QueryConverter
import com.codescape.themovie.data.db.dao.FavoriteMovieDao
import com.codescape.themovie.data.db.dao.MovieDao
import com.codescape.themovie.data.db.dao.MovieGenreDao
import com.codescape.themovie.data.db.dao.RemoteKeyDao
import com.codescape.themovie.data.db.model.FavoriteMovieDb
import com.codescape.themovie.data.db.model.MovieDb
import com.codescape.themovie.data.db.model.MovieGenreDb
import com.codescape.themovie.data.db.model.RemoteKeyDb

@Database(
    entities = [
        MovieDb::class,
        MovieGenreDb::class,
        RemoteKeyDb::class,
        FavoriteMovieDb::class
    ],
    version = 2,
    exportSchema = true
)
@TypeConverters(
    LocalDateConverter::class,
    InstantConverter::class,
    QueryConverter::class
)
abstract class MoviesDatabase : RoomDatabase() {
    abstract fun getMovieDao(): MovieDao

    abstract fun getRemoteKeyDao(): RemoteKeyDao

    abstract fun getMovieGenreDao(): MovieGenreDao

    abstract fun getFavoriteMovieDao(): FavoriteMovieDao
}
