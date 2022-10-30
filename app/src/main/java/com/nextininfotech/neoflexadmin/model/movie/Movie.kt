package com.nextininfotech.neoflexadmin.model.movie

data class Movie(
    val dates: Dates,
    val page: Int,
    val results: ArrayList<Result>,
    val total_pages: Int,
    val total_results: Int
)