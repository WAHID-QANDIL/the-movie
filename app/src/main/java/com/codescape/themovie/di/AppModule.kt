package com.codescape.themovie.di

import android.content.Context
import com.codescape.themovie.BuildConfig
import com.codescape.themovie.data.db.MoviesDatabase
import com.codescape.themovie.util.okHttpClient
import com.codescape.themovie.util.retrofit
import com.codescape.themovie.util.roomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

const val API_TIMEOUT = 10L

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun provideJson(): Json =
        Json {
            ignoreUnknownKeys = true
            encodeDefaults = false
            explicitNulls = false
            coerceInputValues = true
        }

    @Provides
    @Singleton
    fun provideRetrofit(json: Json) =
        retrofit {
            baseUrl(BuildConfig.API_URL)
            okHttpClient {
                connectTimeout(API_TIMEOUT, TimeUnit.SECONDS)
                readTimeout(API_TIMEOUT, TimeUnit.SECONDS)
                writeTimeout(API_TIMEOUT, TimeUnit.SECONDS)
                addInterceptor { chain ->
                    val originalRequest = chain.request()
                    val newRequest =
                        originalRequest
                            .newBuilder()
                            .header("Authorization", "Bearer ${BuildConfig.API_KEY}")
                            .build()
                    chain.proceed(newRequest)
                }
            }
            addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        }

    @Provides
    @Singleton
    fun provideMoviesDatabase(
        @ApplicationContext context: Context
    ) = roomDatabase<MoviesDatabase>(
        context = context,
        name = "movies"
    ) {
        fallbackToDestructiveMigration()
    }
}
