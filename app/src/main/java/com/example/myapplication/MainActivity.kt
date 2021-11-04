package com.example.myapplication

import android.net.*
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.myapplication.connectvity.CheckNetworkConnection
import com.example.myapplication.fragments.HomeTabFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {
    private var homeTabFragment = HomeTabFragment()
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var checkNetworkConnection: CheckNetworkConnection
    private lateinit var vConnection: TextView
    var isConnected: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        vConnection = findViewById(R.id.connection_warning)

        callNetworkConnection()

        bottomNavigationView = findViewById(R.id.bottom_navigation)

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
    private fun callNetworkConnection() {
        checkNetworkConnection = CheckNetworkConnection(application)
        checkNetworkConnection.observe(this,{ isConnected ->
            if (isConnected){
                this@MainActivity.isConnected = true
                vConnection.text = ""
            }else{
                this@MainActivity.isConnected = false
                vConnection.text = "No internet connection"
            }
        })

    }
}