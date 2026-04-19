package com.example.artistico_android.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.artistico_android.R
import com.example.artistico_android.core.ui.collectWhenStarted
import com.example.artistico_android.databinding.FragmentProfileBinding
import com.example.artistico_android.domain.model.ProfileTab
import com.example.artistico_android.domain.model.User
import com.example.artistico_android.domain.model.UserRole
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ProfileViewModel by viewModels()
    private val gridAdapter = ProfileGridAdapter()

    private val tabs = listOf(
        ProfileTab.POSTS to R.drawable.ic_grid,
        ProfileTab.BOOKMARKS to R.drawable.ic_bookmark,
        ProfileTab.PINNED to R.drawable.ic_pin,
        ProfileTab.TROPHIES to R.drawable.ic_trophy
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Stats labels (icons don't change, just set them once).
        binding.statFollowers.iconStat.setImageResource(R.drawable.ic_people)
        binding.statFollowers.txtStatLabel.text = getString(R.string.stat_followers)
        binding.statFollowing.iconStat.setImageResource(R.drawable.ic_person_check)
        binding.statFollowing.txtStatLabel.text = getString(R.string.stat_following)
        binding.statPosts.iconStat.setImageResource(R.drawable.ic_collection)
        binding.statPosts.txtStatLabel.text = getString(R.string.stat_posts)

        binding.recyclerGrid.apply {
            layoutManager = GridLayoutManager(requireContext(), 3)
            adapter = gridAdapter
            setHasFixedSize(false)
        }

        setupTabs()

        binding.btnOverflow.setOnClickListener { anchor ->
            PopupMenu(requireContext(), anchor).apply {
                menuInflater.inflate(R.menu.menu_profile_overflow, menu)
                setOnMenuItemClickListener { item ->
                    Snackbar.make(binding.root, item.title ?: "", Snackbar.LENGTH_SHORT).show()
                    true
                }
            }.show()
        }

        binding.fabNewPost.setOnClickListener {
            Snackbar.make(binding.root, "Create post — coming soon", Snackbar.LENGTH_SHORT).show()
        }

        collectWhenStarted(viewModel.uiState) { state ->
            state.user?.let { bindUser(it) }
            gridAdapter.submitList(state.posts)
            binding.recyclerGrid.isVisible = state.posts.isNotEmpty()
            binding.txtTabEmpty.isVisible = !state.isLoading && state.posts.isEmpty()
            syncSelectedTab(state.activeTab)
        }
    }

    private fun setupTabs() {
        tabs.forEach { (_, iconRes) ->
            binding.tabLayout.addTab(binding.tabLayout.newTab().setIcon(iconRes))
        }
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val profileTab = tabs.getOrNull(tab.position)?.first ?: return
                viewModel.onTabSelected(profileTab)
            }
            override fun onTabUnselected(tab: TabLayout.Tab) = Unit
            override fun onTabReselected(tab: TabLayout.Tab) = Unit
        })
    }

    private fun syncSelectedTab(activeTab: ProfileTab) {
        val index = tabs.indexOfFirst { it.first == activeTab }
        if (index >= 0 && binding.tabLayout.selectedTabPosition != index) {
            binding.tabLayout.getTabAt(index)?.select()
        }
    }

    private fun bindUser(user: User) {
        binding.imgAvatar.setImageResource(user.avatarRes ?: R.drawable.placeholder_avatar)
        binding.txtName.text = user.displayName

        val roleLabel = when (user.role) {
            UserRole.DEV -> getString(R.string.role_dev)
            UserRole.USER -> getString(R.string.role_user)
        }
        binding.chipRole.text = roleLabel
        binding.chipRole.isVisible = user.role == UserRole.DEV

        binding.txtBio.text = user.bio.orEmpty()
        binding.txtBio.isVisible = !user.bio.isNullOrBlank()

        binding.statFollowers.txtStatNumber.text = user.followersCount.toString()
        binding.statFollowing.txtStatNumber.text = user.followingCount.toString()
        binding.statPosts.txtStatNumber.text = user.postsCount.toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerGrid.adapter = null
        _binding = null
    }
}
