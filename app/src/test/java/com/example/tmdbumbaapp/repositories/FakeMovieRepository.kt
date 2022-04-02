package com.example.tmdbumbaapp.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.tmdbumbaapp.model.LatestMovieResponseModel
import com.example.tmdbumbaapp.model.popularmoviemodel.PopularMoviesResponseModel
import com.example.tmdbumbaapp.model.popularmoviemodel.Results
import com.example.tmdbumbaapp.model.upcomingmoviemodel.Result
import com.example.tmdbumbaapp.model.upcomingmoviemodel.UpcomingMovieResponseModel
import com.example.tmdbumbaapp.repository.MovieRepository
import com.example.tmdbumbaapp.repository.MovieRepositoryInterface
import retrofit2.Response

class FakeMovieRepository: MovieRepositoryInterface {
    private val popularMovie = mutableListOf<Results>()
    private val observablePopularMovie = MutableLiveData<List<Results>>(popularMovie)
    private var shouldReturnNetworkError = false

    fun setShouldReturnNetworkError(value: Boolean){
        shouldReturnNetworkError = value
    }

    override suspend fun getLatestMovies(apiKey: String): Response<LatestMovieResponseModel> {
        TODO("Not yet implemented")
    }

    override suspend fun getPopularMovies(
        apiKey: String,
        pageNum: Int
    ): Response<PopularMoviesResponseModel> {
        TODO("Not yet implemented")
    }

    override suspend fun getUpcomingMovies(
        apiKey: String,
        pageNum: Int
    ): Response<UpcomingMovieResponseModel> {
        TODO("Not yet implemented")
    }

    override suspend fun getMovieDetails(id: Int, apiKey: String): Response<Results> {
        TODO("Not yet implemented")
    }

    override fun getPopularMovieFromDb(): LiveData<List<Results>> {
        TODO("Not yet implemented")
    }

    override suspend fun savePopularMovieToDb(popularMovie: List<Results>) {

    }

    override fun getUpcomingMovieFromDb(): LiveData<List<Result>> {
        TODO("Not yet implemented")
    }

    override suspend fun saveUpcomingMovieToDb(upcomingMovie: List<Result>) {
        TODO("Not yet implemented")
    }

    override fun getLatestMovieFromDb(): LiveData<List<LatestMovieResponseModel>> {
        TODO("Not yet implemented")
    }

    override suspend fun saveLatestMovieToDb(latestMovie: LatestMovieResponseModel) {
        TODO("Not yet implemented")
    }
}