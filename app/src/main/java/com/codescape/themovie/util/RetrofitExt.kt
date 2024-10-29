package com.codescape.themovie.util

import com.codescape.themovie.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

fun retrofit(init: Retrofit.Builder.() -> Unit): Retrofit {
    val retrofit = Retrofit.Builder()
    retrofit.init()
    return retrofit.build()
}

fun Retrofit.Builder.okHttpClient(init: OkHttpClient.Builder.() -> Unit): Retrofit.Builder {
    val okHttpClientBuilder = OkHttpClient.Builder()
    if (BuildConfig.DEBUG) {
        okHttpClientBuilder.addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }
        )
    }
    okHttpClientBuilder.init()
    val okHttpClient = okHttpClientBuilder.build()
    client(okHttpClient)
    return this
}
