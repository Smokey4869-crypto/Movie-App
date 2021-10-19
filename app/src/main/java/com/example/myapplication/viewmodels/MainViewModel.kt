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
    private var total_pages: Int = Repository.total_pages
    private var liveMovieQuery = PairMediatorLiveData(page, query)

    init {
        page.value = 1
    }
    val popularMovies: LiveData<List<MovieModel>> = Transformations
        .switchMap(page) {
            page.value?.let { it1 -> Repository.getPopularMovie(key, it1) }
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
        page.value?.let {
            if (page.value!!.toInt() < total_pages) {
                page.value = page.value?.plus(1)
                Log.v("Test Page", "We are in " + page.value.toString())
            } else {}
        }
    }
    fun searchLastPage() {
        page.value?.let {
            if (page.value!! > 1) {
                page.value = page.value?.minus(1)
                Log.v("Test Page", "We are in " + page.value.toString())
            }
        }
    }
    fun setPage(pageNum: Int) {
        page.value = 1
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