package com.example.myapplication.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapter.FavoriteListAdapter
import com.example.myapplication.models.FavoriteList
import com.example.myapplication.models.MovieModel
import com.example.myapplication.viewmodels.FirebaseViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddMovieFragment : BottomSheetDialogFragment() {
    private lateinit var vFavList: RecyclerView
    private var favoriteLists: MutableList<FavoriteList> = mutableListOf()
    private val firebaseViewModel: FirebaseViewModel by activityViewModels()
    private val favAdapter = FavoriteListDialogAdapter(favoriteLists)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add_to_favorite, container, false) as View

        val movie = arguments?.getParcelable<MovieModel>("movie")

        vFavList = view.findViewById(R.id.favorite_recycler_view)
        val linearLayoutManager = LinearLayoutManager(this.context)
        vFavList.adapter = favAdapter
        vFavList.layoutManager = linearLayoutManager

        val cancelBtn = view.findViewById<Button>(R.id.cancel_button)
        cancelBtn.setOnClickListener {
            dismiss()
        }
        val tempList = arguments?.getParcelableArrayList<FavoriteList>("favorite")
        if (tempList != null) {
            favoriteLists.addAll(tempList)
        }
        val addBtn = view.findViewById<Button>(R.id.add_movie)
        addBtn.setOnClickListener {
            favoriteLists.forEach {
                if (it.chosen) {
                    if (movie != null) {
                        firebaseViewModel.addFavoriteMovieToList(it.listName, movie)
                        dismiss()
                        Toast.makeText(activity, "Movie has been added", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
        return view
    }


    class FavoriteListDialogAdapter(private var favoriteList: List<FavoriteList>): RecyclerView.Adapter<FavoriteListDialogAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater
                .inflate(R.layout.favorite_list_item_dialog, parent, false) as View
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bind(favoriteList[position])
        }

        override fun getItemCount(): Int = favoriteList.size

        fun setFavoriteList(list: List<FavoriteList>) {
            favoriteList = list
        }

        inner class ViewHolder(v: View): RecyclerView.ViewHolder(v) {
            private val vFavName = v.findViewById<TextView>(R.id.fav_name)
            fun bind(item: FavoriteList) {
                vFavName.text = item.listName
                vFavName.setOnClickListener {
                    vFavName.setBackgroundColor(Color.parseColor("#526BF8"))
                    item.chosen = true
                }
            }
        }
    }
}