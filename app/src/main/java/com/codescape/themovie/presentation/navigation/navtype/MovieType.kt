package com.codescape.themovie.presentation.navigation.navtype

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import com.codescape.themovie.domain.model.Movie
import com.codescape.themovie.util.parcelable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

val NavType.Companion.MovieType: NavType<Movie>
    get() =
        object : NavType<Movie>(isNullableAllowed = false) {
            override fun get(
                bundle: Bundle,
                key: String
            ) = bundle.parcelable<Movie>(key)

            override fun parseValue(value: String): Movie = Json.decodeFromString(value)

            override fun serializeAsValue(value: Movie): String = Uri.encode(Json.encodeToString(value))

            override fun put(
                bundle: Bundle,
                key: String,
                value: Movie
            ) = bundle.putParcelable(key, value)
        }
