package com.example.artistico_android.ui.main

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.artistico_android.R
import com.example.artistico_android.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // destinationId -> the item's root LinearLayout in the custom bottom nav.
    // Kept as a lazy property so we can iterate in one place for both
    // click-wiring and selection-state syncing.
    private val navItems: List<Pair<Int, LinearLayout>> by lazy {
        listOf(
            R.id.nav_explore    to binding.bottomNav.navItemExplore,
            R.id.nav_connect    to binding.bottomNav.navItemConnect,
            R.id.nav_challenges to binding.bottomNav.navItemChallenges,
            R.id.nav_profile    to binding.bottomNav.navItemProfile,
        )
    }

    private val topLevelDestinations: Set<Int> by lazy {
        navItems.map { it.first }.toSet()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, true)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHost = supportFragmentManager
            .findFragmentById(R.id.nav_host_container) as NavHostFragment
        val navController = navHost.navController

        wireBottomNav(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            val isTopLevel = destination.id in topLevelDestinations
            binding.bottomBarCard.isVisible = isTopLevel
            binding.fabSearch.visibility = if (isTopLevel) View.VISIBLE else View.GONE
            // Sync the pink pill to whichever destination we're on.
            syncSelection(destination.id)
        }

        binding.fabSearch.setOnClickListener {
            Snackbar.make(binding.root, "Global search — coming soon", Snackbar.LENGTH_SHORT).show()
        }
    }

    /**
     * Wire each item's click to navigate to the matching top-level destination.
     * We use a custom NavOptions with singleTop + popUpTo(startDestination) to
     * match the behaviour of NavigationUI.setupWithNavController(): clicking
     * a tab that you're already on does nothing, and switching tabs clears the
     * back stack up to the graph's start destination.
     */
    private fun wireBottomNav(navController: NavController) {
        val navOptions = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setRestoreState(true)
            .setPopUpTo(navController.graph.startDestinationId, inclusive = false, saveState = true)
            .build()

        navItems.forEach { (destinationId, itemView) ->
            itemView.setOnClickListener {
                if (navController.currentDestination?.id != destinationId) {
                    navController.navigate(destinationId, null, navOptions)
                }
            }
        }
    }

    /**
     * Flip isSelected on each item so the selector drawable (pink pill),
     * icon tint, and label colour all update together via duplicateParentState.
     */
    private fun syncSelection(destinationId: Int) {
        navItems.forEach { (destId, itemView) ->
            itemView.isSelected = (destId == destinationId)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_container).navigateUp() || super.onSupportNavigateUp()
    }
}
