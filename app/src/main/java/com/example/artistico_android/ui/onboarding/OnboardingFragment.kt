package com.example.artistico_android.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.artistico_android.R
import com.example.artistico_android.core.ui.collectWhenStarted
import com.example.artistico_android.databinding.FragmentOnboardingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingFragment : Fragment() {

    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!
    private val viewModel: OnboardingViewModel by viewModels()

    private val pages = OnboardingPage.ALL

    /** The dots are added to the layout statically once in onViewCreated. */
    private lateinit var dots: List<ImageView>

    private val pageChangeCallback = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            updateUiForPage(position)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pager.adapter = OnboardingPagerAdapter(pages)
        binding.pager.registerOnPageChangeCallback(pageChangeCallback)

        dots = buildDots(pages.size)
        updateUiForPage(binding.pager.currentItem)

        binding.btnSkip.setOnClickListener { viewModel.markOnboardingComplete() }

        binding.btnBack.setOnClickListener {
            val current = binding.pager.currentItem
            if (current > 0) binding.pager.currentItem = current - 1
        }

        binding.btnNext.setOnClickListener {
            val current = binding.pager.currentItem
            if (current < pages.lastIndex) {
                binding.pager.currentItem = current + 1
            } else {
                viewModel.markOnboardingComplete()
            }
        }

        collectWhenStarted(viewModel.uiState) { state ->
            if (state.onboardingCompleted) {
                viewModel.onNavigationConsumed()
                navigateToLogin()
            }
        }
    }

    /**
     * Dots are inflated statically into dots_container in XML as individual ImageViews.
     * We just grab references to them here so updateUiForPage() can toggle drawables.
     */
    private fun buildDots(count: Int): List<ImageView> {
        val container = binding.dotsContainer
        return (0 until count).mapNotNull { i -> container.getChildAt(i) as? ImageView }
    }

    private fun updateUiForPage(position: Int) {
        dots.forEachIndexed { i, dot ->
            dot.setImageResource(
                if (i == position) R.drawable.dot_active else R.drawable.dot_inactive
            )
        }

        // Hide back on page 1; INVISIBLE (not GONE) so the layout doesn't reflow.
        binding.btnBack.isInvisible = (position == 0)

        val isLast = (position == pages.lastIndex)
        binding.btnNext.setText(
            if (isLast) R.string.onboarding_get_started else R.string.onboarding_next
        )
    }

    private fun navigateToLogin() {
        val options = NavOptions.Builder()
            .setPopUpTo(R.id.nav_onboarding, inclusive = true)
            .setLaunchSingleTop(true)
            .build()
        findNavController().navigate(R.id.nav_login, null, options)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.pager.unregisterOnPageChangeCallback(pageChangeCallback)
        binding.pager.adapter = null
        _binding = null
    }
}
