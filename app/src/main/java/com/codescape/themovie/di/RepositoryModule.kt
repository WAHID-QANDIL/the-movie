package com.codescape.themovie.di

import com.codescape.themovie.data.repository.MovieRepositoryMediator
import com.codescape.themovie.domain.repository.MovieRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindMovieRepositoryMediator(movieRepositoryMediator: MovieRepositoryMediator): MovieRepository
}
