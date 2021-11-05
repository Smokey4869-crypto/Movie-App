package com.example.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.R
import com.example.myapplication.models.MovieModel
import com.example.myapplication.utils.Credentials

//this class is for favorite movies in each favorite playlist
class FavoriteAdapter (private var favMovies: List<MovieModel>,
                       private val listener: (MovieModel) -> Unit): RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.favorite_list_item, parent, false) as View
        return ViewHolder(view)
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(favMovies[position])
    }
    override fun getItemCount(): Int {
        return favMovies.size
    }

    fun setFavMovies(list: List<MovieModel>) {
        favMovies = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(v: View): RecyclerView.ViewHolder(v) {
        val vImage = v.findViewById<ImageView>(R.id.movie_img)
        val vName = v.findViewById<TextView>(R.id.movie_title)
        val vRating = v.findViewById<TextView>(R.id.movie_rating)

        fun bind(item: MovieModel) {
            item.poster_path?.let {
                Glide.with(itemView.context)
                    .load(Credentials.POSTER_PATH + item.poster_path)
                    .into(vImage)
            }
            vName.text = item.title
            val text = "Score: ${item.vote_average/2}/5"
            vRating.text = text
            vImage.setOnClickListener {
                listener(item)
            }
        }
    }

}