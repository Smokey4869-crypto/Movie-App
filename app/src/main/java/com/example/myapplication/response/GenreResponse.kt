package com.example.myapplication.response

import com.example.myapplication.models.Genre
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GenreResponse {
    @SerializedName("genres")
    @Expose
    lateinit var genres: List<Genre>
}