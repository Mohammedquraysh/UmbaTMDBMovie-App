package com.example.tmdbumbaapp.dependencyinjection

import android.app.Application
import com.example.tmdbumbaapp.dao.LatestMovieDao
import com.example.tmdbumbaapp.dao.PopularMovieDao
import com.example.tmdbumbaapp.dao.UpcomingMovieDao
import com.example.tmdbumbaapp.database.MovieDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MovieDatabaseModule {

    @Singleton
    @Provides
    fun provideDatabaseInstance(context: Application): MovieDatabase{
        return MovieDatabase.getDatabase(context)
    }


    @Singleton
    @Provides
    fun providePopularMovieDao(movieDatabase: MovieDatabase): PopularMovieDao{
        return movieDatabase.getPopularMovieDao()
    }


    @Singleton
    @Provides
    fun provideUpcomingMovieDao(movieDatabase: MovieDatabase): UpcomingMovieDao{
        return movieDatabase.getUpcomingMovieDao()
    }

    @Singleton
    @Provides
    fun provideLatestMovieDao(movieDatabase: MovieDatabase): LatestMovieDao {
        return movieDatabase.getLatestMovieDao()
    }


}
