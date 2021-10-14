package com.example.myapplication.api

import com.example.myapplication.models.MovieModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("search/movie")
    suspend fun searchMovie(
        @Query("api_key") key: String,
        @Query("query") query: String,
        @Query("page") page: Int
    ): List<MovieModel>

    //Search with ID
    //https://api.themoviedb.org/3/movie/550?api_key={api_key}
    @GET("movie/{movie_id}?")
    suspend fun getMovie(
        @Path("movie_id") movie_id: Int,
        @Query("api_key") api_key: String
    ): MovieModel
}