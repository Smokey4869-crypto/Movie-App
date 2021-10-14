package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.adapter.MovieAdapter
import com.example.myapplication.models.MovieModel
import com.example.myapplication.viewmodels.MainViewModel

class MainActivity : AppCompatActivity() {
    private val movieListViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val vMovieList = findViewById<RecyclerView>(R.id.recycler_view)
        val movieList: List<MovieModel>? = movieListViewModel.movies.value
        val movieAdapter: MovieAdapter? = movieList?.let { MovieAdapter(it){} }

        movieListViewModel.movies.observe(this, Observer {
            for (item: MovieModel in it) {
                Log.v("Test", item.title)
            }
        })
        movieListViewModel.movie.observe(this, Observer {
            Log.v("Test", it.title)
        })

        movieListViewModel.setMovieId(550)

        val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(this)
        //vMovieList.adapter = movieAdapter
        vMovieList.layoutManager = linearLayoutManager

    }

    override fun onDestroy() {
        super.onDestroy()
        movieListViewModel.cancelJobs()
    }
}