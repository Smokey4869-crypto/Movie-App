package com.example.myapplication.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.api.MyRetrofitBuilder
import com.example.myapplication.models.MovieModel
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import java.io.IOException

object Repository {
    var job: CompletableJob? = null

    fun getMovie(movieId: Int, key: String): LiveData<MovieModel>{
        job = Job()
        return object: LiveData<MovieModel>() {
            override fun onActive() {
                super.onActive()
                job?.let { theJob ->
                    CoroutineScope(IO + theJob).launch {
                        val movieResponse = MyRetrofitBuilder.apiService.getMovie(movieId, key)
                        withContext(Main) {
                            if (movieResponse.isSuccessful) {
                                value = movieResponse.body()?.movie
                                Log.v("Response", "The call is successful")
                            } else {
                                try {
                                    Log.v("Tag", "Error ${movieResponse.errorBody()}")
                                } catch (e: IOException) {
                                    e.printStackTrace()
                                }
                            }
                            theJob.complete()
                        }
                    }
                }
            }
        }
    }

    fun searchMovie(key: String, query: String, page: Int): MutableLiveData<List<MovieModel>> {
        job = Job()
        return object: MutableLiveData<List<MovieModel>>() {
            override fun onActive() {
                super.onActive()
                job?.let { theJob ->
                    CoroutineScope(IO + theJob).launch {
                        val movieListResponse = MyRetrofitBuilder.apiService.searchMovie(key, query, page)
                        withContext(Main) {
                            if (movieListResponse.isSuccessful) {
                                value = movieListResponse.body()?.movies
                                Log.v("Response", "The call is successful")
                            } else {
                                try {
                                    Log.v("Tag", "Error ${movieListResponse.errorBody()}")
                                } catch (e: IOException) {
                                    e.printStackTrace()
                                }
                            }
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