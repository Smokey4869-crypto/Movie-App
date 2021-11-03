package com.example.myapplication.models

import android.os.Parcelable
import android.util.Log
import com.example.myapplication.repository.TMDBRepository
import com.example.myapplication.utils.Credentials
import kotlinx.parcelize.Parcelize

@Parcelize
data class Genre (val id: Int, val name: String) : Parcelable {}