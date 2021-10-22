package com.example.myapplication.adapter

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.fragments.FavoriteTabFragment
import com.example.myapplication.fragments.ShowFavoriteItemFragment
import com.example.myapplication.models.FavoriteList
import com.example.myapplication.models.MovieModel
import java.util.ArrayList

//this class is for each playlist in favorite tab
class FavoriteListAdapter(private var favoriteList: List<FavoriteList>,
                          private val listener: (FavoriteList) -> Unit): RecyclerView.Adapter<FavoriteListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater
            .inflate(R.layout.favorite_playlist_item, parent, false) as View
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(favoriteList[position])
    }

    override fun getItemCount(): Int = favoriteList.size

    fun setFavoriteList(list: List<FavoriteList>) {
        favoriteList = list
        Log.v("Adapter List Size", itemCount.toString())
        notifyDataSetChanged()
    }

    inner class ViewHolder(v: View): RecyclerView.ViewHolder(v) {
        val vPlaylistName = v.findViewById<TextView>(R.id.playlist_name)
        val vMovieNum = v.findViewById<TextView>(R.id.movie_num)

        fun bind(item: FavoriteList) {
            vPlaylistName.text = item.listName

            item.movieNum.let {
                val text: String
                text = if (it.toInt().equals(0) || it.toInt().equals(1)) "$it video"
                else "$it videos"
                vMovieNum.text = text
            }

            vPlaylistName.setOnClickListener {
                listener(item)

            }
        }
    }

}