package com.example.myapplication.models

import android.os.Parcelable
import com.example.myapplication.repository.Repository
import com.example.myapplication.utils.Credentials
import com.example.myapplication.utils.Credentials.Companion.API_KEY
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Genre (val id: Int,
                  val name: String) : Parcelable {
    companion object {
        fun getGenre(id: Int): String? {
            val genres = Repository.getGenreList(Credentials.API_KEY)
            for (genre: Genre in genres) {
                if (genre.id == id)
                    return genre.name
            }
            return null
        }
    }
}