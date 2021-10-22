package com.example.myapplication.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.myapplication.models.FavoriteList
import com.example.myapplication.models.MovieModel
import com.google.firebase.database.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

object FirebaseRepository {
    var job: CompletableJob? = null
    val dbRef = FirebaseDatabase.getInstance().getReference("Favorites")
    val allLists: LiveData<List<FavoriteList>> = getAllFavoriteList()

    fun getAllFavoriteList(): LiveData<List<FavoriteList>> {
        job = Job()
        return object: LiveData<List<FavoriteList>>() {
            override fun onActive() {
                super.onActive()
                job?.let { theJob ->
                    CoroutineScope(IO + theJob).launch {
                        val favList: MutableList<FavoriteList> = mutableListOf()
                        dbRef.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                for (ds in dataSnapshot.children) {
                                    val child = ds.key
                                    if (child != null) {
                                        Log.d("TAG", child)
                                        if (ds.childrenCount.toInt().equals(0)) {
                                            val fav = FavoriteList(child, ds.childrenCount, null)
                                            favList.add(fav)
                                        } else {
                                            val movieList: MutableList<MovieModel> = mutableListOf()
                                            for (dsChild in ds.children) {
                                                val movie = dsChild.getValue(MovieModel::class.java)
                                                if (movie != null) {
                                                    movieList.add(movie)
                                                }
                                            }
                                            val fav = FavoriteList(child, ds.childrenCount, movieList)
                                            favList.add(fav)
                                        }

                                    }
                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                Log.i("onCancelled", databaseError.toException().toString())
                            }
                        })
                        withContext(Main) {
                            value = favList
                            theJob.complete()
                        }
                    }
                }
            }
        }
    }

    fun addFavoriteListWithInitialMovie(listName: String, movie: MovieModel): Boolean {
        var added = false
        job = Job()
        job?.let { theJob ->
            CoroutineScope(IO + theJob).launch {
                dbRef.child(listName).setValue(movie
                ) { error, ref ->
                    Log.v("Test Query", "Value was set. Error = ${error}")
                    added = true
                }
            }
        }
        return added
    }
    fun addEmptyFavoriteList(listName: String): Boolean {
        var added = false
        job = Job()
        job?.let { theJob ->
            CoroutineScope(IO + theJob).launch {
                dbRef.child(listName).setValue("empty"
                ) { error, ref ->
                    Log.v("Test Query", "Value was set. Error = ${error}")
                    added = true
                }
            }
        }
        return added
    }

    fun deleteFavoriteList(listName: String): Boolean {
        var deleted = false
        job = Job()
        job?.let { theJob ->
            CoroutineScope(IO + theJob).launch {
                dbRef.child(listName).removeValue()
                { error, ref ->
                    Log.v("Test Query", "Value was removed. Error = ${error}")
                    deleted = true
                }
            }
        }
        return deleted
    }

    fun ifListExists(listName: String): Boolean {
        var exists = false
        dbRef.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.hasChild(listName))
                    exists = true
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
        return exists
    }

    fun ifFavoriteMovieExists(movie: MovieModel): Boolean {
        var exists = false
        job = Job()
        job?.let { theJob ->
            CoroutineScope(IO + theJob).launch {
                dbRef.addListenerForSingleValueEvent(object: ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }

                })
            }
        }
        return exists
    }

    fun cancelJob() {
        job?.cancel()
    }
}