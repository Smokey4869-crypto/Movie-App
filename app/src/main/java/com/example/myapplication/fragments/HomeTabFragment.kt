package com.example.myapplication.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.DetailActivity
import com.example.myapplication.R
import com.example.myapplication.adapter.MovieAdapter
import com.example.myapplication.models.Genre
import com.example.myapplication.models.MovieModel
import com.example.myapplication.viewmodels.FirebaseViewModel
import com.example.myapplication.viewmodels.MovieViewModel


class HomeTabFragment : BaseFragment() {
    private val movieListViewModel: MovieViewModel by activityViewModels()
    private lateinit var vMovieList: RecyclerView
    private var movieList: List<MovieModel> = listOf()
    private val movieAdapter = MovieAdapter(movieList) {showDetail(it)}
    private var isPopular: Boolean = true
    private lateinit var genres: List<Genre>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_home_tab, container, false) as View
        vMovieList = view.findViewById(R.id.fragment_home_tab)
        configureRecyclerView()
        callNetworkConnection()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (haveInternet) {
            observeMoviesChange()
            if (isPopular)
                observePopMovies()
            getMovieGenres()
        }
    }

    private fun getMovieGenres() {
        movieListViewModel.genres.observe(viewLifecycleOwner,
            { t ->
                if (t != null) {
                    genres = t
                }
            })
    }
    private fun observeMoviesChange() {
        movieListViewModel.movies.observe(viewLifecycleOwner,
            { t ->
                if (t != null) {
                    movieAdapter.setMovieList(t)
                    //Test call
//                    for (item: MovieModel in t)
//                        Log.v("Test Call", item.title )
                }
            })
    }
    private fun observePopMovies() {
        movieListViewModel.popularMovies.observe(viewLifecycleOwner,
            { t ->
                if (t != null ) {
                    movieAdapter.setMovieList(t)
                }
            })
    }
    private fun configureRecyclerView(){
//        val linearLayoutManager = LinearLayoutManager(this.context, LinearLayoutManager.HORIZONTAL, false)
        val linearLayoutManager = LinearLayoutManager(this.context)
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
                if (!recyclerView.canScrollVertically(-1)) {
                    movieListViewModel.searchLastPage()
                }
            }
        })
    }

    private fun showDetail(item: MovieModel) {
        val intent = Intent(activity, DetailActivity::class.java)
        val genresArray: ArrayList<Genre> = arrayListOf()
        genresArray.addAll(genres)
        intent.putParcelableArrayListExtra("genres", genresArray)
        intent.putExtra("Movie Model", item)
        startActivity(intent)
    }

    fun setQuery(newQuery: String) {
        movieListViewModel.setQuery(newQuery)
        isPopular = false
    }
    fun setPage(pageNum: Int) {
        movieListViewModel.setPage(pageNum)
    }

    override fun onDestroy() {
        super.onDestroy()
        movieListViewModel.cancelJobs()
    }
}