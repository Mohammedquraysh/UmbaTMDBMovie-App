package com.example.tmdbumbaapp.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tmdbumbaapp.model.popularmoviemodel.Results

@Dao
interface PopularMovieDao {
    // Add popular movie to the database
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertPopularMovie(popularMovieModel: List<Results>)


    //get movie from Database
    @Query("SELECT * FROM popular_movie")
    fun getPopularMovieFromDB(): LiveData<List<Results>>

    @Query("SELECT * FROM popular_movie WHERE id =:id")
    fun getPopularMovieById(id: Int) : LiveData<Results>

}


