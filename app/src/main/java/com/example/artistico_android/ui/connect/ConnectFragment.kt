package com.example.artistico_android.ui.connect

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.artistico_android.R
import com.example.artistico_android.core.ui.collectWhenStarted
import com.example.artistico_android.databinding.FragmentConnectBinding
import com.example.artistico_android.domain.model.ConnectTab
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ConnectFragment : Fragment() {

    private var _binding: FragmentConnectBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ConnectViewModel by viewModels()

    private val communityAdapter = CommunityAdapter()
    private val chatAdapter = ChatPreviewAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConnectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(false)
        }

        binding.tabCommunities.setOnClickListener { viewModel.onTabSelected(ConnectTab.COMMUNITIES) }
        binding.tabChats.setOnClickListener { viewModel.onTabSelected(ConnectTab.CHATS) }

        binding.inputSearch.doAfterTextChanged { viewModel.onQueryChanged(it?.toString().orEmpty()) }

        binding.fabNew.setOnClickListener {
            Snackbar.make(binding.root, "Create community — coming soon", Snackbar.LENGTH_SHORT).show()
        }

        collectWhenStarted(viewModel.uiState) { state ->
            updateTabTint(state.activeTab)
            binding.txtEmpty.isVisible = state.isEmpty
            binding.txtEmpty.text = when (state.activeTab) {
                ConnectTab.COMMUNITIES -> getString(R.string.connect_empty)
                ConnectTab.CHATS -> getString(R.string.connect_empty)
            }
            binding.recyclerList.isVisible = !state.isEmpty

            when (state.activeTab) {
                ConnectTab.COMMUNITIES -> {
                    if (binding.recyclerList.adapter !== communityAdapter) {
                        binding.recyclerList.adapter = communityAdapter
                    }
                    communityAdapter.submitList(state.communities)
                }
                ConnectTab.CHATS -> {
                    if (binding.recyclerList.adapter !== chatAdapter) {
                        binding.recyclerList.adapter = chatAdapter
                    }
                    chatAdapter.submitList(state.chats)
                }
            }
        }
    }

    private fun updateTabTint(activeTab: ConnectTab) {
        val active = ContextCompat.getColor(requireContext(), R.color.brand_maroon)
        val inactive = ContextCompat.getColor(requireContext(), R.color.text_tertiary)
        binding.iconTabCommunities.setColorFilter(if (activeTab == ConnectTab.COMMUNITIES) active else inactive)
        binding.iconTabChats.setColorFilter(if (activeTab == ConnectTab.CHATS) active else inactive)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerList.adapter = null
        _binding = null
    }
}
