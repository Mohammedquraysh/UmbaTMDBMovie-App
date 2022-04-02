package com.example.tmdbumbaapp.dependencyinjection

import com.example.tmdbumbaapp.dao.LatestMovieDao
import com.example.tmdbumbaapp.dao.PopularMovieDao
import com.example.tmdbumbaapp.dao.UpcomingMovieDao
import com.example.tmdbumbaapp.network.MoviesModuleApiInterface
import com.example.tmdbumbaapp.repository.MovieRepository
import com.example.tmdbumbaapp.repository.MovieRepositoryInterface
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {
    @Singleton
    @Provides
    fun provideMoviesRepository(moviesModuleApiInterface: MoviesModuleApiInterface, popularMoviesDao: PopularMovieDao, upcomingMovieDao: UpcomingMovieDao,latestMovieDao: LatestMovieDao ): MovieRepositoryInterface {
        return MovieRepository(moviesModuleApiInterface,popularMoviesDao, upcomingMovieDao, latestMovieDao)

    }

}