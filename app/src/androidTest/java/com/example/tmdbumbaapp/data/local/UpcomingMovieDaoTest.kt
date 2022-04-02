package com.example.tmdbumbaapp.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.tmdbumbaapp.dao.PopularMovieDao
import com.example.tmdbumbaapp.dao.UpcomingMovieDao
import com.example.tmdbumbaapp.database.MovieDatabase
import com.example.tmdbumbaapp.model.popularmoviemodel.Results
import com.example.tmdbumbaapp.model.upcomingmoviemodel.Result
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
class UpcomingMovieDaoTest {
    @get:Rule
    val instantTaskTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var upcomingMovieDatabase: MovieDatabase
    private lateinit var upcomingMovieDao: UpcomingMovieDao

    @Before
    fun setItUp(){
        upcomingMovieDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MovieDatabase::class.java
        ).allowMainThreadQueries().build()
        upcomingMovieDao = upcomingMovieDatabase.getUpcomingMovieDao()
    }


    @After
    fun tearDown(){
        upcomingMovieDatabase.close()
    }

    @Test
    fun upcomingMovie() = runBlockingTest {
        val upcomingMovie = Result(true, null,1,"en",null,"welcome to tmdb movie App", 555.66,
            null,"30-032022","Batman", true,666.111, 1)

        val listOfUpcomingMovies = listOf(upcomingMovie)

        upcomingMovieDao.insertUpcomingMovie(listOfUpcomingMovies)
        val allUpcomingMovies = upcomingMovieDao.getUpcomingMovieFromDB().getOrAwaitValue()
        Truth.assertThat(allUpcomingMovies).isEqualTo(listOfUpcomingMovies)
    }

}