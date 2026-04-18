package com.example.artistico_anroid.ui.explore

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.artistico_anroid.R
import com.example.artistico_anroid.core.ui.collectWhenStarted
import com.example.artistico_anroid.databinding.FragmentExploreBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExploreFragment : Fragment() {

    private var _binding: FragmentExploreBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ExploreViewModel by viewModels()
    private lateinit var sectionAdapter: ExploreSectionAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sectionAdapter = ExploreSectionAdapter(
            onPostClick = { post ->
                findNavController().navigate(
                    R.id.action_explore_to_post,
                    Bundle().apply { putString("postId", post.id) }
                )
            },
            onLikeClick = { post -> viewModel.onLikeClicked(post.id) }
        )

        binding.recyclerSections.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = sectionAdapter
            setHasFixedSize(false)
        }

        binding.swipeRefresh.setColorSchemeResources(R.color.brand_maroon)
        binding.swipeRefresh.setOnRefreshListener { viewModel.onRefresh() }

        collectWhenStarted(viewModel.uiState) { state ->
            binding.swipeRefresh.isRefreshing = state.isRefreshing
            sectionAdapter.submitList(state.sections)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.recyclerSections.adapter = null
        _binding = null
    }
}
