package com.example.myapplication

import android.content.Context
import android.net.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.widget.SearchView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.connectvity.ConnectionLiveData
import com.example.myapplication.fragments.HomeTabFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.net.ConnectivityManager.NetworkCallback
import com.example.myapplication.fragments.FavoriteTabFragment
import com.google.firebase.perf.FirebasePerformance

import java.lang.Exception


class MainActivity : AppCompatActivity() {
    private var homeTabFragment = HomeTabFragment()
    private lateinit var connectionLiveData: ConnectionLiveData
    private lateinit var bottomNavigationView: BottomNavigationView
    var isNetworkConnected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        connectionLiveData = ConnectionLiveData(this)
        bottomNavigationView = findViewById(R.id.bottom_navigation)

        registerNetworkCallback()
        val navController: NavController = Navigation.findNavController(this, R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(setOf(R.id.homeTabFragment, R.id.favoriteTabFragment, R.id.settingTabFragment, R.id.detailFavListFragment))
        //navigationUI
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavigationView.setupWithNavController(navController)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        if (navHostFragment?.childFragmentManager?.fragments?.get(0) is HomeTabFragment)
            homeTabFragment = navHostFragment.childFragmentManager.fragments[0] as HomeTabFragment
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.appbar_menu, menu)
        val menuItem = menu?.findItem(R.id.search_view)
        val searchView = menuItem?.actionView as SearchView
        searchView.queryHint = "Looking for a movie?"

        //Getting data from SearchView and set query to change live data
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    homeTabFragment.setQuery(query)
                    homeTabFragment.setPage(1)
                    //Come back to home tab when searching
                    Navigation.findNavController(this@MainActivity, R.id.nav_host_fragment).navigate(R.id.homeTabFragment)
                }
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    //Check internet connection

    // Network Check
    fun registerNetworkCallback() {
        try {
            val connectivityManager =
                this.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkRequest = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()
            connectivityManager.registerNetworkCallback(networkRequest, object : NetworkCallback() {
                override fun onAvailable(network: Network) {
                    isNetworkConnected = true // Global Static Variable
                }

                override fun onLost(network: Network) {
                    isNetworkConnected = false // Global Static Variable
                }
            }
            )
            isNetworkConnected = false
        } catch (e: Exception) {
            isNetworkConnected = false
        }
    }
}