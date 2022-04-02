package com.example.tmdbumbaapp.network

import com.example.tmdbumbaapp.model.LatestMovieResponseModel
import com.example.tmdbumbaapp.model.moviedetails.MovieDetailsResponse
import com.example.tmdbumbaapp.model.popularmoviemodel.PopularMoviesResponseModel
import com.example.tmdbumbaapp.model.popularmoviemodel.Results
import com.example.tmdbumbaapp.model.upcomingmoviemodel.UpcomingMovieResponseModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesModuleApiInterface {
    @GET("movie/latest")
    suspend fun getLatestMovies(@Query("api_key") apiKey: String): Response<LatestMovieResponseModel>

    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): Response<PopularMoviesResponseModel>

    
    @GET("movie/upcoming")
    suspend fun getUpcomingMovies(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int
    ): Response<UpcomingMovieResponseModel>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails( @Path("movie_id")id: Int,  @Query("api_key") apiKey: String,): Response<Results>
}