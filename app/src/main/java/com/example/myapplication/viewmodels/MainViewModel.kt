package com.example.myapplication.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.example.myapplication.models.MovieModel
import com.example.myapplication.repository.Repository
import com.example.myapplication.utils.Credentials

class MainViewModel: ViewModel() {
    private val key = Credentials.API_KEY
    private var movieId: MutableLiveData<Int> = MutableLiveData()
    private var query: MutableLiveData<String> = MutableLiveData()
    private var page: MutableLiveData<Int> = MutableLiveData()

    private var liveMovieQuery = PairMediatorLiveData(page, query)

    init {
        page.value = 1
    }
    //getting movie(s) methods
    val movies: LiveData<List<MovieModel>> = Transformations
        .switchMap(liveMovieQuery) {
            it.second?.let { it1 -> it.first?.let { it2 -> Repository.searchMovie(key, it1, it2) } }
        }
    val movie: LiveData<MovieModel> = Transformations
        .switchMap(movieId) {
            Repository.getMovie(it, key)
        }

    fun setMovieId(movieId: Int) {
        if (this.movieId.value == movieId)
            return
        this.movieId.value = movieId
    }

    fun setQuery(newQuery: String) {
        query.value = newQuery
        Log.v("Test Query", query.value.toString())
    }
    fun searchNextPage() {
        page.value = page.value?.plus(1)
        Log.v("Test Page", page.value.toString())
    }

    fun cancelJobs() {
        Repository.cancelJob()
    }

    inner class PairMediatorLiveData<Int, String>( pageNumber: LiveData<Int>, liveQuery: LiveData<String>) : MediatorLiveData<Pair<Int?, String?>>() {
        init {
            addSource(pageNumber) { firstLiveDataValue: Int -> value = firstLiveDataValue to liveQuery.value }
            addSource(liveQuery) { secondLiveDataValue: String -> value = pageNumber.value to secondLiveDataValue }
        }
    }
}