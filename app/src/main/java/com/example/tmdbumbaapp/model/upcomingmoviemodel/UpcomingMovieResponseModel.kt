package com.example.tmdbumbaapp.model.upcomingmoviemodel

data class UpcomingMovieResponseModel(
    val dates: Dates,
    val page: Int,
    val results: MutableList<Result>,
    val total_pages: Int,
    val total_results: Int
)