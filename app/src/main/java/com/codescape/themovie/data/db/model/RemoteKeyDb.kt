package com.codescape.themovie.data.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "remote_key",
    indices = [
        Index("query"),
        Index("next_page")
    ]
)
data class RemoteKeyDb(
    @PrimaryKey(autoGenerate = false)
    val query: Map<String, String>,
    @ColumnInfo(name = "next_page")
    val nextPage: Int = 1
)
