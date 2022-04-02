package com.example.tmdbumbaapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tmdbumbaapp.apiKey
import com.example.tmdbumbaapp.model.LatestMovieResponseModel
import com.example.tmdbumbaapp.model.moviedetails.MovieDetailsResponse
import com.example.tmdbumbaapp.model.popularmoviemodel.Results
import com.example.tmdbumbaapp.model.popularmoviemodel.PopularMoviesResponseModel
import com.example.tmdbumbaapp.model.upcomingmoviemodel.Result
import com.example.tmdbumbaapp.model.upcomingmoviemodel.UpcomingMovieResponseModel
import com.example.tmdbumbaapp.repository.MovieRepository
import com.example.tmdbumbaapp.repository.Resource
import com.example.tmdbumbaapp.util.ApiCallNetworkResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MoviesViewModel @Inject constructor(private val moviesRepository: MovieRepository): ViewModel() {


    /** latest movie liveData*/
    private val _latestMovieLiveData: MutableLiveData<Resource<LatestMovieResponseModel>> = MutableLiveData()
    val latestMovieLiveData: LiveData<Resource<LatestMovieResponseModel>> = _latestMovieLiveData



    /**Handling Errors for latest movie*/
    fun latestMovie(apiKey: String) {
        viewModelScope.launch {
            _latestMovieLiveData.postValue(Resource.Loading())
            try {
                delay(1)
                val response = moviesRepository.getLatestMovies(apiKey)
                if (response.isSuccessful) {
                    response.body().let {
                        if (it != null) moviesRepository.saveLatestMovieToDb(it)
                        _latestMovieLiveData.postValue(Resource.Success(response.body()!!))
                    }
//                    _latestMovieLiveData.postValue(Resource.Success(response.body()!!))
                }else
                {
                    _latestMovieLiveData.postValue(Resource.Error(response.body()!!.toString()))
                }

            } catch (e: Throwable) {
                e.printStackTrace()
                when(e){
                    is IOException ->{
                        _latestMovieLiveData.postValue(Resource.Error( message =
                        "Network Failure, please check your internet connection"
                        ))
                    }
                    is NullPointerException ->{
                        _latestMovieLiveData.postValue(Resource.Error(
                            "No information to display"
                        ))
                    }
                    else->{
                        _latestMovieLiveData.postValue(Resource.Error(message =
                        "an error occur please try again later"))
                    }
                }
            }
        }
    }



    //movie details Livedata
    private val _movieDetailLiveData: MutableLiveData<Resource<Results>> = MutableLiveData()
    val movieDetailLiveData : LiveData<Resource<Results>> = _movieDetailLiveData

    fun movieDetail(id: Int,apiKey: String){
        viewModelScope.launch {
            try {
                val response = moviesRepository.getMovieDetails(id,apiKey)
                if (response.isSuccessful){
                    _movieDetailLiveData.postValue(Resource.Success(response.body()))
                }else{
                    _movieDetailLiveData.postValue(Resource.Error(response.body()!!.toString()))
                }
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }



    /** save latest movie to Database */
    fun saveLatestMovieToDb(latestMovie: LatestMovieResponseModel){
        viewModelScope.launch {
            moviesRepository.saveLatestMovieToDb(latestMovie)
        }
    }

    /** get latest movies from the  Database*/
    fun getLatestMovieLiveDataDb():  LiveData<List<LatestMovieResponseModel>> = moviesRepository.getLatestMovieFromDb()


    /** save popular movies to Database */
    fun savePopularMovieToDb(popularMovie: List<Results>){
        viewModelScope.launch {
            moviesRepository.savePopularMovieToDb(popularMovie)
        }
    }

    /** get popular movies from the  Database*/
    fun getPopularMovieLiveDataDb():  LiveData<List<Results>> = moviesRepository.getPopularMovieFromDb()




    /** save upcoming movies to Database*/
    fun saveUpcomingMovieToDb(upcomingMovie: List<Result>){
        viewModelScope.launch {
            moviesRepository.saveUpcomingMovieToDb(upcomingMovie)
        }
    }

    /** get upcoming movies from the  Database*/
    fun getUpcomingMovieFromDb() = moviesRepository.getUpcomingMovieFromDb()




    /** popular movies livedata */
    private val _popularMovieLiveData: MutableLiveData<Resource<PopularMoviesResponseModel>> = MutableLiveData()
    val popularMovieLiveData: LiveData<Resource<PopularMoviesResponseModel>> = _popularMovieLiveData

    var pageNumber = 1
    var popularMovieResponse: PopularMoviesResponseModel? = null

    init {
        getPopMovies(apiKey)
    }


    fun getPopMovies(apiKey: String) =
        viewModelScope.launch {
            _popularMovieLiveData.postValue(Resource.Loading())
            val response = moviesRepository.getPopularMovies(apiKey, pageNumber)
            _popularMovieLiveData.postValue(handlePopularMovieResponse(response))
        }

    /** handle popular movie response */
    private fun handlePopularMovieResponse(response: Response<PopularMoviesResponseModel>):Resource<PopularMoviesResponseModel>{
        if (response.isSuccessful){
            response.body()?.let {
                 pageNumber++
                if (popularMovieResponse == null){
                    popularMovieResponse = it
                }else{
                    val oldPopularMovies = popularMovieResponse?.results
                    val newPopularMovies = it.results
                   oldPopularMovies?.addAll(newPopularMovies)
                    savePopularMovieToDb(it.results)

                }
                return Resource.Success(popularMovieResponse ?: it)

            }
        }
        return Resource.Error(response.message())
    }




    /** upcoming movies livedata */
    private val _upcomingMovieLiveData: MutableLiveData<Resource<UpcomingMovieResponseModel>> = MutableLiveData()
    val upcomingMovieLiveData: LiveData<Resource<UpcomingMovieResponseModel>> = _upcomingMovieLiveData

    var pageNum = 1
    var upcomingMovieResponse: UpcomingMovieResponseModel? = null

    init {
        getUpcomingMovies(apiKey)
    }


    fun getUpcomingMovies(apiKey: String) =
        viewModelScope.launch {
            _upcomingMovieLiveData.postValue(Resource.Loading())
            val response = moviesRepository.getUpcomingMovies(apiKey, pageNumber)
            _upcomingMovieLiveData.postValue(handleUpcomingMovieResponse(response))

        }


    /** handle upcoming movie response */
    private fun handleUpcomingMovieResponse(response: Response<UpcomingMovieResponseModel>):Resource<UpcomingMovieResponseModel>{
        if (response.isSuccessful){
            response.body()?.let {
                pageNum++
                if (upcomingMovieResponse == null){
                    upcomingMovieResponse = it
                }else{
                    val oldUpcomingMovies = upcomingMovieResponse?.results
                    val newUpcomingMovies = it.results
                    oldUpcomingMovies?.addAll(newUpcomingMovies)
                 //   saveUpcomingMovieToDb(it.results)

                }
                return Resource.Success(upcomingMovieResponse ?: it)
            }
        }
        return Resource.Error(response.message())
    }




}