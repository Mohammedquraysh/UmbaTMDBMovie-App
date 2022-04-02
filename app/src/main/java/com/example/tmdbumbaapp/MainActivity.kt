package com.example.tmdbumbaapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.tmdbumbaapp.databinding.ActivityMainBinding
import com.example.tmdbumbaapp.util.MoviePreference
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding:ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        MoviePreference.initPreference(this)
        /** This is the setup for bottom navigation bar that is used to navigate to explore fragment,
        Wishlist fragment and Profile fragment
         */
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.findNavController()
        binding.bottomNavigationBar.setupWithNavController(navController)



        /** to set listener to bottom Navigation */
        val  bottomNav: BottomNavigationView = findViewById(R.id.bottomNavigationBar)
        bottomNav.setOnNavigationItemSelectedListener(object : BottomNavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                val id = item.itemId
                if (id == R.id.latestMovie){
                    navController.navigateUp()
                    navController.navigate(R.id.latestMoviesFragment)
                }
                if (id == R.id.popularMovie){
                    navController.navigateUp()
                    navController.navigate(R.id.popularMoviesFragment)
                }
                if (id == R.id.upcomingMovie){
                    navController.navigateUp()
                    navController.navigate(R.id.upcomingMovieFragment)
                }
                return true
            }
        })

    }
}