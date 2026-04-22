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
import com.example.artistico_android.domain.repo.AuthRepository
import com.example.artistico_android.domain.repo.OnboardingRepository
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    @Inject lateinit var authRepository: AuthRepository
    @Inject lateinit var onboardingRepository: OnboardingRepository

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

        // Pick the start destination based on two persisted flags:
        //   - active session → straight to Explore
        //   - onboarding seen before → Login
        //   - neither → the onboarding carousel
        // Both reads are one-off DataStore lookups (microseconds, not network), so
        // runBlocking in onCreate is acceptable and avoids a first-frame flash.
        val startDestination = runBlocking {
            when {
                authRepository.isLoggedInNow() -> R.id.nav_explore
                onboardingRepository.hasCompletedOnboardingNow() -> R.id.nav_login
                else -> R.id.nav_onboarding
            }
        }
        val graph = navController.navInflater.inflate(R.navigation.nav_root)
        graph.setStartDestination(startDestination)
        navController.graph = graph

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
     * popUpTo is pinned to nav_explore (not graph.startDestinationId) because the
     * graph's start destination may be nav_login when the user is signed out —
     * we don't want tab switches to route the back stack through the login screen.
     */
    private fun wireBottomNav(navController: NavController) {
        val navOptions = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setRestoreState(true)
            .setPopUpTo(R.id.nav_explore, inclusive = false, saveState = true)
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
