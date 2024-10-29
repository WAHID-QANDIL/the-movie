package com.codescape.themovie.di

import com.codescape.themovie.data.api.service.MovieApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create

@Module
@InstallIn(SingletonComponent::class)
object ApiServiceModule {
    @Provides
    fun createMovieApiService(retrofit: Retrofit): MovieApiService =
        retrofit
            .create<MovieApiService>()
}
