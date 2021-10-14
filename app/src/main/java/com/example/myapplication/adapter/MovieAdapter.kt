package com.example.myapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.models.MovieModel

class MovieAdapter(private val movieList: List<MovieModel>,
                   private val listener: (MovieModel) -> Unit): RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {
    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        context = parent.context
        val view = layoutInflater
            .inflate(R.layout.movie_list_item, parent, false) as View
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movieList[position])
    }

    override fun getItemCount(): Int {
        if (movieList.size == 0) {
            Toast.makeText(context, "List is empty", Toast.LENGTH_LONG).show()
        }
        return movieList.size
    }

    inner class MovieViewHolder(v: View): RecyclerView.ViewHolder(v) {
        var vTitle = v.findViewById<TextView>(R.id.movie_title)
        var vReleaseDate = v.findViewById<TextView>(R.id.movie_release_date)
        var vDuration = v.findViewById<TextView>(R.id.movie_duraton)
        var vImage = v.findViewById<ImageView>(R.id.movie_img)
        var ratingBar = v.findViewById<RatingBar>(R.id.rating_bar)

        fun bind(movie: MovieModel) {
            vTitle?.text = movie.title
            vReleaseDate?.text = movie.release_date
            vDuration?.text = movie.duration
            ratingBar?.rating = movie.vote_average/2

            //imageView using Glide Library
            vImage?.let {
                Glide.with(itemView.context)
                    .load(movie)
                    .into(it)
            }
        }
    }
}