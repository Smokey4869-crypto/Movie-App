package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
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

        //---Configure RecyclerView MVVM
        vMovieList = findViewById<RecyclerView>(R.id.recycler_view)
        var movieList: List<MovieModel> = listOf()
        val movieAdapter = MovieAdapter(movieList) {}

        configureRecyclerView(movieAdapter)
        movieListViewModel.setQuery("Jack")
        movieListViewModel.movies.observe(this,
            { t ->
                if (t != null) {
                    movieAdapter.setMovieList(t)
                    //Test call
                    for (item: MovieModel in t)
                        Log.v("Test Call", item.title )
                }
            })
        //---RecyclerView MVVM

        //Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        //searchView
        setupSearchView()
    }

    override fun onDestroy() {
        super.onDestroy()
        movieListViewModel.cancelJobs()
    }

    //Configure RecyclerView
    private fun configureRecyclerView(movieAdapter: MovieAdapter){
        val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(this)
        vMovieList.adapter = movieAdapter
        vMovieList.layoutManager = linearLayoutManager

        //Pagination
        vMovieList.addOnScrollListener(object: RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1)) {
                    //display the next search results on the page of api
                    movieListViewModel.searchNextPage()
                }
            }
        })
    }
    private fun showDetail(item: MovieModel) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("movie", item)
        startActivity(intent)
    }


    //get data from the SearchView & query the api to get the results
    private fun setupSearchView() {
        val searchView = findViewById<SearchView>(R.id.search_view)
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    movieListViewModel.setQuery(query)
                }
                return false
            }
        })
    }
}