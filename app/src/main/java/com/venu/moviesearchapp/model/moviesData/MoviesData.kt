package com.venu.moviesearchapp.model.moviesData

import com.google.gson.annotations.SerializedName

data class MoviesData(

    val Response: String,

    val Search: List<Search>,

    val totalResults: String,

    val Error: String?
)

data class Search(

    val Poster: String,

    val Title: String,

    val Type: String,

    val Year: String,

    val imdbID: String
)