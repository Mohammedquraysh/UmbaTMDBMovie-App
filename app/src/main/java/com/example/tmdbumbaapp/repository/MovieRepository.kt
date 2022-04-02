package com.example.tmdbumbaapp.repository

import androidx.lifecycle.LiveData
import com.example.tmdbumbaapp.dao.LatestMovieDao
import com.example.tmdbumbaapp.dao.PopularMovieDao
import com.example.tmdbumbaapp.dao.UpcomingMovieDao
import com.example.tmdbumbaapp.model.LatestMovieResponseModel
import com.example.tmdbumbaapp.model.moviedetails.MovieDetailsResponse
import com.example.tmdbumbaapp.model.popularmoviemodel.PopularMoviesResponseModel
import com.example.tmdbumbaapp.model.popularmoviemodel.Results
import com.example.tmdbumbaapp.model.upcomingmoviemodel.Result
import com.example.tmdbumbaapp.model.upcomingmoviemodel.UpcomingMovieResponseModel
import com.example.tmdbumbaapp.network.MoviesModuleApiInterface
import retrofit2.Response
import java.security.PrivateKey
import javax.inject.Inject

class MovieRepository @Inject constructor(private val moviesModuleApiInterface: MoviesModuleApiInterface,
                                          private val popularMovieDao: PopularMovieDao,
                                          private val upcomingMovieDao: UpcomingMovieDao,
                                          private val latestMovieDao: LatestMovieDao
                                          ):MovieRepositoryInterface {
    override suspend fun getLatestMovies(apiKey: String): Response<LatestMovieResponseModel> {
        return moviesModuleApiInterface.getLatestMovies(apiKey)
    }

    override suspend fun getPopularMovies(apiKey: String,pageNum: Int): Response<PopularMoviesResponseModel> {
        return moviesModuleApiInterface.getPopularMovies(apiKey, pageNum)
    }

    override suspend fun getUpcomingMovies(apiKey: String,
                                           pageNum: Int): Response<UpcomingMovieResponseModel> {
        return moviesModuleApiInterface.getUpcomingMovies(apiKey,pageNum)
    }

    override suspend fun getMovieDetails(id: Int, apiKey: String): Response<Results> {
        return moviesModuleApiInterface.getMovieDetails(id,apiKey)
    }

    override fun getPopularMovieFromDb(): LiveData<List<Results>> {
        return popularMovieDao.getPopularMovieFromDB()
    }

    override suspend fun savePopularMovieToDb(popularMovie: List<Results>) {
        return popularMovieDao.insertPopularMovie(popularMovie)
    }

    override fun getUpcomingMovieFromDb(): LiveData<List<com.example.tmdbumbaapp.model.upcomingmoviemodel.Result>> {
        return upcomingMovieDao.getUpcomingMovieFromDB()
    }

    override suspend fun saveUpcomingMovieToDb(upcomingMovie: List<Result>) {
        return upcomingMovieDao.insertUpcomingMovie(upcomingMovie)
    }

    override fun getLatestMovieFromDb(): LiveData<List<LatestMovieResponseModel>> {
        return latestMovieDao.getLatestMovieFromDB()
    }

    override suspend fun saveLatestMovieToDb(latestMovie: LatestMovieResponseModel) {
        return latestMovieDao.insertLatestMovie(latestMovie)
    }
}