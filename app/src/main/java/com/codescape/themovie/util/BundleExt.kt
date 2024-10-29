package com.codescape.themovie.util

import android.os.Build.VERSION.SDK_INT
import android.os.Build.VERSION_CODES.TIRAMISU
import android.os.Bundle

@Suppress("DEPRECATION")
inline fun <reified T> Bundle.parcelable(key: String) =
    if (SDK_INT < TIRAMISU) {
        getParcelable(key)
    } else {
        getParcelable(key, T::class.java)
    }
