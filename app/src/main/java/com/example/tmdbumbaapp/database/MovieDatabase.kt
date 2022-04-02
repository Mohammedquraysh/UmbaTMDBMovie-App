package com.example.tmdbumbaapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.tmdbumbaapp.dao.LatestMovieDao
import com.example.tmdbumbaapp.dao.PopularMovieDao
import com.example.tmdbumbaapp.dao.UpcomingMovieDao
import com.example.tmdbumbaapp.model.LatestMovieResponseModel
import com.example.tmdbumbaapp.model.popularmoviemodel.Results
import com.example.tmdbumbaapp.model.upcomingmoviemodel.Result


@Database(entities = [Results::class, Result::class, LatestMovieResponseModel::class], version = 1, exportSchema = false)
abstract class MovieDatabase:RoomDatabase() {
    abstract fun getPopularMovieDao(): PopularMovieDao
    abstract fun getUpcomingMovieDao(): UpcomingMovieDao
    abstract fun getLatestMovieDao(): LatestMovieDao




    //Creating a single instance of the database
    companion object {

        @Volatile
        private var INSTANCE: MovieDatabase? = null

        fun getDatabase(context: Context): MovieDatabase{

            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java,
                    "orda_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}