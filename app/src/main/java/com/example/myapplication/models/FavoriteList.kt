package com.example.myapplication.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class FavoriteList(
    var listName: String,
    var movieNum: Long,
    var movies: List<MovieModel>?,
    var chosen: Boolean = false
): Parcelable {
}