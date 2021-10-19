package com.example.myapplication

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.myapplication.models.Genre
import com.example.myapplication.models.MovieModel
import com.example.myapplication.utils.Credentials
import com.google.android.material.imageview.ShapeableImageView
import com.ms.square.android.expandabletextview.ExpandableTextView

class DetailActivity: AppCompatActivity() {
    var movie: MovieModel? = null
//    val vOverview = findViewById<ExpandableTextView>(R.id.movie_overview)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val movie = intent.getParcelableExtra<MovieModel>("Movie Model")
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


            //Parse text
            var genreText = ""
            for (i in 1..movie.genre_ids.size-1) {
                val genreName = Genre.getGenre(movie.genre_ids[i])
                genreText =
                    "$genreName|"
            }
            genreText += Genre.getGenre(movie.genre_ids[movie.genre_ids.size])
            vGenre.text = genreText


            vTitle.text = movie.title
            vReleaseDate.text = movie.release_date
//            vOverview.text = movie.overview
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
        }
    }
}