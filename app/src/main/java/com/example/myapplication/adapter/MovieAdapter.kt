package com.example.myapplication.adapter

import android.graphics.*
import android.graphics.Color.parseColor
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.models.MovieModel
import com.example.myapplication.utils.Credentials

class MovieAdapter(private var movieList: List<MovieModel>,
                   private val listener: (MovieModel) -> Unit): RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.movie_list_item, parent, false) as View
        view.setOnClickListener {listener}
        return MovieViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movieList[position])
    }

    override fun getItemCount(): Int = movieList.size

    fun setMovieList(list: List<MovieModel>) {
        movieList = list
        notifyDataSetChanged()
    }

    fun getMovieList(): List<MovieModel> = movieList

    inner class MovieViewHolder(v: View): RecyclerView.ViewHolder(v) {
        private var vTitle = v.findViewById<TextView>(R.id.movie_title)
        private var vReleaseDate = v.findViewById<TextView>(R.id.movie_release_date)
        private var vDuration = v.findViewById<TextView>(R.id.movie_duraton)
        private var vImage = v.findViewById<ImageView>(R.id.movie_img)
        private var ratingBar = v.findViewById<RatingBar>(R.id.rating_bar)

        @RequiresApi(Build.VERSION_CODES.Q)
        fun bind(movie: MovieModel) {
            vTitle.text = movie.title
            vReleaseDate.text = movie.release_date
            vDuration.text = movie.duration

            if (movie.vote_average/2 <= ratingBar.numStars/2) {
                ratingBar.progressDrawable.colorFilter = BlendModeColorFilter(Color.RED, BlendMode.SRC_IN)
                ratingBar.rating = movie.vote_average/2
            } else {
                ratingBar.progressDrawable.colorFilter = BlendModeColorFilter(Color.GREEN, BlendMode.SRC_IN)
                ratingBar.rating = movie.vote_average/2
            }

            //imageView using Glide Library
            vImage?.let {
                Glide.with(itemView.context)
                    .load(Credentials.POSTER_PATH + movie.poster_path)
                    .into(it)
            }
        }
    }
}