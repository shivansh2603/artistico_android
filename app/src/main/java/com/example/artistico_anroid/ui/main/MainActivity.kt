package com.example.artistico_anroid.ui.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.artistico_anroid.R
import com.example.artistico_anroid.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val topLevelDestinations = setOf(
        R.id.nav_explore,
        R.id.nav_connect,
        R.id.nav_challenges,
        R.id.nav_profile
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHost = supportFragmentManager
            .findFragmentById(R.id.nav_host_container) as NavHostFragment
        val navController = navHost.navController

        binding.bottomNav.setupWithNavController(navController)

        // Reselect → scroll to top (handled by fragments via SharedViewModel in a future sprint);
        // for now, just swallow the reselection so it doesn't re-add to back stack.
        binding.bottomNav.setOnItemReselectedListener { /* no-op */ }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val isTopLevel = destination.id in topLevelDestinations
            binding.bottomBarCard.isVisible = isTopLevel
            binding.fabSearch.visibility = if (isTopLevel) View.VISIBLE else View.GONE
        }

        binding.fabSearch.setOnClickListener {
            Snackbar.make(binding.root, "Global search — coming soon", Snackbar.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_container).navigateUp() || super.onSupportNavigateUp()
    }
}
