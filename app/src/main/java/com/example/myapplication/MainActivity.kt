package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.myapplication.fragments.HomeTabFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    val homeTabFragment = HomeTabFragment()
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.bottom_navigation)
        setupBottomNavigation()

        val navController: NavController = Navigation.findNavController(this, R.id.nav_host_fragment)
        val appBarConfiguration: AppBarConfiguration = AppBarConfiguration.Builder(R.id.homeTabFragment, R.id.favoriteTabFragment, R.id.settingTabFragment).build()
        //navigationUI
        NavigationUI.setupWithNavController(bottomNavigationView, navController)
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)

//        supportFragmentManager
//            .beginTransaction()
//            .replace(R.id.recycler_view, homeTabFragment)
//            .commit()

        //Toolbar
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        //searchView
        setupSearchView()

    }

    //get data from the SearchView & query the api to get the results
    private fun setupSearchView() {
        val searchView = findViewById<SearchView>(R.id.search_view)
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
    }

    private fun setupBottomNavigation() {
        bottomNavigationView.setOnItemReselectedListener { item ->
            when (item.itemId) {
                R.id.homeBtn -> {
                    Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.homeTabFragment)
                }
                R.id.favorite -> {
                    Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.favoriteTabFragment)
                }
                R.id.setting -> {
                    Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.settingTabFragment)
                }
                else -> false
            }
            true
        }
    }
}