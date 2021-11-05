package com.example.myapplication.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.myapplication.models.FavoriteList
import com.example.myapplication.models.MovieModel
import com.google.firebase.database.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main

object FirebaseRepository {
    var job: CompletableJob? = null
    val dbRef = FirebaseDatabase.getInstance().getReference("Favorites")

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
                                        if (ds.childrenCount.toInt() == 0) {
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
                dbRef.child(listName).child(movie.id.toString()).setValue(movie
                ) { error, _ ->
                    Log.v("Test Query", "Value was set. Error = $error")
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
                ) { error, _ ->
                    Log.v("Test Query", "Value was set. Error = $error")
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
                { error, _ ->
                    Log.v("Test Query", "Value was removed. Error = $error")
                    deleted = true
                }
            }
        }
        return deleted
    }

    fun addFavoriteMovieToList(listName: String, movie: MovieModel) {
        job = Job()
        job?.let { theJob ->
            CoroutineScope(IO + theJob).launch{
                dbRef.child(listName).child(movie.id.toString()).setValue(movie) {
                        error, _ -> Log.v("Test Query", "Value was set. Error = $error")
                }
            }
        }
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

    fun ifFavoriteMovieExists(movie: MovieModel): LiveData<Boolean> {
        job = Job()
        return object: LiveData<Boolean>() {
            override fun onActive() {
                super.onActive()
                job?.let { theJob ->
                    CoroutineScope(IO + theJob).launch {
                        dbRef.addListenerForSingleValueEvent(object: ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for (child in snapshot.children) {
                                    value = child.hasChild(movie.id.toString())
                                }
                            }
                            override fun onCancelled(error: DatabaseError) {
                            }

                        })
                    }
                }
            }
        }
    }

    fun cancelJob() {
        job?.cancel()
    }
}