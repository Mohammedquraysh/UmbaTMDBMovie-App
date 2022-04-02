package com.example.tmdbumbaapp.model.popularmoviemodel

data class PopularMoviesResponseModel(
    val page: Int,
    val results: MutableList<Results>,
    val total_pages: Int,
    val total_results: Int
)