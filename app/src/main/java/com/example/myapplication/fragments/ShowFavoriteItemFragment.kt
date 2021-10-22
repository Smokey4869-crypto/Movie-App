package com.example.myapplication.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapter.FavoriteAdapter
import com.example.myapplication.models.MovieModel

class ShowFavoriteItemFragment : Fragment() {
    private lateinit var vMovieList: RecyclerView
    private var movieList: List<MovieModel> = listOf()
    private lateinit var movieAdapter: FavoriteAdapter
    private val args by navArgs<ShowFavoriteItemFragmentArgs>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_show_favorite_item, container, false) as View

        val movies = args.favoriteItems.movies
        if (movies != null) {
            movieAdapter = FavoriteAdapter(movies) {}
            vMovieList = view.findViewById(R.id.fav_item_recycler_view)
            vMovieList.adapter = movieAdapter
            val linearLayoutManager = LinearLayoutManager(this.context)
            vMovieList.layoutManager = linearLayoutManager
        } else {
            val vText = view.findViewById<TextView>(R.id.info_text)
            vText.text = "There are no movies in this list yet"
        }

        Log.v("ShowFavItemFrag", "fragment is created")
        return view
    }

}