package com.example.tmdbumbaapp.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.tmdbumbaapp.dao.LatestMovieDao
import com.example.tmdbumbaapp.dao.PopularMovieDao
import com.example.tmdbumbaapp.database.MovieDatabase
import com.example.tmdbumbaapp.model.LatestMovieResponseModel
import com.example.tmdbumbaapp.model.popularmoviemodel.Results
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class LatestMovieDaoTest {
    @get:Rule
    val instantTaskTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var latestMovieDatabase: MovieDatabase
    private lateinit var latestMovieDao: LatestMovieDao

    @Before
    fun setUp(){
       latestMovieDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MovieDatabase::class.java
        ).allowMainThreadQueries().build()
        latestMovieDao = latestMovieDatabase.getLatestMovieDao()
    }


    @After
    fun tearDown(){
        latestMovieDatabase.close()
    }

    @Test
    fun latestMovie() = runBlockingTest {
        val latestMovie = LatestMovieResponseModel(false,null,null,1, "",1,null,
        "eng", "batMan",null,222.22,null,"02-2-2022",1 ,3,null,null,
        null,true,666.66,9)
        val latestMovies = latestMovie

        latestMovieDao.insertLatestMovie(latestMovies)
        val latestMoviesData = latestMovieDao.getLatestMovieFromDB().getOrAwaitValue()
        Truth.assertThat(latestMoviesData).contains(latestMovies)
    }
}