package com.example.myapplication.models

import android.os.Parcelable
import android.util.Log
import com.example.myapplication.repository.TMDBRepository
import com.example.myapplication.utils.Credentials

data class Genre (val id: Int, val name: String)
{
    companion object {
        fun getGenre(id: Int): String? {
            val genres: List<Genre> = TMDBRepository.getGenreList(Credentials.API_KEY)
            for (genre: Genre in genres) {
                Log.v("Genre ID", genre.id.toString())
                if (id == genre.id) { return genre.name }
            }
            return null
        }
    }
}