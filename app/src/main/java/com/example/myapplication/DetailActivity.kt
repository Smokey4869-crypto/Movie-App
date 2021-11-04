package com.example.myapplication

import android.os.Bundle
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.myapplication.fragments.AddMovieFragment
import com.example.myapplication.models.Genre
import com.example.myapplication.models.MovieModel
import com.example.myapplication.utils.Credentials
import com.example.myapplication.viewmodels.FirebaseViewModel
import com.example.myapplication.viewmodels.MovieViewModel
import com.google.android.material.imageview.ShapeableImageView
import com.ms.square.android.expandabletextview.ExpandableTextView

class DetailActivity: AppCompatActivity() {
    private lateinit var movie: MovieModel
    private val firebaseViewModel: FirebaseViewModel by viewModels()
    private val movieViewModel: MovieViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val movie = intent.getParcelableExtra<MovieModel>("Movie Model")
        val genres = intent.getParcelableArrayListExtra<Genre>("genres")
        movie?.let {
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
            var text = ""
            for (i in 0..movie.genre_ids.size-2) {
                genres?.forEach { genre ->
                    if (movie.genre_ids[i] == genre.id)
                        text += genre.name + ", "
                }
            }
            genres?.forEach { genre ->
                if (movie.genre_ids[movie.genre_ids.size-1] == genre.id)
                    text += genre.name
            }

            vGenre.text = text

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

    }
}

