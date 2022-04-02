package com.example.tmdbumbaapp.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.tmdbumbaapp.model.LatestMovieResponseModel

@Dao
interface LatestMovieDao {
    /** Add latest movie to the database */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertLatestMovie(latestMovieModel: LatestMovieResponseModel)


    /** get movie from Database */
    @Query("SELECT * FROM latest_movie")
    fun getLatestMovieFromDB(): LiveData<List<LatestMovieResponseModel>>

    @Query("SELECT * FROM latest_movie WHERE id =:id")
    fun getLatestMovieById(id: Int) : LiveData<LatestMovieResponseModel>

}