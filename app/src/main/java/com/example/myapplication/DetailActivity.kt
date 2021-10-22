package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.navArgs
import com.bumptech.glide.Glide
import com.example.myapplication.fragments.AddMovieFragment
import com.example.myapplication.models.Genre
import com.example.myapplication.utils.Credentials
import com.example.myapplication.viewmodels.FirebaseViewModel
import com.example.myapplication.viewmodels.MovieViewModel
import com.google.android.material.imageview.ShapeableImageView
import com.ms.square.android.expandabletextview.ExpandableTextView

class DetailActivity: AppCompatActivity() {
    private val args by navArgs<DetailActivityArgs>()
    private val firebaseViewModel: FirebaseViewModel by viewModels()
    private val movieViewModel: MovieViewModel by viewModels()
    private lateinit var genres: List<Genre>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val movie = args.chosenMovie
        genres = movieViewModel.genres
        movie.let {
            //text views
            val vTitle = findViewById<TextView>(R.id.movie_title)
            val vReleaseDate = findViewById<TextView>(R.id.movie_release_date)
            //image views
            val vPoster = findViewById<ShapeableImageView>(R.id.movie_poster)
            val vBackdrop = findViewById<ImageView>(R.id.movie_backdrop)
            //rating bar
            val ratingBar = findViewById<RatingBar>(R.id.rating_bar)
            val vGenre = findViewById<TextView>(R.id.movie_genre)
            val vOverview = findViewById<ExpandableTextView>(R.id.movie_overview)
            val heartBtn = findViewById<Button>(R.id.heart_button)

            //Parse text
            var genreText = ""
//            movie.genre_ids.forEach { genreId ->
//                genres.forEach {
//                    if (it.id == genreId) {
//                        genreText += it.name + "|"
//                    }
//                }
//            }
//            genreText += Genre.getGenre(movie.genre_ids[movie.genre_ids.size-1])
            vGenre.text = genreText


            vTitle.text = movie.title
            vReleaseDate.text = movie.release_date
            vOverview.text = movie.overview
            vPoster?.let {
                Glide.with(this)
                    .load(Credentials.POSTER_PATH + movie.poster_path)
                    .into(vPoster)
            }
            vBackdrop?.let {
                Glide.with(this)
                    .load(Credentials.POSTER_PATH + movie.backdrop_path)
                    .into(vBackdrop)
            }
            ratingBar.rating = movie.vote_average / 2

            heartBtn.setOnClickListener {
                val addFragment = AddMovieFragment()
                val bundle = Bundle()
                bundle.putParcelable("movie", movie)
                addFragment.arguments = bundle
                addFragment.show(supportFragmentManager, "Add Movie to List")
                Toast.makeText(this, "Edit favorites", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun checkFavorite() {
        val movie = args.chosenMovie

    }
}

