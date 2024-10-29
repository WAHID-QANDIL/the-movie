package com.codescape.themovie.data.db.dao


import androidx.room.Dao
import androidx.room.Query
import com.codescape.themovie.data.db.model.RemoteKeyDb

@Dao
abstract class RemoteKeyDao : BaseDao<RemoteKeyDb>() {
    @Query(
        """
        SELECT * FROM remote_key
        WHERE `query` = :query
        """
    )
    abstract fun remoteKeyByQuery(query: Map<String, String>): RemoteKeyDb?

    @Query(
        """
        DELETE FROM remote_key
        WHERE `query` = :query
        """
    )
    abstract fun deleteByQuery(query: Map<String, String>)
}
