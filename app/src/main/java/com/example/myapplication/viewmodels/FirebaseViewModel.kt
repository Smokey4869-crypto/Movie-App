package com.example.myapplication.viewmodels

import android.app.Application
import android.util.Log
import android.view.animation.Transformation
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.myapplication.models.FavoriteList
import com.example.myapplication.models.MovieModel
import com.example.myapplication.repository.FirebaseRepository
import com.example.myapplication.repository.TMDBRepository


class FirebaseViewModel(app: Application): AndroidViewModel(app) {
    private val succeeded: MutableLiveData<Boolean> = MutableLiveData()

    val favoriteLists: LiveData<List<FavoriteList>> = FirebaseRepository.getAllFavoriteList()

//    val favoriteLists: LiveData<List<FavoriteList>> = Transformations
//        .switchMap(succeeded) {
//            FirebaseRepository.getAllFavoriteList()
//        }
    fun addFavoriteListWithInitialMovie(listName: String, movie: MovieModel) {
        succeeded.value = FirebaseRepository.addFavoriteListWithInitialMovie(listName, movie)
        if (!succeeded.value!!) {
        }
    }
    fun addEmptyFavoriteList(listName: String) {
        succeeded.value = FirebaseRepository.addEmptyFavoriteList(listName)
        if (!succeeded.value!!){
            Log.v("Check Firebase Query", "Add Empty: $succeeded")
        }
    }
    fun addFavoriteMovieToList(listName: String, movie: MovieModel) {
        FirebaseRepository.addFavoriteMovieToList(listName, movie)
    }
    fun deleteFavoriteList(listName: String) {
        succeeded.value = FirebaseRepository.deleteFavoriteList(listName)
        if (!succeeded.value!!){
            Log.v("Check Firebase Query", "Delete: $succeeded")
        }
    }
    fun favoriteListExists(listName: String): Boolean {
        val exist = FirebaseRepository.ifListExists(listName)
        return exist
    }

    fun checkFavorite(movie: MovieModel): LiveData<Boolean> {
        return FirebaseRepository.ifFavoriteMovieExists(movie)
    }
    fun cancelJobs() {
        FirebaseRepository.cancelJob()
    }
}