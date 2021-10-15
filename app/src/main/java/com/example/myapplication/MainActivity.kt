package com.example.myapplication

import android.content.Intent
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
    private lateinit var vMovieList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vMovieList = findViewById<RecyclerView>(R.id.recycler_view)
        var movieList: List<MovieModel> = listOf()
        val movieAdapter = MovieAdapter(movieList) {}
        val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(this)
        vMovieList.adapter = movieAdapter
        vMovieList.layoutManager = linearLayoutManager

        movieListViewModel.setQuery("Jack")
        movieListViewModel.movies.observe(this,
            { t ->
                if (t != null) {
                    movieAdapter.setMovieList(t)
                }
            })


        movieListViewModel.movies.observe(this, Observer {
            for (item: MovieModel in it)
                Log.v("Test Call", item.title )
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        movieListViewModel.cancelJobs()
    }

    //methods
    private fun observeLiveData() {
        movieListViewModel.movie.observe(this, Observer {
            Log.v("Test Call", it.title)
        })
    }
    private fun showDetail(item: MovieModel) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("movie", item)
        startActivity(intent)
    }
}