package com.codescape.themovie.data.db.converter

import androidx.room.TypeConverter
import kotlinx.serialization.json.Json

class QueryConverter {
    @TypeConverter
    fun stringToQuery(value: String?) =
        value?.let {
            Json.decodeFromString<Map<String, String>>(it)
        }

    @TypeConverter
    fun queryToString(value: Map<String, String>?) = value?.toString()
}
