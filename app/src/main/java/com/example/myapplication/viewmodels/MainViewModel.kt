package com.example.myapplication.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.myapplication.models.MovieModel
import com.example.myapplication.repository.Repository
import com.example.myapplication.utils.Credentials

class MainViewModel: ViewModel() {
    private val key = Credentials.API_KEY
    private var page: Int = 1
    private var movieId: MutableLiveData<Int> = MutableLiveData()
    private var query: MutableLiveData<String> = MutableLiveData()

    //getting movie(s) methods
    val movies: LiveData<List<MovieModel>> = Transformations
        .switchMap(query) {
            Repository.searchMovie(key, it, page)
        }
    val movie: LiveData<MovieModel> = Transformations
        .switchMap(movieId) {
            Repository.getMovie(it, key)
        }

    //setter for mutable live data
    fun setQuery(query: String) {
        if (this.query.value == query)
            return
        this.query.value = query
    }
    fun setMovieId(movieId: Int) {
        if (this.movieId.value == movieId)
            return
        this.movieId.value = movieId
    }

    fun cancelJobs() {
        Repository.cancelJob()
    }
}