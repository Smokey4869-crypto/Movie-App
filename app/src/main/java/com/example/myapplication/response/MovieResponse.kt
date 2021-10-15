package com.example.myapplication.response

import com.example.myapplication.models.MovieModel
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

//Class for single movie request
class MovieResponse {
    // 1- Finding the Movie object
    @SerializedName("results")
    @Expose
    lateinit var movie: MovieModel
    override fun toString(): String {
        return "MovieResponse{movie=$movie}"
    }
}