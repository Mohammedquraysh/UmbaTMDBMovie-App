package com.example.tmdbumbaapp.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tmdbumbaapp.model.popularmoviemodel.Results
import com.example.tmdbumbaapp.model.upcomingmoviemodel.Result

@Dao
interface UpcomingMovieDao {
    /** Add upcoming movie to the database*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUpcomingMovie(upcomingMovieModel: List<com.example.tmdbumbaapp.model.upcomingmoviemodel.Result>)


    /** get movie from Database*/
    @Query("SELECT * FROM upcoming_movie")
    fun getUpcomingMovieFromDB(): LiveData<List<com.example.tmdbumbaapp.model.upcomingmoviemodel.Result>>

    @Query("SELECT * FROM popular_movie WHERE id =:id")
    fun getUpcomingMovieById(id: Int) : LiveData<Result>

}

