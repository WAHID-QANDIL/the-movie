package com.codescape.themovie.data.db.converter

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate

class LocalDateConverter {
    @TypeConverter
    fun stringToLocalDate(value: String?) = value?.let { LocalDate.parse(it) }

    @TypeConverter
    fun localDateToString(value: LocalDate?) = value?.toString()
}
