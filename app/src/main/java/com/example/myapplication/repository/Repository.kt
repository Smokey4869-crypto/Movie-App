package com.example.myapplication.repository

import androidx.lifecycle.LiveData
import com.example.myapplication.api.MyRetrofitBuilder
import com.example.myapplication.models.MovieModel
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

object Repository {
    var job: CompletableJob? = null

    fun getMovie(movieId: Int, key: String): LiveData<MovieModel>{
        job = Job()
        return object: LiveData<MovieModel>() {
            override fun onActive() {
                super.onActive()
                job?.let { theJob ->
                    CoroutineScope(IO + theJob).launch {
                        val movie = MyRetrofitBuilder.apiService.getMovie(movieId, key)
                        withContext(Main) {
                            value = movie
                            theJob.complete()
                        }
                    }
                }
            }
        }
    }

    fun searchMovie(key: String, query: String, page: Int): LiveData<List<MovieModel>> {
        job = Job()
        return object: LiveData<List<MovieModel>>() {
            override fun onActive() {
                super.onActive()
                job?.let { theJob ->
                    CoroutineScope(IO + theJob).launch {
                        val movies = MyRetrofitBuilder.apiService.searchMovie(key, query, page)
                        withContext(Main) {
                            value = movies
                            theJob.complete()
                        }
                    }
                }
            }
        }
    }
    fun cancelJob() {
        job?.cancel()
    }
}