package com.example.artistico_anroid.ui.challenges

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.artistico_anroid.R
import com.example.artistico_anroid.core.ui.collectWhenStarted
import com.example.artistico_anroid.databinding.FragmentChallengesBinding
import com.example.artistico_anroid.domain.model.Challenge
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class ChallengesFragment : Fragment() {

    private var _binding: FragmentChallengesBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ChallengesViewModel by viewModels()
    private val previousAdapter = PreviousWeekAdapter()

    private val dateFormat = DateTimeFormatter.ofPattern("d MMM", Locale.getDefault())
        .withZone(ZoneId.systemDefault())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChallengesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Countdown unit labels once.
        binding.countdownDays.txtUnit.text = getString(R.string.days)
        binding.countdownHrs.txtUnit.text = getString(R.string.hrs)
        binding.countdownMins.txtUnit.text = getString(R.string.mins)
        binding.countdownSec.txtUnit.text = getString(R.string.sec)

        binding.viewpagerPrevious.adapter = previousAdapter
        binding.viewpagerPrevious.offscreenPageLimit = 2

        binding.viewpagerPrevious.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updatePageIndicator(position, previousAdapter.itemCount)
            }
        })

        binding.btnAddSubmission.setOnClickListener {
            Snackbar.make(binding.root, "Add submission — coming soon", Snackbar.LENGTH_SHORT).show()
        }
        binding.txtSeeAll.setOnClickListener {
            Snackbar.make(binding.root, "See all previous weeks — coming soon", Snackbar.LENGTH_SHORT).show()
        }
        binding.btnInfo.setOnClickListener {
            Snackbar.make(binding.root, "Challenge rules — coming soon", Snackbar.LENGTH_SHORT).show()
        }

        collectWhenStarted(viewModel.uiState) { state ->
            state.active?.let { bindChallenge(it) }
            bindCountdown(state.timeRemainingMs)
            previousAdapter.submitList(state.previousWeeks) {
                updatePageIndicator(binding.viewpagerPrevious.currentItem, state.previousWeeks.size)
            }
        }
    }

    private fun bindChallenge(challenge: Challenge) {
        binding.imgCover.setImageResource(challenge.coverRes)
        binding.txtStarts.text = getString(R.string.starts_on, dateFormat.format(challenge.startsAt))
        binding.txtEnds.text = getString(R.string.ends_on, dateFormat.format(challenge.endsAt))
        binding.txtDescription.text = challenge.description
    }

    private fun bindCountdown(remainingMs: Long) {
        val days = TimeUnit.MILLISECONDS.toDays(remainingMs)
        val hrs = TimeUnit.MILLISECONDS.toHours(remainingMs) % 24
        val mins = TimeUnit.MILLISECONDS.toMinutes(remainingMs) % 60
        val sec = TimeUnit.MILLISECONDS.toSeconds(remainingMs) % 60

        binding.countdownDays.txtValue.text = String.format(Locale.getDefault(), "%02d", days)
        binding.countdownHrs.txtValue.text = String.format(Locale.getDefault(), "%02d", hrs)
        binding.countdownMins.txtValue.text = String.format(Locale.getDefault(), "%02d", mins)
        binding.countdownSec.txtValue.text = String.format(Locale.getDefault(), "%02d", sec)
    }

    private fun updatePageIndicator(currentPosition: Int, pageCount: Int) {
        val container = binding.pageIndicator
        if (container.childCount != pageCount) {
            container.removeAllViews()
            repeat(pageCount) {
                val dot = ImageView(requireContext())
                val params = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply { marginStart = 6; marginEnd = 6 }
                dot.layoutParams = params
                container.addView(dot)
            }
        }
        for (i in 0 until container.childCount) {
            val dot = container.getChildAt(i) as ImageView
            dot.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    if (i == currentPosition) R.drawable.dot_active else R.drawable.dot_inactive
                )
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.viewpagerPrevious.adapter = null
        _binding = null
    }
}
