package com.example.tmdbumbaapp.repository

import androidx.lifecycle.LiveData
import com.example.tmdbumbaapp.model.LatestMovieResponseModel
import com.example.tmdbumbaapp.model.moviedetails.MovieDetailsResponse
import com.example.tmdbumbaapp.model.popularmoviemodel.Results
import com.example.tmdbumbaapp.model.popularmoviemodel.PopularMoviesResponseModel
import com.example.tmdbumbaapp.model.upcomingmoviemodel.Result
import com.example.tmdbumbaapp.model.upcomingmoviemodel.UpcomingMovieResponseModel
import retrofit2.Response

interface MovieRepositoryInterface {

    suspend fun getLatestMovies(apiKey: String) : Response<LatestMovieResponseModel>
    suspend fun getPopularMovies(apiKey: String,pageNum: Int): Response<PopularMoviesResponseModel>
    suspend fun getUpcomingMovies(apiKey: String, pageNum: Int): Response<UpcomingMovieResponseModel>
    suspend fun getMovieDetails(id: Int,apiKey: String): Response<Results>
    fun getPopularMovieFromDb(): LiveData<List<Results>>
    suspend fun savePopularMovieToDb(popularMovie: List<Results>)
    fun getUpcomingMovieFromDb(): LiveData<List<com.example.tmdbumbaapp.model.upcomingmoviemodel.Result>>
    suspend fun saveUpcomingMovieToDb(upcomingMovie: List<Result>)
    fun getLatestMovieFromDb(): LiveData<List<LatestMovieResponseModel>>
    suspend fun saveLatestMovieToDb(latestMovie: LatestMovieResponseModel)


}