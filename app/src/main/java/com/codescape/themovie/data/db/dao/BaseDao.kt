package com.codescape.themovie.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Update
import androidx.room.Upsert

@Dao
abstract class BaseDao<T> {
    @Update
    abstract suspend fun update(item: T)

    @Update
    abstract suspend fun update(items: List<T>)

    @Delete
    abstract suspend fun delete(item: T)

    @Upsert
    abstract suspend fun upsert(item: T)

    @Upsert
    abstract suspend fun upsert(items: List<T>)
}
