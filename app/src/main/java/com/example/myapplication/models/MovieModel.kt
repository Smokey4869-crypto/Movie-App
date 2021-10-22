package com.example.myapplication.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieModel (
    var title: String,
    var poster_path: String?,
    var release_date: String,
    var id: Int,
    var vote_average: Float,
    var overview: String,
    var backdrop_path: String?,
    var genre_ids: List<Int>): Parcelable {

    //default constructor to use Realtime Firebase DB
    constructor(): this("","", "", 0, 0.0.toFloat(), "", "", listOf(1))
}