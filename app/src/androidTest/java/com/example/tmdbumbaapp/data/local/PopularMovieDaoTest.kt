package com.example.tmdbumbaapp.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.example.tmdbumbaapp.dao.PopularMovieDao
import com.example.tmdbumbaapp.database.MovieDatabase
import com.example.tmdbumbaapp.model.popularmoviemodel.Results
import com.google.common.truth.Truth
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class PopularMovieDaoTest {
    @get:Rule
    val instantTaskTaskExecutorRule = InstantTaskExecutorRule()
    private lateinit var popularMovieDatabase: MovieDatabase
    private lateinit var popularMovieDao: PopularMovieDao

    @Before
    fun setUp(){
        popularMovieDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            MovieDatabase::class.java
        ).allowMainThreadQueries().build()
        popularMovieDao = popularMovieDatabase.getPopularMovieDao()
    }


    @After
    fun tearDown(){
        popularMovieDatabase.close()
    }

    @Test
    fun popularMovie() = runBlockingTest {
        val popularMovie = Results(true, null,1,"en",null,"welcome to tmdb movie App", 555.66,
        null,"30-032022","Batman", true,666.111, 1)
        val listOfPopularMovies = listOf(popularMovie)

        popularMovieDao.insertPopularMovie(listOfPopularMovies)
        val allPopularMovies = popularMovieDao.getPopularMovieFromDB().getOrAwaitValue()
        Truth.assertThat(allPopularMovies).isEqualTo(listOfPopularMovies)
    }

}



