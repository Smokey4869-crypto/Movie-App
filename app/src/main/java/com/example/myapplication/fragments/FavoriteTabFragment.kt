package com.example.myapplication.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.adapter.FavoriteListAdapter
import com.example.myapplication.models.FavoriteList
import com.example.myapplication.viewmodels.FirebaseViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FavoriteTabFragment : BaseFragment() {
    private val favListViewModel: FirebaseViewModel by activityViewModels()
    private lateinit var vFavList: RecyclerView
    private var favoriteList: List<FavoriteList> = listOf()
    private val favAdapter = FavoriteListAdapter(favoriteList) {showListItem(it)}

    private val rotateOpen: Animation by lazy {AnimationUtils.loadAnimation(activity, R.anim.rotate_open_anim)}
    private val rotateClose: Animation by lazy {AnimationUtils.loadAnimation(activity, R.anim.rotate_close_anim)}
    private val fromBottom: Animation by lazy {AnimationUtils.loadAnimation(activity, R.anim.from_bottom_anim)}
    private val toBottom: Animation by lazy {AnimationUtils.loadAnimation(activity, R.anim.to_bottom_anim)}

    private var clicked: Boolean = false
    private lateinit var editBtn: FloatingActionButton
    private lateinit var addBtn: FloatingActionButton
    private lateinit var delBtn: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_favorite_tab, container, false) as View
        vFavList = view.findViewById(R.id.favorite_recycler_view)
        val linearLayoutManager = LinearLayoutManager(this.context)
        vFavList.adapter = favAdapter
        vFavList.layoutManager = linearLayoutManager

        editBtn = view.findViewById(R.id.edit_btn)
        addBtn = view.findViewById(R.id.add_btn)

        delBtn = view.findViewById(R.id.delete_btn)
        editBtn.setOnClickListener {
            onEditButtonClicked()
        }
        addBtn.setOnClickListener {
            val addFragment = AddListFragment()
            val transaction = childFragmentManager.beginTransaction()
            transaction.replace(R.id.add_fragment, addFragment)
            transaction.commit()

            Toast.makeText(activity, "Edit favorites", Toast.LENGTH_LONG).show()
        }
        delBtn.setOnClickListener {
            Toast.makeText(activity, "Delete favorites", Toast.LENGTH_LONG).show()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        favListViewModel.favoriteLists.observe(viewLifecycleOwner,
            { t ->
                if (t != null) {
                    favAdapter.setFavoriteList(t)
                }
            })
    }

    private fun showListItem(item: FavoriteList) {
        val action = FavoriteTabFragmentDirections.actionFavoriteTabFragmentToDetailFavListFragment(item)
        activity?.findNavController(R.id.nav_host_fragment)?.navigate(action)
    }

    //This is for the expandable FAB animation
    private fun onEditButtonClicked() {
        setVisibility(clicked)
        setAnimation(clicked)
        setClickable(clicked)
        clicked = !clicked
    }
    private fun setVisibility(clicked: Boolean) {
        if (!clicked) {
            addBtn.visibility = View.VISIBLE
            delBtn.visibility = View.VISIBLE
        } else {
            addBtn.visibility = View.INVISIBLE
            delBtn.visibility = View.INVISIBLE
        }
    }
    private fun setAnimation(clicked: Boolean) {
        if (!clicked) {
            addBtn.startAnimation(fromBottom)
            delBtn.startAnimation(fromBottom)
            editBtn.startAnimation(rotateOpen)
        } else {
            addBtn.startAnimation(toBottom)
            delBtn.startAnimation(toBottom)
            editBtn.startAnimation(rotateClose)
        }
    }
    private fun setClickable(clicked: Boolean) {
        if (!clicked) {
            addBtn.isClickable = true
            delBtn.isClickable = true
        } else {
            addBtn.isClickable = false
            delBtn.isClickable = false
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        favListViewModel.cancelJobs()
    }
}

