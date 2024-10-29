package com.codescape.themovie.data.db.dao

import androidx.room.Dao
import com.codescape.themovie.data.db.model.MovieGenreDb

@Dao
abstract class MovieGenreDao : BaseDao<MovieGenreDb>()
