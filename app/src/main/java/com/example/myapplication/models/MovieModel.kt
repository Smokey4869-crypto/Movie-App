package com.example.myapplication.models

import android.os.Parcelable
import android.os.Parcel
import kotlinx.parcelize.Parcelize

@Parcelize
//Model Class for movies
data class MovieModel (
    var title: String,
    var category: String,
    var poster_path: String,
    var release_date: String,
    var movie_id: Int,
    var vote_average: Float,
    var duration: String,
    var movie_overview: String,
    var backdrop_path: String,
    var genre_ids: List<Int>): Parcelable {
}