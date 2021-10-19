package com.example.myapplication.response

import com.example.myapplication.models.MovieModel
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

//this class is for getting multiple movies (Movies list)
class MovieSearchResponse {
    @SerializedName("total_results")
    @Expose
    var total_count: Int = 0

    @SerializedName("results")
    @Expose
    lateinit var movies: List<MovieModel>

    @SerializedName("total_pages")
    @Expose
    var total_pages: Int = 0
}